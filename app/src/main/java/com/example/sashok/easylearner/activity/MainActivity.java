package com.example.sashok.easylearner.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.adapter.ExpandableListViewAdapter;
import com.example.sashok.easylearner.fragment.AddFolderFragment;
import com.example.sashok.easylearner.fragment.ExpandableListFragment;
import com.example.sashok.easylearner.fragment.SearchInNetFragment;
import com.example.sashok.easylearner.fragment.ShowCardWithWords;
import com.example.sashok.easylearner.listener.FolderAddedListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.FragmentTags;
import com.example.sashok.easylearner.model.FragmentTagsController;
import com.example.sashok.easylearner.model.RealmString;
import com.example.sashok.easylearner.model.Word;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

import io.realm.RealmList;

public class MainActivity extends AppCompatActivity implements FolderAddedListener,NavigationView.OnNavigationItemSelectedListener {
    // tags used to attach the fragments

    public static FragmentTags CURRENT_TAG;

    //used to retrive from savedinstance when oritration changes
//    private static String BUNDLE_NAV_ITEM_INDEX = "navItemIndex";
    private static String BUNDLE_TOOLBAR_TITLE = "toolBarTitle";
    private static String BUNDLE_CURRENT_TAG = "cur_tag";

//    // index to identify current nav menu item
//    public static int navItemIndex = 0;

    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView toolBarTitle;
    //used when user addes new folder
    private FolderAddedListener folderAddedListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        showMostViewsFolders();
        setSupportActionBar(toolbar);
        setListeners();
        if (savedInstanceState==null ) {
            CURRENT_TAG = FragmentTags.TAG_LIST_FOLDER;
            loadFragment(); // if app startes load home fragment
        }
        toggleFab();

    }

    public void loadFragment() {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(FragmentTagsController.toString(CURRENT_TAG)) != null) {
            drawerLayout.closeDrawers();
            toggleFab();
            return;
        }
        Fragment fragment = getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, fragment, FragmentTagsController.toString(CURRENT_TAG));
        fragmentTransaction.commit();
        toggleFab();
        drawerLayout.closeDrawers();

    }

    private Fragment getFragment() {
        switch (CURRENT_TAG) {
            case TAG_LIST_FOLDER:
                ExpandableListFragment listFolderFragment = new ExpandableListFragment();
                toolBarTitle.setText(R.string.app_name);
                return listFolderFragment;
            case TAG_SHOW_CARD:

                ShowCardWithWords cardFragment = new ShowCardWithWords();
                toolBarTitle.setText(R.string.app_name);
                return cardFragment;
            case TAG_ADD_FOLDER:

                AddFolderFragment addFolderFragment = new AddFolderFragment();
                toolBarTitle.setText(R.string.AddFolder);
                return addFolderFragment;
            case TAG_SEARCH_INTERNER:
                SearchInNetFragment searchFragment = new SearchInNetFragment();
                toolBarTitle.setText(R.string.search_in_net);
                return searchFragment;
            default:
                return new ShowCardWithWords();
        }

    }

    public void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
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
        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showADdWordDialog();
            }
        });
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i("TAg","LOL");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setIconsToDefault();
        MenuItem item=navigationView.getMenu().findItem(savedInstanceState.getInt("item"));
        if (item!=null) item.setChecked(true);
        toolBarTitle.setText(savedInstanceState.getString(BUNDLE_TOOLBAR_TITLE));
        CURRENT_TAG = savedInstanceState.getParcelable(BUNDLE_CURRENT_TAG);
    }

    public void startFragmentWithFolder(MenuItem item, Folder folder) {
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        item.setIcon(R.drawable.ic_action_open_folder);
        toolBarTitle.setText(folder.getName());
        Fragment fragment = ShowCardWithWords.newInstance(folder.getID());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, fragment, FragmentTagsController.toString(CURRENT_TAG));
        fragmentTransaction.commit();
        toggleFab();
    }

    public void showADdWordDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.add_word_dialog, null);
        final EditText en_name = (EditText) content.findViewById(R.id.word_in_engl);
        final EditText rus_name = (EditText) content.findViewById(R.id.word_in_rus);
        final EditText trans = (EditText) content.findViewById(R.id.transcription);

        builder.setView(content)
                .setTitle(R.string.AddWord)
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
                        onNewWordAdd();
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

    private void onNewWordAdd() {
        if (CURRENT_TAG==FragmentTags.TAG_LIST_FOLDER){
            Fragment curFragment= getSupportFragmentManager().findFragmentByTag(FragmentTagsController.toString(CURRENT_TAG));
            if (curFragment  instanceof ExpandableListFragment){
                ((ExpandableListFragment) curFragment).onNewWordAdd();
            }
        }
    }

    public void showMostViewsFolders() {
        RealmController realmController = RealmController.with(MainActivity.this);
        List<Folder> folders = realmController.getFolders();
        Menu m = navigationView.getMenu();
        MenuItem item = m.getItem(0);
        SubMenu subMenu = item.getSubMenu();
        subMenu.clear();
        int count = 0;
        if (folders != null) {
            for (Folder folder : folders) {
                if (count > 3) break;
                subMenu.add(0, folder.getID(), Menu.NONE, folder.getName()).setIcon(R.drawable.ic_action_black_folder);
                count++;
            }
        }
        subMenu.add(0, R.id.unsorted_item, Menu.NONE, R.string.unsorted_item_string).setIcon(R.drawable.ic_action_black_folder);
        subMenu.add(0, R.id.favourite_item, Menu.NONE, R.string.favourite_item_string).setIcon(R.drawable.ic_action_black_folder);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//         Синхронизировать состояние переключения после того, как
//         возникнет onRestoreInstanceState
        actionBarDrawerToggle.syncState();
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
        else {
            setIconsToDefault();
            if (CURRENT_TAG != FragmentTags.TAG_LIST_FOLDER) {
//            navItemIndex = 0;
                CURRENT_TAG = FragmentTags.TAG_LIST_FOLDER;
                loadFragment();
                return;
            } else {
                super.onBackPressed();
            }
        }

    }

    public void toggleFab() {
        if (CURRENT_TAG == FragmentTags.TAG_LIST_FOLDER) {
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

    @Override
    public void onFolderAddedListener(Fragment fragment) {
        //navItemIndex = 0;
        CURRENT_TAG = FragmentTags.TAG_LIST_FOLDER;
        toggleFab();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_layout, getFragment(), FragmentTagsController.toString(CURRENT_TAG));
        fragmentTransaction.commit();
        showMostViewsFolders();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(BUNDLE_NAV_ITEM_INDEX, navItemIndex);
        outState.putString(BUNDLE_TOOLBAR_TITLE, toolBarTitle.getText().toString());
        outState.putParcelable(BUNDLE_CURRENT_TAG, CURRENT_TAG);
        Menu menu=navigationView.getMenu();
        MenuItem item;
        int selectedItemId=-1;
        SubMenu subMenu;
        for (int i=0;i<menu.size();i++){
        if (selectedItemId!=-1 ) break;;
            item=menu.getItem(i);
            if (item.getSubMenu()!=null){
                subMenu=item.getSubMenu();
                for (int j=0;j<subMenu.size();j++){
                    if (subMenu.getItem(j).isChecked()){
                        selectedItemId=subMenu.getItem(j).getItemId();
                    }

                }
            }
            if (item.isChecked()){
                selectedItemId=item.getItemId();

            }


        }
        outState.putInt("item",selectedItemId);
    }

    public void setIconsToDefault() {
        Menu m = navigationView.getMenu();
        MenuItem item = m.getItem(0);
        SubMenu subMenu = item.getSubMenu();
        for (int i = 0; i < subMenu.size(); i++) {
            MenuItem menuItem = subMenu.getItem(i);
            if (menuItem.isChecked()) {
                menuItem.setChecked(false);
                menuItem.setIcon(R.drawable.ic_action_black_folder);
            }
        }
        for (int i=1;i<m.size();i++){
            MenuItem menuItem=m.getItem(i);
            if (menuItem.isChecked()) menuItem.setChecked(false);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Folder folder;
        setIconsToDefault();
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.add_folder:
//                        navItemIndex = 2;
                CURRENT_TAG = FragmentTags.TAG_ADD_FOLDER;
                break;
            case R.id.net_search:
//                        navItemIndex = 3;
                CURRENT_TAG = FragmentTags.TAG_SEARCH_INTERNER;
                break;
            case R.id.unsorted_item:
//                        navItemIndex = 1;
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                Folder unsorted_folder = new Folder();
                unsorted_folder.setName(getResources().getString(R.string.unsorted_item_string));
                unsorted_folder.setID(-1);
                startFragmentWithFolder(item, unsorted_folder);
                break;
            case R.id.favourite_item:
//                        navItemIndex = 1;
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                Folder fav_folder = new Folder();
                fav_folder.setName(getResources().getString(R.string.favourite_item_string));
                fav_folder.setID(-2);
                startFragmentWithFolder(item, fav_folder);
                break;
            default:
                folder = RealmController.with(MainActivity.this).getFolderById(item.getItemId());
                //navItemIndex = 1;
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                if (folder != null) {
                    startFragmentWithFolder(item, folder);
                    return true;
                }
        }
        item.setChecked(true);

        loadFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
