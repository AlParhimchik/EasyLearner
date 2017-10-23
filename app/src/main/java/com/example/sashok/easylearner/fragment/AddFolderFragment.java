package com.example.sashok.easylearner.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.WordsAdapter;
import com.example.sashok.easylearner.data.DataProvider;
import com.example.sashok.easylearner.helper.DividerItemDecoration;
import com.example.sashok.easylearner.listener.FolderAddedListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 24.9.17.
 */

public class AddFolderFragment extends AbsFragment {
    private RecyclerView recyclerView;
    private WordsAdapter mAdapter;
    private TextView btnSave;
    private EditText folderName;
    private FolderAddedListener folderAddedListener;
    private LinearLayout mainLayout;
    private TextView errorText;
    private List<Word> words;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView choose_word_textview;
    public AddFolderFragment() {

    }

    public static AddFolderFragment newInstance() {
        AddFolderFragment folderFragment = new AddFolderFragment();
        return folderFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;
        if (context instanceof Activity) {
            activity = (Activity) context;
            folderAddedListener = (FolderAddedListener) activity;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View main_view = inflater.inflate(R.layout.add_folder_fragment, container, false);
        initComponents(main_view);
        words = RealmController.with(getActivity()).getWordsWithoutFolder();
        showView();
        setRecyclerVIew();
        setListeners();
        return main_view;
    }

    private void showView() {
        if (words.size() == 0) {
            choose_word_textview.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            errorText.setVisibility(View.VISIBLE);
        } else {
            choose_word_textview.setVisibility(View.VISIBLE);
            errorText.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setRecyclerVIew() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new WordsAdapter(this.getActivity(), words);
        recyclerView.setAdapter(mAdapter);

    }

    private void initComponents(View main_view) {
        mainLayout = (LinearLayout) main_view.findViewById(R.id.add_folder_layout);
        recyclerView = (RecyclerView) main_view.findViewById(R.id.add_folder_recycler);
        btnSave = (TextView) main_view.findViewById(R.id.btn_save_folder);
        folderName = (EditText) main_view.findViewById(R.id.folder_name_edit_text);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        errorText = (TextView) main_view.findViewById(R.id.error_text_no_words);
        errorText.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
        errorText.setText(R.string.Error_no_Words_to_Add);
        choose_word_textview = (TextView) main_view.findViewById(R.id.choose_word_textview);
    }

    private void setListeners() {
        mainLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) hideKeyBoard(v);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Folder folder = new Folder();
                String folder_name = folderName.getText().toString();
                if (!TextUtils.isEmpty(folder_name)) {
                    folder.setName(folder_name);
                    mAdapter.onSaveBtnClicked(folder);
                    folderAddedListener.onFolderAddedListener(AddFolderFragment.this);

                } else Toast.makeText(getActivity(), "Введите имя темы", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void hideKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onDataSetChanged() {
        words.clear();
        words.addAll(RealmController.with(getActivity()).getWordsWithoutFolder());
        showView();
       mAdapter.notifyDataSetChanged();
    }
}
