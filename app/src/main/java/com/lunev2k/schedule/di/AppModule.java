package com.lunev2k.schedule.di;

import android.content.Context;

import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.database.Repository;
import com.lunev2k.schedule.utils.PrefsUtils;
import com.lunev2k.schedule.utils.RangeDateUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    PrefsUtils providePrefsUtils(Context context) {
        return new PrefsUtils(context);
    }

    @Provides
    @Singleton
    Repository provideDatabaseRepository(Context context) {
        return new DatabaseRepository(context);
    }

    @Provides
    @Singleton
    RangeDateUtil provideRangeDateUtil() {
        return new RangeDateUtil();
    }
}
