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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 *  The main activity is the underlying code for where the other fragments sit, it sets the
 *  side validation bar which is used to navigate through the other fragments.
 *
 * */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    NavigationView navigationView;
    private static final int SCAN_ID = 49374;
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

        globals = Globals.getInstance();
        globals.setDatabase(new Database());
        globals.setCurrentItem(new Item());
        globals.setCurrentUser(globals.getDatabase().createCurrentUser(FirebaseAuth.getInstance().getCurrentUser()));

        //This code creates and controls the hamburger icon for the menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

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
                Toast.makeText(this, "Notifications - Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share - Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact_us:
                Toast.makeText(this, "Contact Us - Coming Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_about:
                Toast.makeText(this, "About Us - Coming Soon", Toast.LENGTH_SHORT).show();
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

    @Override
    // after a user logs in with either facebook or google, this method is called.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // the activity that just completed was a scanner activity
        if (requestCode == SCAN_ID)
        {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            //successful scan, create a new item object to hold the barcode, then query the db to see if this exists
            // if an item exists, the recycling number is taken, if not then the user is asked to input this
            if (scanningResult.getContents() != null)
            {
                globals.getCurrentItem().setBarcode(scanningResult.getContents());
                globals.getCurrentUser().setPoints(globals.getCurrentUser().getPoints() + 10);
                Boolean itemExists = globals.getDatabase().checkItemInDatabase(globals.getCurrentItem());

                // if an item exists in the database -> grab that item now then update the currentItem with the recycling number
                if(itemExists)
                {
                    // the item exists in the database, update the UI now
                    // Re initializing the scan fragment to update the result field
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScanFragment(),"scan_fragment").commit();
                    Toast.makeText(this, "Scan success", Toast.LENGTH_SHORT).show();
                }
                // if an item doesnt exist in the database, ask the user for a recycling number by opening the ItemFormFragment
                else
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ItemFormFragment(),"get_recycling_number_fragment").commit();
                }
            }
            else
            {
                Toast.makeText(this, "No scan data received!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // logs the user out of firebase, google and facebook, saves their data in the database and moves into the login screen again
    private void logout() {

        try {
            globals.getDatabase().updateDatabase();
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

            super.finish();

        }catch(Exception e)
        {
            Log.d("logout exception" , " error - "+ e.getMessage());
        }
    }

    // syncs the current user to the backend database when the user clicks the back button
    @Override
    public void finish()
    {
        try{
            globals.getDatabase().updateDatabase();

        } catch (Exception e)
        {
            Log.d("finish exception" , " error - " + e.getMessage());
        }

        super.finish();
    }
}