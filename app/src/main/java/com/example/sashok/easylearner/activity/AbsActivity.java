package com.example.sashok.easylearner.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
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
import com.example.sashok.easylearner.model.Folder;
import com.example.sashok.easylearner.realm.RealmController;

import java.util.List;

/**
 * Created by sashok on 22.10.17.
 */

public abstract class AbsActivity extends AppCompatActivity {
    private static final String MENU_ITEM_BUNDLE = "MENU_ITEM_BUNDLE";
    private static final String BUNDLE_TOOLBAR_TITLE = "BUNDLE_TOOLBAR_TITLE";
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    protected NavigationView navigationView;
    private TextView toolBarTitle;
    protected Toolbar toolbar;
    protected MenuItem selectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setToolBarTitle(String title) {
        toolBarTitle.setText(title);
    }

    public void setToolBarTitle(@IdRes int title) {
        toolBarTitle.setText(title);
    }


    public String getToolbarTitle() {
        return toolBarTitle.getText().toString();
    }

    public void showMostViewsFolders() {
        RealmController realmController = RealmController.with(AbsActivity.this);
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

    public void initialize() {

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolBarTitle = (TextView) findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener listener) {
        navigationView.setNavigationItemSelectedListener(listener);
    }

    public void setectMenuItem(MenuItem item) {
        this.selectedItem = item;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedItem != null) outState.putInt(MENU_ITEM_BUNDLE, selectedItem.getItemId());
        outState.putString(BUNDLE_TOOLBAR_TITLE, getToolbarTitle());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedItem = navigationView.getMenu().findItem(savedInstanceState.getInt(MENU_ITEM_BUNDLE));
        if (selectedItem != null){
            selectedItem.setChecked(true);
            navigationView.setCheckedItem(selectedItem.getItemId());
            if (selectedItem.getGroupId()==0) selectedItem.setIcon(R.drawable.ic_action_open_folder);
        }
        setToolBarTitle(savedInstanceState.getString(BUNDLE_TOOLBAR_TITLE));
    }

    public MenuItem getSelectMenuItem() {
        return selectedItem;
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
}
