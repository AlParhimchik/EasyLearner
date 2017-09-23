package com.example.sashok.easylearner.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.sashok.easylearner.R;
//import com.example.sashok.easylearner.realm.RealmController;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // RealmController realm = RealmController.getInstance();


    }
}
