package com.globant.repository;

import android.content.Context;

import java.util.ArrayList;

public final class Repository<TP, TH> {

    DataSourceManager<TP, TH> mDataSourceManager;
    private ArrayList<RepositoryListener<TP>> mListeners;
    MainThreadAttacher mAttacher;

    public Repository(Context context) {
        mListeners = new ArrayList<>();
        mPendingNotifications = new RepositoryPendingNotifications<>();
        mDataSourceManager = new DataSourceManager<>();
        mDataSourceManager.setRepository(this);
        mAttacher = new MainThreadAttacher(context.getMainLooper(), this);
    }

    public void registerListener(RepositoryListener<TP> listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    public void unregisterListener(RepositoryListener<TP> listener) {
        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    public void get(TH itemId) {
        if (mDataSourceManager.contains(itemId)) {
            deliverRetrieved(mDataSourceManager.get(itemId));
            return;
        }

        mDataSourceManager.retrieveItem(itemId, false);
    }

    public void getForcingRefresh(TH itemId) {
        mDataSourceManager.retrieveItem(itemId, true);
    }

    public ArrayList<TP> getAvailableItems() {
        return mDataSourceManager.getAll();
    }

    public void deleteFromRepository(TH itemId) {
        TP item = mDataSourceManager.deleteItem(itemId);
        if (item != null) {
            deliverDeleted(item);
        }
    }

    protected void deliverRetrieved(TP item) {

        NotificationEvent<TP> notificationEvent =
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
    protected void deliverUpdated(TP item) {
        NotificationEvent<TP> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_UPDATED, null);


        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    protected void deliverDeleted(TP item) {
        NotificationEvent<TP> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_DELETED, null);

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    protected void deliverError(TP item, String messageError) {
        NotificationEvent<TP> notificationEvent =
                new NotificationEvent<>(item, RepositoryPendingNotifications.EVENT_FAIL,
                        messageError);

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners(notificationEvent);
    }

    private void notifyListeners(NotificationEvent<TP> notificationEvent) {
        int numberOfNotifications = 0;
        for (RepositoryListener<TP> listener : mListeners) {
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
    RepositoryPendingNotifications<TP> mPendingNotifications;
    private int maxNumberOfNotifications = 1;
    boolean mSaveIgnoredEvents = false;

    public void resumePendingEvents() {
        NotificationEvent<TP> notificationEvent = mPendingNotifications.getNextPendingNotification();

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(notificationEvent);
            return;
        }

        notifyListeners( notificationEvent);
    }



    /**
     * this method receive the max number of notifications that the callback has to call
     *
     * @param maxNumber must be greater than 0
     */
    public void setMaxNumberOfListenerNotified(int maxNumber) {
        if (maxNumber > 0) {
            maxNumberOfNotifications = maxNumber;
        }
    }

    /**
     * this method set the flag to keep events when the  repository has no one to notify any event
     *
     * @param newHaveToSaveIgnoredEvents true for save the notifications and send in next #resumePendingEvents()
     */
    public void setSaveIgnoredEvents(boolean newHaveToSaveIgnoredEvents) {
        mSaveIgnoredEvents = newHaveToSaveIgnoredEvents;
    }

    public void setNetworkAdapter(NetworkOperationAdapter<TP, TH> adapter) {
        mDataSourceManager.setNetworkOperationAdapter(adapter);
    }
}
