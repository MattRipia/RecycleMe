package ripia.matt.recycleme;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 *  The splash activity it is the first activity when the app runs and is used to
 *  check if a user is already logged in. If a user is already logged in, then we can skip the
 *  login activity and go straight to the MainActivity.
 *
 * */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Splash", "Splash Screen Hit");

        //TODO Make the splash screen show properly
        setContentView(R.layout.activity_splash);

        boolean networkState = checkNetworkActive();

        checkLocalUserExists(networkState);
    }

    private boolean checkNetworkActive() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            connected = true;
        }
        return connected;
    }

    private void checkLocalUserExists(boolean networkState) {
        Intent activityIntent;

        // if the user is not found then go to the login screen
        if (FirebaseAuth.getInstance().getCurrentUser() == null || !networkState)
        {
            activityIntent = new Intent(this, LoginActivity.class);
            Log.d("Splash", "User not found");

            if(!networkState) {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            activityIntent = new Intent(this, MainActivity.class);
            Log.d("Splash", "User Found");
        }

        startActivity(activityIntent);
        finish();
    }

}
