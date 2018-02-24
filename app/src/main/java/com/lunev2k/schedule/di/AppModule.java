package com.lunev2k.schedule.di;

import android.content.Context;

import com.lunev2k.schedule.database.DatabaseRepository;
import com.lunev2k.schedule.utils.PrefsUtils;
import com.lunev2k.schedule.utils.RangeDateUtil;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    PrefsUtils providePrefsUtils(Context context) {
        return new PrefsUtils(context);
    }

    @Provides
    DatabaseRepository provideDatabaseRepository(Context context) {
        return new DatabaseRepository(context);
    }

    @Provides
    RangeDateUtil provideRangeDateUtil() {
        return new RangeDateUtil();
    }
}
