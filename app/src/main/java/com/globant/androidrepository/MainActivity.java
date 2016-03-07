package com.globant.androidrepository;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.globant.repository.Repository;
import com.globant.repository.RepositoryListener;
import com.globant.repository.RepositoryViews.RepositoryActivity;
import com.globant.repository.SimpleRepositoryListener;

public class MainActivity extends RepositoryActivity<MyModelExample, String> {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public Repository<MyModelExample, String> provideRepository() {
        return MyApplication.getRepository();
    }

    @Override
    public RepositoryListener<MyModelExample> provideRepositoryListener() {
        return new SimpleRepositoryListener<MyModelExample, String>(MyApplication.getRepository()) {
            @Override
            public void onRetrieved(MyModelExample item) {
                ///retrieved model
            }

            @Override
            public void onUpdate(MyModelExample item) {
                Toast.makeText(MainActivity.this, "updated model", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getRepository().get("itemId");
    }

}
