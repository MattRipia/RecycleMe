package ripia.matt.recycleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 *  The splash activity it is the first activity when the app runs and is used to
 *  check if a user is already logged in. If a user is already logged in, then we can skip the
 *  login activity and go straight to the MainActivity.
 *
 * */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Splash", "Splash Screen Hit");

        //TODO Make the splash screen show properly
        setContentView(R.layout.activity_splash);

        Thread myThread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        };
        myThread.start();
    }
}





