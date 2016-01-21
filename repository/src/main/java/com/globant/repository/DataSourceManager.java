package com.globant.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataSourceManager<TP, TH> {

    protected Map<TH, TP> mItems;

    private Repository<TP, TH> myRepository;
    private NetworkOperationAdapter<TP, TH> mNetworkOperationAdapter;

    DataSourceManager() {
        mItems = new LinkedHashMap<>();
        mNetworkOperationAdapter = new NetworkOperationAdapter<TP, TH>() {
            @Override
            public TP getModelFromNetworkSource(TH itemId) {
                return null;
            }
        };
    }

    void setNetworkOperationAdapter(NetworkOperationAdapter<TP, TH> adapter) {
        mNetworkOperationAdapter = adapter;
    }

    protected void setRepository(Repository<TP, TH> repository) {
        myRepository = repository;
    }

    boolean contains(TH itemId) {
        return mItems.containsKey(itemId);
    }

    void retrieveItem(final TH itemId, boolean forceRefresh) {
        if (forceRefresh) {
            mNetworkOperationAdapter.retrieveModelForcingRefreshFromNetworkSource(itemId, this);
        } else {
            mNetworkOperationAdapter.retrieveModelFromNetworkSource(itemId, this);
        }
    }

    void insertUpdatedItem(TH itemId, TP item) {
        mItems.put(itemId, item);
        myRepository.deliverUpdated(item);
    }

    void insertRetrievedItem(TH itemId, TP item) {
        mItems.put(itemId, item);
        myRepository.deliverRetrieved(item);
    }

    void onErrorRetrievedItem(String message) {
        myRepository.deliverError(null, message);
    }

    TP get(TH itemId) {
        return mItems.get(itemId);
    }

    ArrayList<TP> getAll() {
        return new ArrayList<>(mItems.values());
    }

    void deleteAllItems() {
        mItems.clear();
    }

    TP deleteItem(TH itemId) {
        return mItems.remove(itemId);
    }

}
