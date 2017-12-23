package com.teamtreehouse.ribbit;

import android.app.Application;

import com.teamtreehouse.ribbit.mockdata.MockUsers;

public class RibbitApplication extends Application {

    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        MockUsers.initialize();

        PACKAGE_NAME = getApplicationContext().getPackageName();
    }
}
