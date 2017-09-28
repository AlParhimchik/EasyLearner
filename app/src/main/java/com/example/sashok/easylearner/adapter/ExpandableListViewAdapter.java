package com.example.sashok.easylearner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.activity.MainActivity;
import com.example.sashok.easylearner.fragment.AddWordDialogFragment;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.RealmString;
import com.example.sashok.easylearner.model.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sashok on 25.9.17.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Activity _context;
    // header titles
    // child data in format of header title, child title
    private List<Folder> filteredListFolders;
    private List<Folder> folderList;

    public ExpandableListViewAdapter(Activity context, List<Folder> folders) {
        this._context = context;
        this.filteredListFolders = folders;
        folderList = new ArrayList<>();
        folderList.addAll(filteredListFolders);
    }

    @Override
    public int getGroupCount() {
        return filteredListFolders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filteredListFolders.get(groupPosition).getWords().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filteredListFolders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filteredListFolders.get(groupPosition).getWords().get(childPosition);
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
        String headerTitle = ((Folder) getGroup(groupPosition)).getName();
        ImageView indicator;
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.folder_item_expandable_list_view, null);

        }
//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("TAG","lol");
//
//            }
//        });
        indicator = (ImageView) convertView.findViewById(R.id.toggleIcon);
        if (isExpanded) {
            indicator.setImageResource(R.drawable.ic_action_collapse);
        } else {
            indicator.setImageResource(R.drawable.ic_action_expanse);
        }
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.headingTxt);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (filteredListFolders.get(groupPosition).getWords().size() == 0) return null;
        String headerTitle = ((Folder) getGroup(groupPosition)).getWords().get(childPosition).getEnWord();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.words_item_expendable_list_view, null);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWordDialogFragment dialogFragment = new AddWordDialogFragment(_context, (MainActivity) _context, filteredListFolders.get(groupPosition).getWords().get(childPosition));
                dialogFragment.getWindow().getAttributes().windowAnimations = R.style.RegistrationDialogAnimation;
                dialogFragment.setTitle(R.string.change_word);
                dialogFragment.show();
            }
        });
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.titleTxt);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onCollapseFolder(int folderPos) {

    }

    public void onExpanseFolder(int folderPos) {
    }

    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredListFolders.clear();
        if (charText.length() == 0) {
            filteredListFolders.addAll(folderList);
        } else {
            for (Folder folder : folderList) {
                if (folder.getName().contains(charText)) addFolderToFilteredList(folder, null);
                for (Word word : folder.getWords()) {
                    if (word.getEnWord().contains(charText)) {
                        addFolderToFilteredList(folder, null);
                        folder.setWord(word);
                        break;
                    }
                    for (RealmString string : word.getTranslation()) {
                        if (string.string_name.contains(charText)) {
                            addFolderToFilteredList(folder, word);
                            break;
                        }

                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    private void addFolderToFilteredList(Folder folder, Word word) {
        Folder newFolder = null;
        for (Folder fldr : filteredListFolders) {
            if (fldr.getID() == folder.getID()) {
                newFolder = fldr;
                break;
            }
        }
        if (newFolder == null) {
            newFolder = new Folder();
            newFolder.setName(folder.getName());
            newFolder.setID(folder.getID());
            filteredListFolders.add(newFolder);

        }
        if (word != null) {

            for (Word names : newFolder.getWords()) {
                if (word.getID() == names.getID()) {
                    word = null;
                }
            }
        }
        if (word != null) newFolder.getWords().add(word);
    }
}
