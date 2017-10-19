package com.example.sashok.easylearner.data;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.sashok.easylearner.app.LearnerApplication;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sashok on 17.10.17.
 */

public class WordDataProvider implements DataProvider<Word> {
    private final Activity activity;

    public WordDataProvider(Activity activity) {
        this.activity = activity;
    }

    @Override
    public List<Word> getData() {
        RealmController controller=RealmController.with(activity);
        return controller.getWords();
    }
}
