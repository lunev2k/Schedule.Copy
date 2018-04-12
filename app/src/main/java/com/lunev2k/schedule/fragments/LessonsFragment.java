package com.lunev2k.schedule.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lunev2k.schedule.App;
import com.lunev2k.schedule.R;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.utils.Constants;
import com.lunev2k.schedule.utils.DateTimeUtil;
import com.lunev2k.schedule.utils.PrefsUtils;

import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LessonsFragment extends Fragment {

    @Inject
    Repository mRepository;
    private static final String PAGE_NUMBER = "page_number";
    @Inject
    PrefsUtils mPrefsUtils;
    @BindView(R.id.pager)
    ViewPager pager;
    PagerAdapter pagerAdapter;

    public LessonsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lessons, container, false);
        ButterKnife.bind(this, view);
        App.getComponent().inject(this);
        pagerAdapter = new LessonPagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Date date = mRepository.getDateByPosition(position);
                mPrefsUtils.putLong(Constants.SELECT_DATE, date.getTime());
                Log.d(getClass().getName(), DateTimeUtil.getFormatDate(date));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        int id = mPrefsUtils.getInt(PAGE_NUMBER);
        pager.setCurrentItem(id);
    }

    @Override
    public void onStop() {
        super.onStop();
        int id = pager.getCurrentItem();
        mPrefsUtils.putInt(PAGE_NUMBER, id);
    }

    private class LessonPagerAdapter extends FragmentStatePagerAdapter {

        public LessonPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return LessonPageFragment.newInstance(mRepository.getDateByPosition(position));
        }

        @Override
        public int getCount() {
            return mRepository.getCountDays();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mRepository.getFormatDateByPosition(position);
        }

    }
}
