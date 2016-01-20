package com.globant.androidrepository;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.globant.repository.SimpleRepositoryListener;

public class MainActivity extends AppCompatActivity {

    SimpleRepositoryListener<MyModelExample, String> repositoryListener
            = new SimpleRepositoryListener<MyModelExample, String>(MyApplication.getRepository()) {
        @Override
        public void onRetrieved(MyModelExample item) {
            //super.onRetrieved(item); call super to back the event to repository
            MyApplication.getRepository().getAvailableItems();
            MyApplication.getRepository().deleteFromRepository("itemIdToErase");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getRepository().registerListener(repositoryListener);
        MyApplication.getRepository().resumePendingEvents();
        MyApplication.getRepository().get("itemId");
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getRepository().unregisterListener(repositoryListener);
    }

}
