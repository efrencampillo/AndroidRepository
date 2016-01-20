package com.globant.repository;

import java.util.ArrayList;

public class Repository<TP, TH> {

    private DataSourceManager<TP, TH> mDataSourceManager;
    private ArrayList<RepositoryListener<TP>> mListeners;

    public Repository() {
        mListeners = new ArrayList<>();
        mPendingNotifications = new RepositoryPendingNotifications<>();
        mDataSourceManager = new DataSourceManager<>();
        mDataSourceManager.setRepository(this);
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

        mDataSourceManager.retrieveItem(itemId);
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

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_ADDED, null);
            return;
        }

        int numberOfNotifications = 0;
        for (RepositoryListener<TP> listener : mListeners) {
            listener.onRetrieved(item);
            numberOfNotifications++;
            if (!(numberOfNotifications < maxNumberOfNotifications)) {
                break;
            }
        }
    }

    protected void deliverUpdated(TP item) {

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_UPDATED, null);
            return;
        }

        int numberOfNotifications = 0;
        for (RepositoryListener<TP> listener : mListeners) {
            listener.onUpdate(item);
            numberOfNotifications++;
            if (!(numberOfNotifications < maxNumberOfNotifications)) {
                break;
            }
        }
    }

    protected void deliverDeleted(TP item) {

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_DELETED, null);
            return;
        }

        int numberOfNotifications = 0;
        for (RepositoryListener<TP> listener : mListeners) {
            listener.onDeleted(item);
            numberOfNotifications++;
            if (!(numberOfNotifications < maxNumberOfNotifications)) {
                break;
            }
        }
    }

    protected void deliverError(TP item, String messageError) {

        if (mListeners.isEmpty()) {
            mPendingNotifications.addPendingNotification(item,
                    RepositoryPendingNotifications.EVENT_FAIL, messageError);
            return;
        }

        int numberOfNotifications = 0;
        for (RepositoryListener<TP> listener : mListeners) {
            listener.onError(item, messageError);
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

        if (notificationEvent == null) {
            return;
        }

        switch (notificationEvent.mEventType) {
            case RepositoryPendingNotifications.EVENT_ADDED:
                deliverRetrieved(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_DELETED:
                deliverDeleted(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_UPDATED:
                deliverUpdated(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_FAIL:
                deliverError(notificationEvent.mItem, notificationEvent.mMessage);
                break;
        }
    }

    /**
     * this method receive the max number of notifications that the callback has to call
     *
     * @param maxNumber
     * must be greater than 0
     */
    public void setMaxNumberOfListenerNotified(int maxNumber) {
        if (maxNumber > 0) {
            maxNumberOfNotifications = maxNumber;
        }
    }

    /**
     * this method set the flag to keep events when the  repository has no one to notify any event
     * @param newHaveToSaveIgnoredEvents
     * true for save the notifications and send in next #resumePendingEvents()
     */
    public void setSaveIgnoredEvents(boolean newHaveToSaveIgnoredEvents) {
        mSaveIgnoredEvents = newHaveToSaveIgnoredEvents;
    }

    public void setNetworkAdapter(NetworkOperationAdapter<TP,TH> adapter) {
        mDataSourceManager.setNetworkOperationAdapter(adapter);
    }
}
