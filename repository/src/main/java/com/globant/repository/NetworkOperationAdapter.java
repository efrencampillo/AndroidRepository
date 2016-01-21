package com.globant.repository;

public abstract class NetworkOperationAdapter<TP, TH> {

    void retrieveModelFromNetworkSource(final TH itemId,
                                        final DataSourceManager<TP, TH> dataSourceManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TP item = getModelFromNetworkSource(itemId);
                if (item != null) {
                    dataSourceManager.insertRetrievedItem(itemId, item);
                } else {
                    dataSourceManager.onErrorRetrievedItem(
                            "Error at retrieving object with ID:" + itemId.toString());
                }
            }
        }).start();
    }

    void retrieveModelForcingRefreshFromNetworkSource(final TH itemId,
                                                      final DataSourceManager<TP, TH> dataSourceManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TP item = getModelFromNetworkSource(itemId);
                if (item != null) {
                    dataSourceManager.insertUpdatedItem(itemId, item);
                } else {
                    dataSourceManager.onErrorRetrievedItem(
                            "Error at retrieving object with ID:" + itemId.toString());
                }
            }
        }).start();
    }

    public abstract TP getModelFromNetworkSource(TH itemId);
}
