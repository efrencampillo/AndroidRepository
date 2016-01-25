package com.globant.repository;

/**
 * Class to wraps the event data.
 *
 * @param <TM> Models to be stored by the Repository.
 *
 * @author Efrén Campillo
 * @author Fernando Sierra Pastrana
 * @version 1.0
 * @since 20/01/16.
 */
public class NotificationEvent<TM> {
    TM mItem;
    int mEventType;
    String mMessage;

    /**
     * Constructor
     *
     * @param item      Result model where it's wrapped the information.
     * @param eventType Type of the event triggered.
     * @param message   Result message of the operation.
     */
    public NotificationEvent(TM item, int eventType, String message) {
        mItem = item;
        mEventType = eventType;
        mMessage = message;
    }

}