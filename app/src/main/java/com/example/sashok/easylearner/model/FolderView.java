package com.example.sashok.easylearner.model;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.fragment.ListFolderFragment;
import com.example.sashok.easylearner.listener.FolderLongClickLListener;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.expand.Collapse;
import com.mindorks.placeholderview.annotations.expand.Expand;
import com.mindorks.placeholderview.annotations.expand.Parent;
import com.mindorks.placeholderview.annotations.expand.ParentPosition;
import com.mindorks.placeholderview.annotations.expand.SingleTop;
import com.mindorks.placeholderview.annotations.expand.Toggle;

/**
 * Created by sashok on 24.9.17.
 */
@Parent
@SingleTop
@Layout(R.layout.folder_heading)
public class FolderView {
    @View(R.id.headingTxt)
    private TextView headingTxt;


    @View(R.id.toggleIcon)
    private ImageView toggleIcon;

    @Toggle(R.id.toggleView)
    private LinearLayout toggleView;

    @ParentPosition
    private int mParentPosition;

    private Context mContext;
    private Folder folder;
    private  FolderLongClickLListener folderLongClickLListener;
    public FolderView(Context context, Folder folder, FolderLongClickLListener listener) {
        mContext = context;
        this.folder = folder;
        folderLongClickLListener=listener;
    }

    @Click(R.id.folder_view)
    public void Click(){
        Log.i("TEG","GDS");
    }

    @Resolve
    private void onResolved() {

        toggleIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_action_expanse));
        headingTxt.setText(folder.getName());
    }

    @Expand
    private void onExpand(){
        toggleIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_action_collapse));
    }

    @Collapse
    private void onCollapse(){
        toggleIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_action_expanse));
    }
    @LongClick(R.id.toggleView)
    public void onFolderLongClicked(){
        toggleView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.cardview_dark_background));
        folderLongClickLListener.onFolderLongClicked(folder);
    }


}
