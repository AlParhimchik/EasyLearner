package com.example.sashok.easylearner.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.ExpandableListViewAdapter;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 25.9.17.
 */

public class ExpandableListFragment extends Fragment {
    private Context mContext;
    private ExpandableListView mExpandableView;
    private ExpandableListViewAdapter mAdapter;

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
        RealmController realmController = RealmController.with(getActivity());
        List<Folder> folders = realmController.getFolders();
        mAdapter = new ExpandableListViewAdapter(getContext(), folders);
        mExpandableView.setAdapter(mAdapter);
        mExpandableView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
                if (parent.isGroupExpanded(groupPosition))
                    parent.collapseGroup(groupPosition);
                else
                    parent.expandGroup(groupPosition,true);
                Log.i("TAG", "LOL");
                return true;
            }
        });
        return  view;
    }
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onNewWordAdd(){
        mAdapter.notifyDataSetChanged();
    }

    public static ExpandableListFragment newInstance() {
        return new ExpandableListFragment();
    }

}
