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
            public TP getFromNetworkSource(TH itemId) {
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

    void retrieveItem(final TH itemId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TP item = mNetworkOperationAdapter.getFromNetworkSource(itemId);
                if (item != null) {
                    insertRetrievedItem(itemId, item);
                } else {
                    myRepository.deliverError(item, "Error at retrieving Item " + itemId);
                }
            }
        }).start();
    }

    void insertRetrievedItem(TH itemId, TP item) {
        mItems.put(itemId, item);
        myRepository.deliverRetrieved(item);
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
