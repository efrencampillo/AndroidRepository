package com.globant.androidrepository;

import android.app.Application;

import com.globant.repository.Repository;

public class MyApplication extends Application {

    private static Repository<
            MyModelExample,//Type of object to handle
            String //type of id to handle
            > repository;

    @Override
    public void onCreate() {
        super.onCreate();
        repository = new Repository<>();
        repository.setNetworkAdapter(new MyOkHttpNetworkAdapter());
        repository.setMaxNumberOfListenerNotified(1);
        repository.setSaveIgnoredEvents(true);
    }

    public static Repository<MyModelExample, String> getRepository() {
        return repository;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        repository.clear();
    }
}
