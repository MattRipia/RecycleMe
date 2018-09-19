package ripia.matt.recycleme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

//Imports for ZXing barcode scanner
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    // Variables for logging in, users and databases
    CallbackManager callbackManager;
    GoogleApiClient nGoogleApiClient;
    FirebaseAuth firebaseAuth;
    GoogleSignInAccount account = null;


    // the buttons which need referencing
    private SignInButton googleSignInButton;
    private LoginButton facebookSignInButton;
    private Button guestLogin;

    // primitive variables
    private int GOOGLE_ID = 9001;
    private int FACEBOOK_ID = 64206;
    private String googleReqID = "447863665017-d5ajtcvrs13huldi929i4ij1k01dm5n4.apps.googleusercontent.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setup();
    }

    // this method sets up the database, google sign in, and firebase sdk objects
    private void setup() {

        // creates a new google sign in option type
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(googleReqID)
                .requestEmail()
                .build();

        // creates the google api client, uses to connect to the google servers
        nGoogleApiClient = new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();


        //gets the current instance of the program, used to check if a user has authenticated already
        firebaseAuth = FirebaseAuth.getInstance();
        setupButtons();
    }

    private void setupButtons() {
        // gets the id of the buttons from the xml file and creates them locally so they can be 'pressed'
        googleSignInButton = findViewById(R.id.sign_in_button);
        facebookSignInButton = findViewById(R.id.facebook_login_button);
        guestLogin = findViewById(R.id.guest_login);

        // creates event listeners when a button is pressed
        googleSignInButton.setOnClickListener(this);
        facebookSignInButton.setOnClickListener(this);
        guestLogin.setOnClickListener(this);
    }

    @Override
    //checks if a user is already logged in, if they are, then updates the UI to the camera screen
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    // once a user successfully logs in, this method is called, changes the UI to the main activity
    public void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    // determines which button was pressed and what to do
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                googleLogin();
                break;
            case R.id.facebook_login_button:
                facebookLogin();
                break;
            case R.id.guest_login:
                GuestLogin();
                break;

        }
    }

    private void GuestLogin() {
        try {
            firebaseAuth.signInAnonymously();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            updateUI(currentUser);

        } catch (Exception e) {
            Log.d("guest login", " " + e.getMessage());
        }
    }

    // the user wants to sign in with google, start this activity now
    private void googleLogin() {

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(nGoogleApiClient);
        startActivityForResult(signIntent, GOOGLE_ID);
    }

    // the user wants to sign in with facebook, sets the facebook permissions and starts a new activity
    // onActivityResult is called when onSuccess is hit, meaning a user has logged into facebook successfully
    // the facebook access token is then handled
    private void facebookLogin() {

        callbackManager = CallbackManager.Factory.create();
        facebookSignInButton.setReadPermissions("email", "public_profile", "user_friends");
        facebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                // if a user tries to login
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d("facebook log", "Facebook:On Success" + loginResult);
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(), "You canceled something!", Toast.LENGTH_LONG).show();
                Log.d("facebook log", "Facebook:On Cancel");
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                Log.d("facebook log", "Facebook:On Error" + error.getMessage());
            }
        });
    }

    // on a successful facebook login the facebook token is parsed to the firebase credential manager, which then updates the UI
    // the firebase credential manager now has the account details in which we can access
    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser aUser = firebaseAuth.getCurrentUser();
                    updateUI(aUser);
                } else {
                    Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    // what happens when the google sign in cannot access the google servers
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getApplicationContext(), "Connection to google failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    // after a user logs in with either facebook or google, this method is called.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // the activity that just completed was a google activity
        if (requestCode == GOOGLE_ID) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                handleGoogleAccessToken(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google login failed", Toast.LENGTH_LONG).show();
                Log.d("google sign in log", " error - " + e.getMessage());
            }
        }

        // the activity that just completed was a facebook activity
        else if (requestCode == FACEBOOK_ID) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // when a user signs in with google, the token is then parsed to the firebase authentication variable
    private void handleGoogleAccessToken(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {

                    // If sign in fails, display a message to the user.
                    updateUI(firebaseAuth.getCurrentUser());
                    Toast.makeText(getApplicationContext(), "firebase auth with google - failed!", Toast.LENGTH_LONG).show();
                    Log.d("google firebase auth", "sign in with google on firebase failed");
                }
            }
        });
    }



    private void Zone() {

        // Ali Zihan Rasheed to add his code here
        Toast.makeText(this, "Ali needs to do his job lol", Toast.LENGTH_LONG).show();
    }
}