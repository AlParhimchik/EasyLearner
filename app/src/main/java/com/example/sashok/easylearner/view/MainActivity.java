package com.example.sashok.easylearner.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        addMostFavFoldersToNav();
        setupToolBar();
        setListeners();
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
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        RealmController realmController = RealmController.with(MainActivity.this);
        List<Folder> folders = realmController.getFolders();
        int count = 0;
        if (folders != null) {
            for (Folder folder : folders) {
                Menu m = navView.getMenu();
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
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //implement search here
        if (id == R.id.search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
