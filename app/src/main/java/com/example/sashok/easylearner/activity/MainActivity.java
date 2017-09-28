package com.example.sashok.easylearner.activity;


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
import com.example.sashok.easylearner.helper.FragmentTagsController;
import com.example.sashok.easylearner.listener.FolderAddedListener;
import com.example.sashok.easylearner.listener.WordChangedListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.FragmentTags;
import com.example.sashok.easylearner.realm.RealmController;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FolderAddedListener, NavigationView.OnNavigationItemSelectedListener, WordChangedListener {
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
    MaterialSearchView searchView;
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
            CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
            replaceFragment(); // if app startes load home fragment
        }
        toggleFab();

    }

    public void replaceFragment() {
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(FragmentTagsController.toString(CURRENT_TAG)) != null) {
            drawerLayout.closeDrawers();
            toggleFab();
            return;
        }
        Fragment fragment = getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment, FragmentTagsController.toString(CURRENT_TAG));
        fragmentTransaction.commit();
        toggleFab();
        drawerLayout.closeDrawers();

    }

    public void addFragment() {
        if (getSupportFragmentManager().findFragmentByTag(FragmentTagsController.toString(CURRENT_TAG)) != null) {
            drawerLayout.closeDrawers();
            toggleFab();
            return;
        }
        Fragment fragment = getFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
//                android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_layout, fragment, FragmentTagsController.toString(CURRENT_TAG));
        //fragmentTransaction.addToBackStack(CURRENT_TAG.name());
        fragmentTransaction.commit();
        toggleFab();
        drawerLayout.closeDrawers();
    }

    private Fragment getFragment() {
        switch (CURRENT_TAG) {
            case TAG_LIST_FOLDER:
                ExpandableListFragment listFolderFragment = new ExpandableListFragment();
//                toolBarTitle.setText(R.string.app_name);
                return listFolderFragment;
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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
    }

    public void setListeners() {

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddWordDialogFragment dialogFragment = new AddWordDialogFragment(MainActivity.this, MainActivity.this);
                dialogFragment.getWindow().getAttributes().windowAnimations = R.style.RegistrationDialogAnimation;
                dialogFragment.setTitle(R.string.AddWord);
                dialogFragment.show();
            }
        });
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (getFragment() instanceof ExpandableListFragment) {
                    ExpandableListFragment fragment = (ExpandableListFragment) getFragment();
                    fragment.filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(FragmentTagsController.toString(CURRENT_TAG));

                if (fragment instanceof ExpandableListFragment) {
                    ExpandableListFragment fragment1 = (ExpandableListFragment) fragment;
                    fragment1.filter(newText);
                }
                return true;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                setDrawerState(false);
                CURRENT_TAG = FragmentTags.TAG_LIST_FOLDER;
                addFragment();
            }

            @Override
            public void onSearchViewClosed() {
                setDrawerState(true);
                if (CURRENT_TAG == FragmentTags.TAG_LIST_FOLDER)
                    onBackPressed();
            }
        });
    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_UNLOCKED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.syncState();

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.onDrawerStateChanged(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            actionBarDrawerToggle.syncState();
        }
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
        fragmentTransaction.replace(R.id.frame_layout, fragment, FragmentTagsController.toString(CURRENT_TAG));
        fragmentTransaction.commit();
        toggleFab();
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
            if (CURRENT_TAG == FragmentTags.TAG_LIST_FOLDER) {
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                Fragment cur_fragment = fragments.get(0);
                Fragment last_fragment = fragments.get(1);
                for (Fragment fragment : fragments) {
                    if (FragmentTagsController.fromString(fragment.getTag()) == FragmentTags.TAG_LIST_FOLDER)
                        cur_fragment = fragment;
                    else {
                        last_fragment = fragment;
                    }
                }
                getSupportFragmentManager().beginTransaction().remove(cur_fragment).commit();
                FragmentTags last_tag = FragmentTagsController.fromString(last_fragment.getTag());
                if (last_tag != null) CURRENT_TAG = last_tag;
                else
                    CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                if (searchView.isSearchOpen()) searchView.closeSearch();
                toggleFab();
                return;
            }
            setIconsToDefault();
            if (CURRENT_TAG != FragmentTags.TAG_SHOW_CARD) {
//            navItemIndex = 0;
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                replaceFragment();
                return;
            } else {
                super.onBackPressed();
            }
        }

    }

    public void toggleFab() {
        if (CURRENT_TAG == FragmentTags.TAG_SHOW_CARD) {
            fab.show();
        } else {
            fab.hide();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        searchView.setMenuItem(menuItem);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //implement search here
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    public void onFolderAddedListener(Fragment fragment) {
        //navItemIndex = 0;
        CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
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
        Folder folder = null;
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
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.add_folder:
                CURRENT_TAG = FragmentTags.TAG_ADD_FOLDER;
                break;
            case R.id.net_search:
                CURRENT_TAG = FragmentTags.TAG_SEARCH_INTERNER;
                break;
            case R.id.unsorted_item:
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                Folder unsorted_folder = new Folder();
                unsorted_folder.setName(getResources().getString(R.string.unsorted_item_string));
                unsorted_folder.setID(-1);
                startFragmentWithFolder(item, unsorted_folder);
                return true;
            case R.id.favourite_item:
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
                Folder fav_folder = new Folder();
                fav_folder.setName(getResources().getString(R.string.favourite_item_string));
                fav_folder.setID(-2);
                startFragmentWithFolder(item, fav_folder);
                return true;
            default:
                folder = RealmController.with(MainActivity.this).getFolderById(item.getItemId());
                CURRENT_TAG = FragmentTags.TAG_SHOW_CARD;
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
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).OnDataSetChanged();
            }
        }
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
            ;
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

}
