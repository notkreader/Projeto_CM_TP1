package com.example.projeto_cm;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Switch;
import android.widget.TextView;

import com.example.projeto_cm.ui.Login_Register.LoginUser;
import com.example.projeto_cm.ui.Messages.MessagesFragment;
import com.example.projeto_cm.ui.Requests.RequestsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;

    private FirebaseAuth mAuth;
    public static DatabaseReference mDataBase= FirebaseDatabase.getInstance("https://clickandvisit-59882-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        boolean isDarkModeOn;
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            isDarkModeOn = true;
        }else{
            isDarkModeOn = false;
        }
        final boolean dmo = sharedPreferences.getBoolean("isDarkModeOn", isDarkModeOn);


        if (dmo == true) {
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.LightTheme);
        }

      try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();





        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.nav_host_fragment, new RequestsFragment());
            fm.commit();
        });


        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().findItem(R.id.nav_logOut).setOnMenuItemClickListener(menuItem -> {
            mAuth.signOut();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            startActivity(new Intent(MainActivity.this, LoginUser.class));
            return true;
        });


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_categories, R.id.nav_favorites, R.id.nav_wishList, R.id.nav_aboutus, R.id.nav_accountSettings)
                .setDrawerLayout(drawer)
                .build();

        if(fbUser!=null) {
            mDataBase.child("Users").child(fbUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (user.getIsGuide()) {
                        navigationView.getMenu().getItem(2).setVisible(false);
                        navigationView.getMenu().getItem(3).setVisible(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                }
            });
        }


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser fbUser = mAuth.getCurrentUser();
        if(fbUser==null){
            startActivity(new Intent(MainActivity.this, LoginUser.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.app_bar_messages:
                fm.replace(R.id.nav_host_fragment, new MessagesFragment());
                fm.commit();
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}