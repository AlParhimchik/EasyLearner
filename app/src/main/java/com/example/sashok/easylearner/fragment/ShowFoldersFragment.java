package com.example.sashok.easylearner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.activity.CollectionFolderActivity;
import com.example.sashok.easylearner.adapter.FolderListAdapter;
import com.example.sashok.easylearner.data.AssetFolderDataProvider;
import com.example.sashok.easylearner.data.DataProvider;
import com.example.sashok.easylearner.model.Folder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sashok on 2.10.17.
 */

public class ShowFoldersFragment extends AbsFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvFolders;
    private FolderListAdapter mAdapter;
    private List<Folder> folders;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Thread loadingThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folders = new ArrayList<>();
    }

    public void initViews(View root_view) {
        rvFolders = (RecyclerView) root_view.findViewById(R.id.folder_recycler_view);
        rvFolders.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        rvFolders.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout) root_view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show_folders_layout, container, false);
        initViews(view);
        getFolders();
        return view;
    }


    public void getFolders() {
        swipeRefreshLayout.setRefreshing(true);
        loadingThread  = new Thread(new Runnable() {
            @Override
            public void run() {
                final DataProvider provider = new AssetFolderDataProvider();
                folders = provider.getData();
                if (loadingThread.isInterrupted()) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onFoldersFetches();
                    }
                });
            }
        });
        loadingThread.start();

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getFolders();
    }

    public void onFoldersFetches() {
        swipeRefreshLayout.setRefreshing(false);
        mAdapter = new FolderListAdapter(getActivity(), folders, (CollectionFolderActivity) getActivity());
        rvFolders.setAdapter(mAdapter);

    }

    public static ShowFoldersFragment newInstance(Bundle args) {
        ShowFoldersFragment fragment = new ShowFoldersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loadingThread!=null)
            if (loadingThread.isAlive())
                loadingThread.interrupt();
    }
}
