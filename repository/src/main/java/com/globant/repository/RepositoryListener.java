package com.globant.repository;

public interface RepositoryListener<TM> {
    void onRetrieved(TM object);

    void onUpdate(TM object);

    void onDeleted(TM object);

    void onError(TM object, String message);

}