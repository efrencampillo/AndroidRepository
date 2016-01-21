package com.globant.repository;

import org.junit.Test;

public class NotificationEventTest {

    @Test
    public void shouldInitializeClassWithInfo() throws Exception {
        NotificationEvent<Object> event = new NotificationEvent<>(null, 0, null);
    }

}