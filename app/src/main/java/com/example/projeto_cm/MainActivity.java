package com.example.projeto_cm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;

import com.example.projeto_cm.ui.AboutUs.AboutUsFragment;
import com.example.projeto_cm.ui.AccountSettings.AccountSettingsFragment;
import com.example.projeto_cm.ui.Categories.CategoriesFragment;
import com.example.projeto_cm.ui.Favorites.FavoritesFragment;
import com.example.projeto_cm.ui.Login_Register.LoginUser;
import com.example.projeto_cm.ui.Messages.MessagesFragment;
import com.example.projeto_cm.ui.Requests.RequestsFragment;
import com.example.projeto_cm.ui.WishList.WishListFragment;
import com.example.projeto_cm.ui.home.HomeFragment;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity{

    private static boolean existVisits;
    private AppBarConfiguration mAppBarConfiguration;

    public static FirebaseAuth mAuth;
    public static DatabaseReference mDataBase= FirebaseDatabase.getInstance("https://clickandvisit-59882-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        /*boolean isDarkModeOn;
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
        }*/

        setTheme(R.style.LightTheme);

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
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
            if(frag!=null) {
                fm.remove(frag);
            }
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

        setMenuButtons(navigationView, drawer);

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

    public static boolean isUpdated(){
        MainActivity.mDataBase.child("Visits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists() ){
                    existVisits=true;
                }else{

                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });
        return existVisits;
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

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_accountSettings).setTitle(mAuth.getCurrentUser().getDisplayName());

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.app_bar_messages:
                fm.replace(R.id.nav_host_fragment, new MessagesFragment());
                Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
                if(frag != null) {
                    fm.remove(frag);
                }
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

    public void setMenuButtons(NavigationView navigationView, DrawerLayout drawer){
        navigationView.getMenu().findItem(R.id.nav_categories).setOnMenuItemClickListener(item -> {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.nav_host_fragment, new CategoriesFragment());
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
            if(frag!=null) {
                fm.hide(frag);
            }
            fm.commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_aboutus).setOnMenuItemClickListener(item -> {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.nav_host_fragment, new AboutUsFragment());
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
            if(frag!=null) {
                fm.hide(frag);
            }
            fm.commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_accountSettings).setOnMenuItemClickListener(item -> {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.nav_host_fragment, new AccountSettingsFragment());
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
            if(frag!=null) {
                fm.hide(frag);
            }
            fm.commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_favorites).setOnMenuItemClickListener(item -> {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.nav_host_fragment, new FavoritesFragment());
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
            if(frag!=null) {
                fm.hide(frag);
            }
            fm.commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        navigationView.getMenu().findItem(R.id.nav_wishList).setOnMenuItemClickListener(item -> {
            FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
            fm.replace(R.id.nav_host_fragment, new WishListFragment());
            Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
            if(frag!=null) {
                fm.hide(frag);
            }
            fm.commit();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    public void setVisitsOff(){
        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.nav_host_fragment, new HomeFragment());
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.mid_frag);
        if(frag!=null) {
            fm.hide(frag);
        }
        fm.commit();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(this, MainActivity.class);
        startActivity(startMain);
        finish();
    }
}