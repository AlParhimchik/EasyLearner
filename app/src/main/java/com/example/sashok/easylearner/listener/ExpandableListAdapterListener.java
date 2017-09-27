package com.example.sashok.easylearner.listener;

/**
 * Created by sashok on 26.9.17.
 */

public interface ExpandableListAdapterListener {

    void onFolderClicked(int position);

    void onWordClicked(int position);

    void onFolderLongClicked(int position);

    void onWordLongClicked(int position);
}
