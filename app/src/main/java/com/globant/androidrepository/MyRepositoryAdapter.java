package com.globant.androidrepository;

import android.view.View;
import android.view.ViewGroup;

import com.globant.repository.Repository;
import com.globant.repository.RepositoryViews.RepositoryAdapter;

/**
 * Created by efren.campillo on 24/02/16.
 */
public class MyRepositoryAdapter extends RepositoryAdapter<MyModelExample, String> {
    @Override
    public Repository<MyModelExample, String> provideRepository() {
        return MyApplication.getRepository();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyModelExample modelExample = getItem(position);
        //TODO insert model in view
        return convertView;
    }
}
