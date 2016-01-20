package com.globant.repository;

public interface RepositoryListener<TP> {
    void onRetrieved(TP object);

    void onUpdate(TP object);

    void onDeleted(TP object);

    void onError(TP object, String message);

}