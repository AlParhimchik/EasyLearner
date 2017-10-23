package com.example.sashok.easylearner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sashok.easylearner.R;
import com.example.sashok.easylearner.fragment.ShowCardWithWordsFragment;
import com.example.sashok.easylearner.fragment.ShowFoldersFragment;
import com.example.sashok.easylearner.listener.FolderClicklistener;
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.model.FragmentTag;
import com.example.sashok.easylearner.realm.RealmController;

/**
 * Created by sashok on 21.10.17.
 */

public class CollectionFolderActivity extends AbsActivity implements FolderClicklistener,NavigationView.OnNavigationItemSelectedListener {
    private static final String BUNDLE_ITEM_ID = "BUNDLE_ITEM_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_folders_activity);
        initialize();
        replaceFragment(ShowFoldersFragment.newInstance(null));
        setToolBarTitle(R.string.compilations);
        showMostViewsFolders();
        navigationView.setCheckedItem(R.id.compilation);

    }

    @Override
    public void initialize() {
        super.initialize();
        setNavigationItemSelectedListener(this);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frame_layout, fragment, fragment.getTag());
        fragmentTransaction.commit();
    }


    public void addFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        fragmentTransaction.add(R.id.frame_layout, fragment, fragment.getTag());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    @Override
    public void onFolderClicked(Folder folder) {
        Fragment fragment = ShowCardWithWordsFragment.newInstance(folder.getFolderURL());
        addFragment(fragment);
        getSupportActionBar().setTitle(folder.getRusName());
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
        if (getSupportFragmentManager().getBackStackEntryCount()!=0) setToolBarTitle(R.string.compilations);
        super.onBackPressed();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        MenuItem item=navigationView.getMenu().findItem(R.id.compilation);
        if (item!=null) item.setChecked(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vocablurary:

                startActivity(VocabluraryActivity.class);
                break;
            case  R.id.compilation:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case R.id.add_folder:
            case R.id.net_search:
            case R.id.unsorted_item:
            case R.id.favourite_item:

            default:
                startActivity(MainActivity.class,Intent.FLAG_ACTIVITY_CLEAR_TOP,createIntBundle(item.getItemId()));

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startActivity(Class acivity){
        startActivity(acivity,null,null);
    }

    public void startActivity(Class activity,Integer flags,Bundle args){
        Intent intent=new Intent(this,activity);
        if (flags!=null) intent.addFlags(flags);
        if (args!=null)intent.putExtras(args);
        startActivity(intent);
    }
    public Bundle createIntBundle(int value){
        Bundle bundle=new Bundle();
        bundle.putInt(BUNDLE_ITEM_ID,value);
        return bundle;
    }
}