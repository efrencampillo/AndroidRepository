package com.globant.repository;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;


public class DataSourceManagerTest {

    Repository<String, String> repository;
    NetworkOperationAdapter<String, String> networkAdapter;
    RepositoryListener<String> listener;
    MainThreadAttacher attacher;
    boolean isRetrieved = false;
    boolean isUpdated = false;
    boolean isDeleted = false;
    boolean isError = false;

    Context context;

    @Before
    public void setUp() {
        attacher = mock(MainThreadAttacher.class);
        context = mock(Context.class);
        repository = new Repository<>(context);
        repository.mAttacher = attacher;
        networkAdapter = mock(NetworkOperationAdapter.class);

        listener = new RepositoryListener<String>() {
            @Override
            public void onRetrieved(String object) {
                isRetrieved = true;
            }

            @Override
            public void onUpdate(String object) {
                isUpdated = true;
            }

            @Override
            public void onDeleted(String object) {
                isDeleted = true;
            }

            @Override
            public void onError(String object, String message) {
                isError = true;
            }
        };
        repository.registerListener(listener);
        setAttacherBehaviour();
    }

    @After
    public void tearDown() {
        repository.setNetworkAdapter(null);
        repository.clear();
        repository.unregisterListener(listener);
    }

    @Test
    public void shouldReturnNullWithDefaultAdapter() throws Exception {
        repository.get("a");
    }

    @Test
    public void shouldUseNetworkAdapterSet() throws Exception {
        setNetworkAdapterSuccessRetrieve(true);
        repository.setNetworkAdapter(networkAdapter);
        repository.get("a");
    }

    @Test
    public void shouldRetrieveTheDataSavedIfExist() throws Exception {
        setNetworkAdapterSuccessRetrieve(true);
        repository.setNetworkAdapter(networkAdapter);
        repository.get("a");
        Assert.assertTrue(isRetrieved);
    }

    @Test
    public void shouldNotifyErrorOnRetrievedFail() throws Exception {
        setNetworkAdapterSuccessRetrieve(false);
        repository.setNetworkAdapter(networkAdapter);
        repository.get("a");
        Assert.assertTrue(isError);
    }

    @Test
    public void shouldRetrieveObjectIfAlreadyExistWhenGet() throws Exception {
        setNetworkAdapterSuccessRetrieve(true);
        repository.setNetworkAdapter(networkAdapter);
        repository.get("a");
        repository.get("a");
    }

    @Test
    public void shouldRetrieveAllObjectsWhenGetAllCalled() throws Exception {
        setNetworkAdapterSuccessRetrieve(true);
        repository.setNetworkAdapter(networkAdapter);
        repository.get("a");
        repository.getAvailableItems();
    }

    @Test
    public void shouldCallDeleteIfObjectExist() throws Exception {
        setNetworkAdapterSuccessRetrieve(true);
        repository.setNetworkAdapter(networkAdapter);
        repository.get("a");
        repository.deleteFromRepository("a");
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
                when(networkAdapter).
                retrieveModelFromNetworkSource(anyString(), Matchers.any(DataSourceManager.class));
        repository.setNetworkAdapter(networkAdapter);
    }

    private void setAttacherBehaviour() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                listener.onRetrieved("");
                listener.onUpdate("");
                listener.onDeleted("");
                listener.onError("", "");
                return null;
            }
        }).when(attacher).attachEvent(any(RepositoryListener.class), any(NotificationEvent.class));
    }

}