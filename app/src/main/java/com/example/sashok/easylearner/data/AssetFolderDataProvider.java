package com.example.sashok.easylearner.data;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.sashok.easylearner.app.LearnerApplication;
import com.example.sashok.easylearner.model.Folder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 12.10.17.
 */

public class AssetFolderDataProvider extends FolderDataProvider {
    private Context mContext;
    private static String FILE_NAME="realmfolders.json";

    public AssetFolderDataProvider() {
        this.mContext= LearnerApplication.getContext();
    }

    @Override
    public List<Folder> getData() {
        AssetManager am = mContext.getAssets();
       List<Folder> folders=null;
        try {
            InputStream str = am.open(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(str);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            String json = sb.toString();
            Gson gson = new Gson();
            folders = gson.fromJson(json, new TypeToken<List<Folder>>(){}.getType());
            bufferedReader.close();
            isr.close();
            str.close();

        } catch (Exception e) {

        } finally {

        }
        return folders;
    }

}
