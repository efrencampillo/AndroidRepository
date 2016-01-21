package com.globant.repository;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;

public class SimpleRepositoryListenerTest {

    Repository<String, String> repository = new Repository<>();
    SimpleRepositoryListener<String, String> listener;

    @Before
    public void setUp() {
        repository.setSaveIgnoredEvents(true);
        listener = new SimpleRepositoryListener<String, String>(repository) {
        };
        repository.registerListener(listener);
    }

    @After
    public void tearDown() {
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldSaveEventAdded() throws Exception {
        repository.deliverRetrieved("a");
    }

    @Test
    public void shouldSaveEventUpdate() throws Exception {
        repository.deliverUpdated("a");
    }

    @Test
    public void shouldSaveEventDelete() throws Exception {
        repository.deliverDeleted("a");
    }

    @Test
    public void shouldSaveEventError() throws Exception {
        repository.deliverError(null, "a");
    }
}