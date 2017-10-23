package com.example.sashok.easylearner.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.example.sashok.easylearner.fragment.SearchInNetFragment;
import com.example.sashok.easylearner.fragment.ShowCardWithWordsFragment;
import com.example.sashok.easylearner.listener.DataSetChangeListener;
import com.example.sashok.easylearner.listener.FolderAddedListener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.FragmentTag;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

import io.realm.Realm;

public class MainActivity extends AbsActivity implements FolderAddedListener, NavigationView.OnNavigationItemSelectedListener, DataSetChangeListener {
    // tags used to attach the fragments

    public static FragmentTag CURRENT_TAG;
    //used to retrive from savedinstance when oritration changes
//    private static String BUNDLE_NAV_ITEM_INDEX = "navItemIndex";
    private static String BUNDLE_ITEM_ID = "BUNDLE_ITEM_ID";
    private static String BUNDLE_CURRENT_TAG = "cur_tag";
    //used when user addes new folder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        showMostViewsFolders();
        setListeners();
        if (getIntent().getExtras()!=null){
            int item_id=getIntent().getExtras().getInt("BUNDLE_ITEM_ID",-1);
            if (item_id!=-1) {
                selectedItem=navigationView.getMenu().findItem(item_id);
                onNavigationItemSelected(selectedItem);
            }
        }
        else
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
        //drawerLayout.closeDrawers();
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

    private Fragment getFragment() {
        switch (CURRENT_TAG) {

            case TAG_DEFAULT_FRAGMENT:
                ShowCardWithWordsFragment defaultFragment = new ShowCardWithWordsFragment();
                setToolBarTitle(R.string.default_page);
                return defaultFragment;
            case TAG_SHOW_CARD:

                ShowCardWithWordsFragment cardFragment = new ShowCardWithWordsFragment();
                setToolBarTitle(R.string.app_name);
                return cardFragment;
            case TAG_ADD_FOLDER:

                AddFolderFragment addFolderFragment = new AddFolderFragment();
                setToolBarTitle(R.string.AddFolder);
                return addFolderFragment;
            case TAG_SEARCH_INTERNER:
                SearchInNetFragment searchFragment = new SearchInNetFragment();
                setToolBarTitle(R.string.search_in_net);
                return searchFragment;
            default:
                return new ShowCardWithWordsFragment();
        }

    }

    public void setListeners() {
        setNavigationItemSelectedListener(this);
    }

    public void startFragmentWithFolder(MenuItem item, Folder folder) {
        drawerLayout.closeDrawer(GravityCompat.START);
        item.setChecked(true);
        selectedItem=item;
        item.setIcon(R.drawable.ic_action_open_folder);
        setToolBarTitle(folder.getName());
        Fragment fragment = ShowCardWithWordsFragment.newInstance(folder.getID());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG.getFragmentName());
        fragmentTransaction.commit();
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
                startActivity(new Intent(this,VocabluraryActivity.class));
                break;
            case  R.id.compilation:
                startActivity(new Intent(this,CollectionFolderActivity.class));
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
        if (item.getItemId()!=R.id.compilation&& item.getItemId()!=R.id.vocablurary)
        selectedItem=item;
        replaceFragment();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CURRENT_TAG = savedInstanceState.getParcelable(BUNDLE_CURRENT_TAG);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_CURRENT_TAG, CURRENT_TAG);
    }

    @Override
    protected void onRestart() {
        setIconsToDefault();
        if (selectedItem!=null) {
            navigationView.setCheckedItem(selectedItem.getItemId());
            if (selectedItem.isChecked()==false){
                selectedItem.setChecked(true);

            }
            if (selectedItem.getGroupId()==0) selectedItem.setIcon(R.drawable.ic_action_open_folder);
        }
         super.onRestart();
        onDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm.getDefaultInstance().close();
    }

    @Override
    public void onDataSetChanged() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof AbsFragment) {
                ((AbsFragment) fragment).onDataSetChanged();
            }
        }
    }
}