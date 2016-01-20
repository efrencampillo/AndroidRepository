package com.globant.androidrepository;

import com.globant.repository.NetworkOperationAdapter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MyOkHttpNetworkAdapter extends NetworkOperationAdapter<MyModelExample, String> {

    OkHttpClient mClient = new OkHttpClient();

    @Override
    public MyModelExample getFromNetworkSource(String itemId) {
        try {
            Request request = new Request.Builder()
                    .url("http://www.google.com")
                    .build();
            Response response = mClient.newCall(request).execute();
            return new MyModelExample();
        } catch (Exception e) {
            return null;
        }
    }
}
