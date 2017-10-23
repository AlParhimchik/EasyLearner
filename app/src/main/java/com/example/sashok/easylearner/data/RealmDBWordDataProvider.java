package com.example.sashok.easylearner.data;

import android.app.Activity;

import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 21.10.17.
 */

public class RealmDBWordDataProvider extends WordDataProvider {
    private final Activity activity;

    public RealmDBWordDataProvider(Activity activity) {
        this.activity = activity;
    }

    @Override
    public List<Word> getData() {
        RealmController controller=RealmController.with(activity);
        return controller.getWords();
    }
}
