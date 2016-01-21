package com.globant.repository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class RepositoryTest {

    Repository<String, String> repository;
    NetworkOperationAdapter<String, String> networkOperationAdapter;
    RepositoryListener<String> listener;

    @Before
    public void setUp() throws Exception {
        repository = new Repository<>();
        networkOperationAdapter = mock(NetworkOperationAdapter.class);
        listener = mock(RepositoryListener.class);
    }

    @Test
    public void shouldSaveEventIfThereIsNoListener() {
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(true);
        repository.get("a");
        verify(listener, never()).onRetrieved(Matchers.anyString());
        NotificationEvent<String> notificationEvent =
                repository.mPendingNotifications.getNextPendingNotification();
        assertTrue(notificationEvent != null);
    }

    @Test
    public void shouldNotifyListenerIfIsRegisteredAtGetItem() {
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(true);
        repository.registerListener(listener);
        repository.get("a");
        verify(listener, atLeastOnce()).onRetrieved(Matchers.anyString());
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldNotifyListenerIfIsRegisteredAtUpdateItem() {
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(true);
        repository.registerListener(listener);
        repository.deliverUpdated("a");
        verify(listener, atLeastOnce()).onUpdate(Matchers.anyString());
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldNotifyListenerWithErrorAtErrorRetrieve() {
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(false);
        repository.registerListener(listener);
        repository.get("a");
        verify(listener, atLeastOnce()).onError(anyString(), Matchers.anyString());
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldNotifyDeleteInListenerWhenDelete() {
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(true);
        repository.registerListener(listener);
        repository.get("a");
        repository.deleteFromRepository("a");
        verify(listener, atLeastOnce()).onDeleted(anyString());
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldNotifyOnRetrieveIfAlreadyExist() {
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(true);
        repository.registerListener(listener);
        repository.get("a");
        repository.get("a");
        verify(listener, atLeastOnce()).onRetrieved(anyString());
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldWorkResumingEvents() {
        //separate this test cases because i was hurry and compact all the rest of the coverage here
        repository.setMaxNumberOfListenerNotified(1);
        repository.setSaveIgnoredEvents(true);
        setNetworkAdapterSuccessRetrieve(true);
        repository.resumePendingEvents();
        repository.get("a");
        repository.deleteFromRepository("a");
        setNetworkAdapterSuccessRetrieve(false);
        repository.get("a");
        repository.resumePendingEvents();
        repository.resumePendingEvents();
        repository.resumePendingEvents();
        repository.unregisterListener(listener);
        repository.clear();
    }

    private void setNetworkAdapterSuccessRetrieve(final boolean success) {
        Answer answer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                if (success) {
                    repository.mDataSourceManager.insertRetrievedItem("a", "a");
                } else {
                    repository.mDataSourceManager.onErrorRetrievedItem("a");
                }
                return null;
            }
        };
        doAnswer(answer).
                when(networkOperationAdapter).
                retrieveModelFromNetworkSource(anyString(), Matchers.any(DataSourceManager.class));
        repository.setNetworkAdapter(networkOperationAdapter);
    }

}