package com.globant.repository;

import android.os.Handler;
import android.os.Looper;

/**
 * This class have the duty to manage the events going to be dispatch on the correct thread.
 *
 * @param <TM> Models to be stored by the Repository.
 *
 * @author Efrén Campillo
 * @author Fernando Sierra Pastrana
 * @version 1.0
 * @since 22/01/16.
 */
public class MainThreadAttacher<TM> {

    Looper mMainLooper;
    Repository mRepository;

    /**
     * Constructor
     *
     * @param mainThread Main thread where will be joined the events.
     * @param repository Reference where will be attached.
     */
    MainThreadAttacher (Looper mainThread, Repository repository) {
        mMainLooper = mainThread;
        mRepository = repository;
    }

    /**
     * Notifies the event on the correct thread.
     *
     * @param listener          Listener to triggers the events.
     * @param notificationEvent Event to be attached to the thread.
     */
    void attachEvent(final RepositoryListener<TM> listener,
                     final NotificationEvent<TM> notificationEvent) {
        // If it's current thread the expected.
        if(Thread.currentThread() == mMainLooper.getThread()) {
            deliverEvent(listener, notificationEvent);
        } else {
            // Triggers the event on the main thread defined.
            Handler handler = new Handler(mMainLooper);
            handler.post(new Runnable() {
                @Override
                public void run() {
                   deliverEvent(listener, notificationEvent);
                }
            });
        }
    }

    /**
     * Sends the event through the listener.
     *
     * @param listener          Listener where will be sent the event.
     * @param notificationEvent Event to be notified.
     */
    private void deliverEvent(RepositoryListener<TM> listener,
                              NotificationEvent<TM> notificationEvent) {

        if (notificationEvent == null) {
            return;
        }

        switch (notificationEvent.mEventType) {
            case RepositoryPendingNotifications.EVENT_ADDED:
                listener.onRetrieved(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_DELETED:
                listener.onDeleted(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_UPDATED:
                listener.onUpdate(notificationEvent.mItem);
                break;
            case RepositoryPendingNotifications.EVENT_FAIL:
                listener.onError(notificationEvent.mItem, notificationEvent.mMessage);
                break;
        }
    }
}
