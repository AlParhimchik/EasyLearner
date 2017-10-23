package com.example.sashok.easylearner.data;

import android.app.Activity;

import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 21.10.17.
 */

public class RealmDBFolderDataProvider extends FolderDataProvider {
    private final Activity context;

    public RealmDBFolderDataProvider(Activity context) {
        this.context = context;
    }

    @Override
    public List<Folder> getData() {
        RealmController controller = RealmController.with(context);
        return controller.getFolders();
    }
}
