package com.example.sashok.easylearner.activity;

import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.fragment.AbsFragment;
import com.example.sashok.easylearner.fragment.AddFolderFragment;
import com.example.sashok.easylearner.fragment.AddWordDialogFragment;
import com.example.sashok.easylearner.fragment.ExpandableListFragment;
import com.example.sashok.easylearner.fragment.SearchInNetFragment;
import com.example.sashok.easylearner.fragment.ShowCardWithWordsFragment;
import com.example.sashok.easylearner.listener.FolderAddedListener;
import com.example.sashok.easylearner.listener.WordChangedListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.FragmentTag;
import com.example.sashok.easylearner.realm.RealmController;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements FolderAddedListener, NavigationView.OnNavigationItemSelectedListener, WordChangedListener {
    // tags used to attach the fragments

    public static FragmentTag CURRENT_TAG;
    //used to retrive from savedinstance when oritration changes
//    private static String BUNDLE_NAV_ITEM_INDEX = "navItemIndex";
    private static String BUNDLE_TOOLBAR_TITLE = "toolBarTitle";
    private static String BUNDLE_CURRENT_TAG = "cur_tag";

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
        if (savedInstanceState == null) {
            CURRENT_TAG = FragmentTag.TAG_DEFAULT_FRAGMENT;
            replaceFragment(); // if app startes load home fragment
        }

    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG.getFragmentName());
        fragmentTransaction.commit();
        drawerLayout.closeDrawers();
    }

    public void replaceFragment() {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG.getFragmentName())!= null) {
            drawerLayout.closeDrawers();
             return;
        }
        replaceFragment(getFragment());


    }

    public void addFragment() {

        Fragment fragment = getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
//                android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_layout, fragment, CURRENT_TAG.getFragmentName());
        fragmentTransaction.commit();
        drawerLayout.closeDrawers();
    }

    private Fragment getFragment() {
        switch (CURRENT_TAG) {

            case TAG_DEFAULT_FRAGMENT:
                ShowCardWithWordsFragment defaultFragment = new ShowCardWithWordsFragment();
                toolBarTitle.setText(R.string.app_name);
                return defaultFragment;
            case TAG_SHOW_CARD:

                ShowCardWithWordsFragment cardFragment = new ShowCardWithWordsFragment();
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
                return new ShowCardWithWordsFragment();
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
    }

    public void setListeners() {

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void startFragmentWithFolder(MenuItem item, Folder folder) {
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        item.setIcon(R.drawable.ic_action_open_folder);
        toolBarTitle.setText(folder.getName());
        Fragment fragment = ShowCardWithWordsFragment.newInstance(folder.getID());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG.getFragmentName());
        fragmentTransaction.commit();
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
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (CURRENT_TAG != FragmentTag.TAG_DEFAULT_FRAGMENT) {
//            navItemIndex = 0;
                CURRENT_TAG = FragmentTag.TAG_DEFAULT_FRAGMENT;
                replaceFragment();
                setIconsToDefault();

            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.add_word){
            AddWordDialogFragment dialogFragment = new AddWordDialogFragment(MainActivity.this, MainActivity.this);
            dialogFragment.getWindow().getAttributes().windowAnimations = R.style.RegistrationDialogAnimation;
            dialogFragment.setTitle(R.string.AddWord);
            dialogFragment.show();
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void onFolderAddedListener(Fragment fragment) {
        CURRENT_TAG = FragmentTag.TAG_DEFAULT_FRAGMENT;
        replaceFragment();
        showMostViewsFolders();
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
        for (int i = 1; i < m.size(); i++) {
            MenuItem menuItem = m.getItem(i);
            if (menuItem.isChecked()) menuItem.setChecked(false);
        }
    }

    public Folder getSelectedItem() {
        Menu m = navigationView.getMenu();
        MenuItem item = m.getItem(0);
        MenuItem selected_item = null;
        SubMenu subMenu = item.getSubMenu();
        Folder folder;
        for (int i = 0; i < subMenu.size(); i++) {
            MenuItem menuItem = subMenu.getItem(i);
            if (menuItem.isChecked()) {
                selected_item = menuItem;

            }
        }
        for (int i = 1; i < m.size(); i++) {
            MenuItem menuItem = m.getItem(i);
            if (menuItem.isChecked()) {
                selected_item = menuItem;

            }
        }
        if (selected_item != null) {
            RealmController controller = RealmController.with(this);
            folder = controller.getFolderById(selected_item.getItemId());
            if (folder == null) {
                folder = new Folder();
                folder.setName(selected_item.getTitle().toString());
                folder.setID(selected_item.getItemId());
            }
            return folder;
        } else return null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Folder folder;
        setIconsToDefault();
        switch (item.getItemId()) {
            case R.id.add_folder:
                CURRENT_TAG = FragmentTag.TAG_ADD_FOLDER;
                break;
            case R.id.vocablurary:
                Intent intent=new Intent(this,VocabluraryActivity.class);
                startActivity(intent);
                break;
            case R.id.net_search:
                CURRENT_TAG = FragmentTag.TAG_SEARCH_INTERNER;
                break;
            case R.id.unsorted_item:
                CURRENT_TAG = FragmentTag.TAG_SHOW_CARD;
                Folder unsorted_folder = new Folder();
                unsorted_folder.setName(getResources().getString(R.string.unsorted_item_string));
                unsorted_folder.setID(-1);
                startFragmentWithFolder(item, unsorted_folder);
                return true;
            case R.id.favourite_item:
                CURRENT_TAG = FragmentTag.TAG_SHOW_CARD;
                Folder fav_folder = new Folder();
                fav_folder.setName(getResources().getString(R.string.favourite_item_string));
                fav_folder.setID(-2);
                startFragmentWithFolder(item, fav_folder);
                return true;
            default:
                folder = RealmController.with(MainActivity.this).getFolderById(item.getItemId());
                CURRENT_TAG = FragmentTag.TAG_SHOW_CARD;
                if (folder != null) {
                    startFragmentWithFolder(item, folder);
                    return true;
                }
        }
        item.setChecked(true);

        replaceFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onWordAdded() {
        onWordDeleteFolder();
    }

    @Override
    public void onWordDeleteFolder() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).OnDataSetChanged();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setIconsToDefault();
        MenuItem item = navigationView.getMenu().findItem(savedInstanceState.getInt("item"));
        if (item != null) item.setChecked(true);
        toolBarTitle.setText(savedInstanceState.getString(BUNDLE_TOOLBAR_TITLE));
        CURRENT_TAG = savedInstanceState.getParcelable(BUNDLE_CURRENT_TAG);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(BUNDLE_NAV_ITEM_INDEX, navItemIndex);
        outState.putString(BUNDLE_TOOLBAR_TITLE, toolBarTitle.getText().toString());
        outState.putParcelable(BUNDLE_CURRENT_TAG, CURRENT_TAG);
        Menu menu = navigationView.getMenu();
        MenuItem item;
        int selectedItemId = -1;
        SubMenu subMenu;
        for (int i = 0; i < menu.size(); i++) {
            if (selectedItemId != -1) break;
            item = menu.getItem(i);
            if (item.getSubMenu() != null) {
                subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    if (subMenu.getItem(j).isChecked()) {
                        selectedItemId = subMenu.getItem(j).getItemId();
                    }

                }
            }
            if (item.isChecked()) {
                selectedItemId = item.getItemId();

            }


        }
        outState.putInt("item", selectedItemId);
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
    protected void onRestart() {
        setIconsToDefault();
        super.onRestart();
        onWordDeleteFolder();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm.getDefaultInstance().close();
    }
}