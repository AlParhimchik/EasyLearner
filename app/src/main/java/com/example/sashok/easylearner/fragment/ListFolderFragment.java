package com.example.sashok.easylearner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.activity.MainActivity;
import com.example.sashok.easylearner.listener.FolderLongClickLListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.FolderView;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.model.WordView;
import com.example.sashok.easylearner.realm.RealmController;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;
import com.mindorks.placeholderview.Utils;

import java.util.List;

/**
 * Created by sashok on 25.9.17.
 */

public class ListFolderFragment  extends Fragment implements FolderLongClickLListener{
    private Context mContext;
    private ExpandablePlaceHolderView mExpandableView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.folders_with_words_fragment,container,false);
        mContext = getContext();
        mExpandableView = (ExpandablePlaceHolderView)view.findViewById(R.id.expandableView);
        RealmController realmController=RealmController.with(getActivity());
        List<Folder> folders=realmController.getFolders();
        List<Word> words;
        for(Folder folder : folders){
            mExpandableView.addView(new FolderView(mContext, folder,this));
            words=folder.getWords();
            for(Word word : words){
                mExpandableView.addView(new WordView(mContext, word));
            }
        }
        return view;
    }

    public ListFolderFragment(){

    }

    public static ListFolderFragment newInstance(){
        return new ListFolderFragment();
    }

    @Override
    public void onFolderLongClicked(Folder folder) {
        Log.i("TAG","EEE");

    }
}
