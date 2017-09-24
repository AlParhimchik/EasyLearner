package com.example.sashok.easylearner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.easylearner.R;

/**
 * Created by sashok on 24.9.17.
 */

public class AddFolderFragment extends Fragment {

    public  AddFolderFragment(){

    }
    public static AddFolderFragment newInstance(){
        AddFolderFragment folderFragment=new AddFolderFragment();
        return folderFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_folder_fragment,container, false);
    }
}
