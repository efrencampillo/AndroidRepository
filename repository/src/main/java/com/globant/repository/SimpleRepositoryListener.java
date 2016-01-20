package com.globant.repository;


/**
 * Simple Repository class
 */
public abstract class SimpleRepositoryListener<TP, TH> implements RepositoryListener<TP> {
    Repository<TP, TH> mRepository;

    public SimpleRepositoryListener(Repository<TP, TH> repository) {
        mRepository = repository;
    }

    @Override
    public void onRetrieved(TP item) {
        if (mRepository.mSaveIgnoredEvents) {
            mRepository.mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_ADDED, null);
        }
    }

    @Override
    public void onUpdate(TP item) {
        if (mRepository.mSaveIgnoredEvents) {
            mRepository.mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_UPDATED, null);
        }
    }

    @Override
    public void onDeleted(TP item) {
        if (mRepository.mSaveIgnoredEvents) {
            mRepository.mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_DELETED, null);
        }
    }

    @Override
    public void onError(TP item, String message) {
        if (mRepository.mSaveIgnoredEvents) {
            mRepository.mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_DELETED, message);
        }
    }

}