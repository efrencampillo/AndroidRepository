package com.globant.repository;

/**
 * Created by efren.campillo on 20/01/16.
 */
public abstract class NetworkOperationAdapter<TP,TH> {

    public abstract TP getFromNetworkSource(TH itemId);
}
