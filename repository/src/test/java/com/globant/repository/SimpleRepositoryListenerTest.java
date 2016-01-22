package com.globant.repository;

import android.content.Context;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimpleRepositoryListenerTest {

    Repository<String, String> repository;
    SimpleRepositoryListener<String, String> listener;
    Context context;
    MainThreadAttacher attacher;

    @Before
    public void setUp() {
        attacher = mock(MainThreadAttacher.class);
        context = mock(Context.class);
        repository  = new Repository<>(context);
        repository.setSaveIgnoredEvents(true);
        listener = new SimpleRepositoryListener<String, String>(repository) {
        };
        repository.registerListener(listener);
        repository.mAttacher = attacher;
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