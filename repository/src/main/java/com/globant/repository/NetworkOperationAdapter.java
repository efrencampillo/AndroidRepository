package com.globant.repository;

public abstract class NetworkOperationAdapter<TM, TI> {

    void retrieveModelFromNetworkSource(final TI itemId,
                                        final DataSourceManager<TM, TI> dataSourceManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TM item = getModelFromNetworkSource(itemId);
                if (item != null) {
                    dataSourceManager.insertRetrievedItem(itemId, item);
                } else {
                    dataSourceManager.onErrorRetrievedItem(
                            "Error at retrieving object with ID:" + itemId.toString());
                }
            }
        }).start();
    }

    void retrieveModelForcingRefreshFromNetworkSource(final TI itemId,
                                                      final DataSourceManager<TM, TI> dataSourceManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TM item = getModelFromNetworkSource(itemId);
                if (item != null) {
                    dataSourceManager.insertUpdatedItem(itemId, item);
                } else {
                    dataSourceManager.onErrorRetrievedItem(
                            "Error at retrieving object with ID:" + itemId.toString());
                }
            }
        }).start();
    }

    public abstract TM getModelFromNetworkSource(TI itemId);
}
