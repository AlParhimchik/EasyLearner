package com.example.sashok.easylearner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.model.Folder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 25.9.17.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
   // header titles
    // child data in format of header title, child title
    private List<Folder> listFolders;

    public ExpandableListViewAdapter(Context context, List<Folder> folders) {
        this._context=context;
        this.listFolders=folders;
    }

    @Override
    public int getGroupCount() {
        return listFolders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listFolders.get(groupPosition).getWords().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listFolders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listFolders.get(groupPosition).getWords().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = ((Folder)getGroup(groupPosition)).getName();
        ImageView indicator;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.folder_item_expandable_list_view, null);

        }
        indicator=(ImageView)convertView.findViewById(R.id.toggleIcon);
        if (isExpanded) {
            indicator.setImageResource(R.drawable.ic_action_collapse);
        }else{
            indicator.setImageResource(R.drawable.expanse_collapse_icon);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.headingTxt);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String headerTitle = ((Folder)getGroup(groupPosition)).getWords().get(childPosition).getEnWord();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.words_item_expendable_list_view, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.titleTxt);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onCollapseFolder(int folderPos){

    }
    public void onExpanseFolder(int folderPos){

    }
}
