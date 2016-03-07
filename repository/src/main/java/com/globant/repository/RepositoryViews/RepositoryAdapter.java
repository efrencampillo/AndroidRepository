package com.globant.repository.RepositoryViews;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.globant.repository.Repository;

/**
 * Created by efren.campillo on 24/02/16.
 */
public abstract class RepositoryAdapter<TM, TI> extends BaseAdapter {
    protected Repository<TM, TI> repository;

    public abstract Repository<TM, TI> provideRepository();

    public RepositoryAdapter() {
        super();
        repository = provideRepository();
    }

    @Override
    public final int getCount() {
        return repository.getAvailableItems().size();
    }

    @Override
    public TM getItem(int position) {
        return repository.getAvailableItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
