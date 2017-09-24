package com.example.sashok.easylearner.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.fragment.AddFolderFragment;
import com.example.sashok.easylearner.fragment.SearchInNetFragment;
import com.example.sashok.easylearner.fragment.ShowCardWithWords;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.RealmString;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    // tags used to attach the fragments
    private static final String TAG_SHOW_CARD = "show_cards";
    private static final String TAG_ADD_FOLDER = "addFolder";
    private static final String TAG_SEARCH_INTERNER = "searchInternet";
    public static String CURRENT_TAG = TAG_SHOW_CARD;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        addMostFavFoldersToNav();
        setupToolBar();
        setListeners();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_SHOW_CARD;
            loadFragment();
        }
    }

    public void loadFragment() {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();
            toggleFab();
            return;
        }
        Fragment fragment = getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, fragment,CURRENT_TAG);
        fragmentTransaction.commit();
        toggleFab();
        drawerLayout.closeDrawers();

    }

    private Fragment getFragment() {
        switch (navItemIndex) {
            case 0:

                ShowCardWithWords cardFragment = new ShowCardWithWords();
                return cardFragment;
            case 1:

                AddFolderFragment addFolderFragment = new AddFolderFragment();
                return addFolderFragment;
            case 2:
                SearchInNetFragment searchFragment = new SearchInNetFragment();
                return searchFragment;
            default:
                return new ShowCardWithWords();
        }

        }


    public void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    public void setListeners() {

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.add_folder:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ADD_FOLDER;
                        break;
                    case R.id.net_search:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ADD_FOLDER;
                        break;


                    default:
                        navItemIndex = 0;
                }
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

//    public void setFragment(int item_id) {
//        Fragment fragment;
//        switch (item_id) {
//            case R.id.add_folder:
//                fragment = new AddFolderFragment();
//                break;
//            case R.id.net_search:
//                fragment = new SearchInNetFragment();
//                break;
//        }
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                android.R.anim.fade_out);
//        fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
//    }

    public void setupToolBar() {

        setSupportActionBar(toolbar);
    }

    public void show_words(View view) {
        ArrayList<Word> words = RealmController.getInstance().getWords();
        Log.d("TAG", words.get(0).getEnWord());
    }

    public void showAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.add_word, null);
        final EditText en_name = (EditText) content.findViewById(R.id.title);
        final EditText rus_name = (EditText) content.findViewById(R.id.author);
        final EditText trans = (EditText) content.findViewById(R.id.thumbnail);

        builder.setView(content)
                .setTitle("Edit Book")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Word word = new Word();
                        word.setEnWord(en_name.getText().toString());
                        word.setFavourite(false);
                        RealmList<RealmString> strings = new RealmList<>();
                        RealmString string = new RealmString();
                        string.string_name = rus_name.getText().toString();
                        strings.add(string);
                        word.setTranslation(strings);
                        RealmController controller = RealmController.with(MainActivity.this);
                        controller.addWord(word);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addMostFavFoldersToNav() {
        RealmController realmController = RealmController.with(MainActivity.this);
        List<Folder> folders = realmController.getFolders();
        int count = 0;
        if (folders != null) {
            for (Folder folder : folders) {
                Menu m = navigationView.getMenu();
                MenuItem item = m.getItem(0);
                SubMenu subMenu = item.getSubMenu();
                subMenu.add(folder.getName()).setIcon(R.drawable.ic_action_black_folder);
                if (count > 5) break;
                count++;
            }
        }

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Синхронизировать состояние переключения после того, как
        // возникнет onRestoreInstanceState
        // actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Передать любые изменения конфигурации переключателям
        // drawer'а
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (navItemIndex != 0) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_SHOW_CARD;
            loadFragment();
            return;
        } else {
            super.onBackPressed();
        }
    }

    public void toggleFab() {
        if (navItemIndex == 0) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //implement search here
        if (id == R.id.search) {

            return true;
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
