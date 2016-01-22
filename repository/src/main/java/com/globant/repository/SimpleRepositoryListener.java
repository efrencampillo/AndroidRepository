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
            NotificationEvent<TP> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_ADDED, null);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

    @Override
    public void onUpdate(TP item) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TP> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_UPDATED, null);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

    @Override
    public void onDeleted(TP item) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TP> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_DELETED, null);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

    @Override
    public void onError(TP item, String message) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TP> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_FAIL, message);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

}