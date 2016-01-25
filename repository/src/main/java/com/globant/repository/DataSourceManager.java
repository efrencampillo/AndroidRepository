package com.globant.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @param <TM> Models to be stored by the Repository.
 * @param <TI> Model's id.
 * @author Efrén Campillo
 * @author Fernando Sierra Pastrana
 * @version 1.0
 * @since 25/02/2016
 */
public class DataSourceManager<TM, TI> {

    protected Map<TI, TM> mItems;

    private Repository<TM, TI> myRepository;
    private NetworkOperationAdapter<TM, TI> mNetworkOperationAdapter;

    DataSourceManager() {
        mItems = new LinkedHashMap<>();
        mNetworkOperationAdapter = new NetworkOperationAdapter<TM, TI>() {
            @Override
            public TM getModelFromNetworkSource(TI itemId) {
                return null;
            }
        };
    }

    void setNetworkOperationAdapter(NetworkOperationAdapter<TM, TI> adapter) {
        mNetworkOperationAdapter = adapter;
    }

    protected void setRepository(Repository<TM, TI> repository) {
        myRepository = repository;
    }

    boolean contains(TI itemId) {
        return mItems.containsKey(itemId);
    }

    void retrieveItem(final TI itemId, boolean forceRefresh) {
        if (forceRefresh) {
            mNetworkOperationAdapter.retrieveModelForcingRefreshFromNetworkSource(itemId, this);
        } else {
            mNetworkOperationAdapter.retrieveModelFromNetworkSource(itemId, this);
        }
    }

    void insertUpdatedItem(TI itemId, TM item) {
        mItems.put(itemId, item);
        myRepository.deliverUpdated(item);
    }

    void insertRetrievedItem(TI itemId, TM item) {
        mItems.put(itemId, item);
        myRepository.deliverRetrieved(item);
    }

    void onErrorRetrievedItem(String message) {
        myRepository.deliverError(null, message);
    }

    TM get(TI itemId) {
        return mItems.get(itemId);
    }

    ArrayList<TM> getAll() {
        return new ArrayList<>(mItems.values());
    }

    void deleteAllItems() {
        mItems.clear();
    }

    TM deleteItem(TI itemId) {
        return mItems.remove(itemId);
    }

}
