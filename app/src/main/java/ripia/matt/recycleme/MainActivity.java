package ripia.matt.recycleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private FrameLayout fragmentContainer;
    private Database database;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentContainer = findViewById(R.id.fragment_container);

        //This code creates and controls the hamburger icon for the menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        firebaseAuth = FirebaseAuth.getInstance();
        database = new Database();

        //Starts the main activity with the home screen in view
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        //Select the home menu option
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ScanFragment()).commit();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment()).commit();
                break;
            case R.id.nav_log_out:
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                logout();
                break;
            case R.id.nav_notifications:
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact_us:
                Toast.makeText(this, "Contact Us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
    private void logout() {

        this.firebaseAuth.signOut();
        database.closeDB();
        Log.d("logout", "logout hit");
        try {
            LoginManager.getInstance().logOut();

        } catch (Exception e) {
            Log.d("logout exception", " " + e);
        }

        startActivity(new Intent(this, SplashActivity.class));
        Log.d("logout", "logout end");

        finish();
    }
}
