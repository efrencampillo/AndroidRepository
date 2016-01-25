package com.globant.repository;


/**
 * Simple Repository class
 */
public abstract class SimpleRepositoryListener<TM, TI> implements RepositoryListener<TM> {
    Repository<TM, TI> mRepository;

    public SimpleRepositoryListener(Repository<TM, TI> repository) {
        mRepository = repository;
    }

    @Override
    public void onRetrieved(TM item) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TM> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_ADDED, null);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

    @Override
    public void onUpdate(TM item) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TM> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_UPDATED, null);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

    @Override
    public void onDeleted(TM item) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TM> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_DELETED, null);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

    @Override
    public void onError(TM item, String message) {
        if (mRepository.mSaveIgnoredEvents) {
            NotificationEvent<TM> notificationEvent =
                    new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_FAIL, message);
            mRepository.mPendingNotifications.addPendingNotification(notificationEvent);
        }
    }

}