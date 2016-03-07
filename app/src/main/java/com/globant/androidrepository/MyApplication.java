package com.globant.androidrepository;

import android.app.Application;
import android.content.Context;

import com.globant.repository.Repository;

public class MyApplication extends Application {

    private static Repository<MyModelExample, String> mRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        createRepository(this);
    }

    public static Repository<MyModelExample, String> getRepository() {
        return mRepository;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mRepository.clear();
    }

    private void createRepository( Context context) {
        mRepository = new Repository<>(context);
        mRepository.setNetworkAdapter(new MyOkHttpNetworkAdapter());
        mRepository.setMaxNumberOfListenerNotified(1);
        mRepository.setSaveIgnoredEvents(true);
    }
}
