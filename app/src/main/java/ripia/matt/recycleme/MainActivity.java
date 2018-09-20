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
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    NavigationView navigationView;
    private Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        globals = globals.getInstance();
        globals.setDatabase(new Database());
        globals.setCurrentItem(new Item());
        globals.setCurrentUser(globals.getDatabase().createCurrentUser(FirebaseAuth.getInstance().getCurrentUser()));

        //This code creates and controls the hamburger icon for the menu
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                        R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //  ** ^---- im not sure why that code doesnt work with the "R.string.navigation_drawer_open, R.string.navigation_drawer_close" parameters
        //  **       so i replaced those with 0's just to get it working... @jaime
        
        //This code creates and controls the hamburger icon for the menu
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
//                0, 0);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Starts the main activity with the home screen in view
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        //Select the home menu option
        navigationView.setCheckedItem(R.id.nav_home);
    }

    /**
     * called when the user selects an item in the drawer menu
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment(), "home_fragment").commit();
                break;
            case R.id.nav_scan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ScanFragment(),"scan_fragment").commit();
                break;
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AccountFragment(),"account_fragment").commit();
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
        //closes the drawer once selection has been processed
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

        FirebaseAuth.getInstance().signOut();
        globals.getDatabase().closeDB();
        //Te ensure that no data from the previous session is passes to the next session.
        globals = null;
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
