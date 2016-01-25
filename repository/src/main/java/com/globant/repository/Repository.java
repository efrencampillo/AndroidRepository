package com.globant.repository;

import android.content.Context;

import java.util.ArrayList;

/**
 * Class to manage the request, response and error of network and storage operations.
 *
 * @param <TM> Models to be stored by the Repository.
 * @param <TI> Model's id.
 * @author Efrén Campillo
 * @author Fernando Sierra Pastrana
 * @version 1.0
 * @since 25/02/2016
 */
public final class Repository<TM, TI> {

    DataSourceManager<TM, TI> mDataSourceManager;
    private final ArrayList<RepositoryListener<TM>> mListeners;
    MainThreadAttacher mAttacher;

    public Repository(Context context) {
        mListeners = new ArrayList<>();
        mPendingNotifications = new RepositoryPendingNotifications<>();
        mDataSourceManager = new DataSourceManager<>();
        mDataSourceManager.setRepository(this);
        mAttacher = new MainThreadAttacher(context.getMainLooper(), this);
    }

    public void registerListener(RepositoryListener<TM> listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void unregisterListener(RepositoryListener<TM> listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void get(TI itemId) {
        if (mDataSourceManager.contains(itemId)) {
            deliverRetrieved(mDataSourceManager.get(itemId));
            return;
        }

        mDataSourceManager.retrieveItem(itemId, false);
    }

    public void getForcingRefresh(TI itemId) {
        mDataSourceManager.retrieveItem(itemId, true);
    }

    public ArrayList<TM> getAvailableItems() {
        return mDataSourceManager.getAll();
    }

    public void deleteFromRepository(TI itemId) {
        TM item = mDataSourceManager.deleteItem(itemId);
        if (item != null) {
            deliverDeleted(item);
        }
    }

    protected void deliverRetrieved(TM item) {

        NotificationEvent<TM> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_ADDED, null);

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    /*
    * there is no call to this method, we have to write the update request
    * */
    protected void deliverUpdated(TM item) {
        NotificationEvent<TM> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_UPDATED, null);


        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    protected void deliverDeleted(TM item) {
        NotificationEvent<TM> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_DELETED, null);

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    protected void deliverError(TM item, String messageError) {
        NotificationEvent<TM> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_FAIL,
                        messageError);

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    private void notifyListeners(NotificationEvent<TM> notificationEvent) {
        int numberOfNotifications = 0;
        for (RepositoryListener<TM> listener : mListeners) {
            mAttacher.attachEvent(listener, notificationEvent);
            numberOfNotifications++;
            if (!(numberOfNotifications < maxNumberOfNotifications)) {
                break;
            }
        }
    }

    public void clear() {
        mDataSourceManager.deleteAllItems();
        mPendingNotifications.clear();
    }

    //pending operations struct and configurations
    RepositoryPendingNotifications<TM> mPendingNotifications;
    private int maxNumberOfNotifications = 1;
    boolean mSaveIgnoredEvents = false;

    public void resumePendingEvents() {
        NotificationEvent<TM> notificationEvent = mPendingNotifications.getNextPendingNotification();

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners( notificationEvent);
    }



    /**
     * This method receive the max number of notifications that the callback has to call
     *
     * @param maxNumber must be greater than 0
     */
    public void setMaxNumberOfListenerNotified(int maxNumber) {
        if (maxNumber > 0) {
            maxNumberOfNotifications = maxNumber;
        }
    }

    /**
     * This method set the flag to keep events when the repository has no one to notify any event
     *
     * @param newHaveToSaveIgnoredEvents true for save the notifications and send in next {@link #resumePendingEvents()}
     */
    public void setSaveIgnoredEvents(boolean newHaveToSaveIgnoredEvents) {
        mSaveIgnoredEvents = newHaveToSaveIgnoredEvents;
    }

    public void setNetworkAdapter(NetworkOperationAdapter<TM, TI> adapter) {
        mDataSourceManager.setNetworkOperationAdapter(adapter);
    }
}
