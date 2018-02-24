package com.lunev2k.schedule;

import android.app.Application;

import com.lunev2k.schedule.di.AppComponent;
import com.lunev2k.schedule.di.AppModule;
import com.lunev2k.schedule.di.DaggerAppComponent;

public class App extends Application {

    private static AppComponent component;

    public static AppComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .build();
    }
}
