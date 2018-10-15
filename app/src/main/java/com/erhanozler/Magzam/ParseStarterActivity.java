package com.erhanozler.Magzam;

import android.app.Application;

import com.parse.Parse;

public class ParseStarterActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        Parse.initialize(new Parse.Configuration.Builder(this)
        .applicationId("Di7ZdAhGYp70TcbqIn0lnmJmAQo0x13o3UyCon6G")
        .clientKey("MG9BDL7vdOoQ5nxEzzjblISlghrV3TpQLPJqIKqO")
        .server("https://parseapi.back4app.com/")
        .build()
        );


    }
}
