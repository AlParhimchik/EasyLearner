package com.example.sashok.easylearner.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.VocabluraryAdapter;
import com.example.sashok.easylearner.data.DataProvider;
import com.example.sashok.easylearner.data.WordDataProvider;
import com.example.sashok.easylearner.listener.ActionModeListener;
import com.example.sashok.easylearner.model.Word;
import com.example.swipecardlibrary.adapter.WordAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

/**
 * Created by sashok on 20.10.17.
 */

public class VocabluraryActivity extends AppCompatActivity  implements ActionModeListener {
    private DataProvider dataProvider;

    MaterialSearchView searchView;
    private Toolbar toolbar;
    private TextView toolBarTitle;
    private VocabluraryAdapter adapter;
    private int mSortType = 0;
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocablurary_activity);
        initialize();
        setListeners();
    }

    public void setListeners() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });


    }

    public void initialize() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataProvider = new WordDataProvider(this);
        List<Word> words = dataProvider.getData();
        adapter = new VocabluraryAdapter(this, words);
        recyclerView.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolBarTitle.setText(R.string.vocablurary);
        actionModeCallback = new ActionModeCallback();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.word_activity_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        searchView.setMenuItem(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                adapter.onEditItemClicked();
                if (actionMode == null) {
                    actionMode = startSupportActionMode(actionModeCallback);
                }
                break;
            case R.id.sort:
                createDialog();
                break;
        }
        return true;
    }

    public void createDialog() {
        final boolean[] mCheckedItems = {false, true};
        final String[] sortValues = {SortType.SORT_BY_DATE.getName(), SortType.SORT_BY_FOLDER.getName()};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Упорядочить по")
                .setCancelable(true)
                .setSingleChoiceItems(sortValues, mSortType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSortType = which;
                    }
                })
                .setNegativeButton("По возраст.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.onSortByItemClicked(SortType.getByValue(mSortType), false);
                        dialog.dismiss();
                    }
                })                // Добавляем кнопки
                .setPositiveButton("По убыв.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                adapter.onSortByItemClicked(SortType.getByValue(mSortType), true);

                                dialog.dismiss();

                            }
                        }).create();

        builder.show();
    }

    @Override
    public void onItemSelectedCountChanges(int count) {
        if (count == 0) actionMode.setTitle("");
        else
            actionMode.setTitle("Выделено " + String.valueOf(count));
        actionMode.invalidate();

    }

    public interface ToolbarItemListener {
        public void onEditItemClicked();

        public void onDeleteItemClicked();

        public void onSortByItemClicked(SortType type, Boolean orderBy);

        public void onCancelItemClicked();
    }

    public enum SortType {
        SORT_BY_DATE(0, "Время"), SORT_BY_FOLDER(1, "Название папки");
        private final int value;
        private final String name;

        private SortType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static SortType getByValue(int value) {
            return SortType.values()[value];
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.word_activity_action_mode, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    // delete all the selected messages
                    adapter.onDeleteItemClicked();
                    mode.finish();
                    return true;
                case R.id.close:
                    mode.finish();
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            actionMode = null;
            adapter.onCancelItemClicked();
        }


    }
}

