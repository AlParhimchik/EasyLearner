package com.example.sashok.easylearner.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.ExpandableListViewAdapter;
import com.example.sashok.easylearner.listener.ExpandableListAdapterListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 25.9.17.
 */

public class ExpandableListFragment extends AbsFragment {
    private Context mContext;
    private ExpandableListView mExpandableView;
    private ExpandableListViewAdapter mAdapter;
    private SparseBooleanArray selectedItems;
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;
    private static int currentSelectedIndex = -1;
    private ExpandableListAdapterListener listAdapterListener;
    private RealmController realmController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expandable_list_fragment, container, false);
        mContext = getContext();
        mExpandableView = (ExpandableListView) view.findViewById(R.id.expandableView);
        realmController = RealmController.with(getActivity());
        List<Folder> folders = realmController.getFolders();
        Folder folder=new Folder();
        folder.setName("Без темы");
        folder.setID(-1);
        List<Word> wordsWithoutFolder=realmController.getWordsWithoutFolder();
        for (Word word:wordsWithoutFolder){
            folder.setWord(word);
        }
        folders.add(folder);
        mAdapter = new ExpandableListViewAdapter(getActivity(), folders);
        mExpandableView.setAdapter(mAdapter);
        mExpandableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                View view = parent.findViewById((int) id);

                v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
                if (parent.isGroupExpanded(groupPosition))
                    parent.collapseGroup(groupPosition);
                else
                    parent.expandGroup(groupPosition, true);
                return true;
            }
        });
        mExpandableView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                mAdapter.showWordDialog(groupPosition,childPosition);
                return true;
            }
        });
        return view;

    }

    public void setListener(ExpandableListAdapterListener listener) {
        this.listAdapterListener = listener;
    }

    public static ExpandableListFragment newInstance() {
        return new ExpandableListFragment();
    }


    public void filter(String filter_string) {
        mAdapter.filter(filter_string);
    }

    @Override
    public void OnDataSetChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
