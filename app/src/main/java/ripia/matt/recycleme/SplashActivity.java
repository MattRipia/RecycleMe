package ripia.matt.recycleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Splash", "Splash Screen Hit");

        //TODO Make the splash screen show properly
        setContentView(R.layout.activity_splash);

        Intent activityIntent;

        // if the user is not found then go to the login screen
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            activityIntent = new Intent(this, LoginActivity.class);
            Log.d("Splash", "User not found");
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
