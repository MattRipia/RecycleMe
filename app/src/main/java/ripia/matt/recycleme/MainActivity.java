package ripia.matt.recycleme;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    // facebook / google / firebase API's
    CallbackManager callbackManager;
    GoogleApiClient nGoogleApiClient;
    FirebaseAuth firebaseAuth;
    GoogleSignInAccount account = null;

    // the buttons which need referencing
    private SignInButton googleSignInButton;
    private LoginButton  facebookSignInButton;
    private Button logoutButton;


    // variables
    private int RC_SIGN_IN = 9001;
    private String TAG = "FACELOG";
    private String googleReqID = "447863665017-d5ajtcvrs13huldi929i4ij1k01dm5n4.apps.googleusercontent.com";
    private String connectionString = "jdbc:jtds:sqlserver://mattripia.database.windows.net:1433/RecycleMe;user=mattripia@mattripia;password=Hello1234;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private String driverName = "net.sourceforge.jtds.jdbc.Driver";
    public  Connection conn = null;
    public  Statement statement = null;
    public  User currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
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
        nGoogleApiClient =
                new GoogleApiClient.Builder(this)
                        .enableAutoManage(this, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();


        //gets the current instance of the program, used to check if a user has authenticated already
        firebaseAuth = FirebaseAuth.getInstance();
        setupButtonsMain();
    }

    private void setupButtonsMain() {
        // gets the id of the buttons from the xml file and creates them locally so they can be 'pressed'
        googleSignInButton = findViewById(R.id.sign_in_button);
        facebookSignInButton = findViewById(R.id.facebook_login_button);

        // creates event listeners when a button is pressed
        googleSignInButton.setOnClickListener(this);
        facebookSignInButton.setOnClickListener(this);
    }

    private void setupButtonsLoggedIn() {
        // gets the id of the buttons from the xml file and creates them locally so they can be 'pressed'
        logoutButton = findViewById(R.id.logout_button);

        // creates event listeners when a button is pressed
        logoutButton.setOnClickListener(this);
    }

    @Override
    //checks if a user is already logged in, if they are, then updates the UI to the camera screen
    protected void onStart()
    {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser != null)
        {
            updateUI(currentUser);
        }
    }

    @Override
    // when either the facebook or google sign in buttons are clicked
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.sign_in_button:
                googleLogin();
                break;
            case R.id.facebook_login_button:
                facebookLogin();
                break;
            case R.id.logout_button:
                logout();
                break;
        }
    }

    // initialises the db object where we will communicate with our database
    private void initializeDb() {

        // This prevents the app from crashing as internet i/o shouldn't be completed on the main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            // loading the driver jtds
            Class.forName(driverName);

        } catch (Exception e) {

            Log.d("debug cnf exception- ", " " + e);
        }

        try {
            // establishing a connection to the db and running the test statement
            conn = DriverManager.getConnection(connectionString);

        } catch (Exception e) {

           Log.d("debug sql exception", " " + e);
        }
    }

    public void closeDB() {
        try {
            conn.close();
            statement.close();
        } catch (SQLException e) {
            Log.d("debug sql close ex - ", " " + e);
        }
    }

    private void logout() {

        this.firebaseAuth.signOut();
        closeDB();

        try
        {
            LoginManager.getInstance().logOut();
        }
        catch(Exception e) {
            Log.d("logout exception", " " + e);
        }

        updateUI(null);
    }

    // once a user successfully logs in, this method is called, changes the UI to the camera activity
    public void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser == null)
        {
            setContentView(R.layout.activity_main);
            setupButtonsMain();
        }
        else
        {
            //Toast.makeText(getApplicationContext(),"UpdateUi Hit", Toast.LENGTH_LONG).show();
            initializeDb();
            setupUser(firebaseUser);
            setContentView(R.layout.activity_account);
            setupButtonsLoggedIn();
        }
    }

    // this method checks the database to see a users details.
    // if there is no user in the database, a new user is created with a unique id used to identify them
    // if a user is already in the database, load the details into the 'currentUser' object
    private void setupUser(FirebaseUser firebaseUser) {

        currentUser = new User();
        ResultSet rs;
        currentUser.setName(firebaseUser.getDisplayName());
        currentUser.setUniqueID(firebaseUser.getUid());

        String queryUser = "select * from account where uniqueid = '" + currentUser.getUniqueID() + "'";

        try {
            statement = conn.createStatement();
            rs = statement.executeQuery(queryUser);

            // a user already exists in the database, pull their data now!
            if(rs.next())
            {
                // indexes for sql start at 1 -- gets all the user data from the account table and puts it into a user object
                currentUser.setUniqueID(rs.getString(1));
                currentUser.setName(rs.getString(2));
                currentUser.setAddress(rs.getString(3));
                currentUser.setPoints(rs.getInt(4));
                Toast.makeText(this, "got the user details from database!", Toast.LENGTH_LONG).show();
            }
            // a user doesn't exist in the database, create a new record now!
            else
            {
                String insertUser = "insert into account values('"
                                  + currentUser.getUniqueID() + "','"
                                  + currentUser.getName()  + "','"
                                  + currentUser.getAddress() + "',"
                                  + currentUser.getPoints() + ")";
                statement.executeUpdate(insertUser);
                Toast.makeText(this, "inserted a new user " + currentUser.getUniqueID() +" as one was not found!", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Toast.makeText(this, "something went wrong in setupUser" + e, Toast.LENGTH_LONG).show();
            Log.d("SetupUser tag exception", " " + e);
        }
    }

    // the user wants to sign in with google
    private void googleLogin() {

        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(nGoogleApiClient);
        startActivityForResult(signIntent, RC_SIGN_IN);
    }

    // the user wants to sign in with facebook
    private void facebookLogin() {

        callbackManager = CallbackManager.Factory.create();
        facebookSignInButton.setReadPermissions("email", "public_profile");
        facebookSignInButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // if a user tries to login
                Log.d(TAG, "Facebook:On Success" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(),"You canceled something!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Facebook:On Cancel");
            }

            @Override
            public void onError(FacebookException error) {

                Toast.makeText(getApplicationContext(),"Something went wrong!", Toast.LENGTH_LONG).show();
                Log.d(TAG, "Facebook:On Error" + error.getStackTrace());
                Log.d(TAG, "Facebook:On Error" + error.getMessage());
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

                if(task.isSuccessful())
                {
                    FirebaseUser aUser = firebaseAuth.getCurrentUser();
                    updateUI(aUser);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Authentication failed.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    // what happens when the google sign in cannot access the google servers
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(getApplicationContext(),"Connection to google failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    // after a user logs in with either facebook or google, this method is called.
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google login failed", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // when a user signs in with google, the token is then parsed to the firebase authentication variable
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

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
                    Toast.makeText(getApplicationContext(),"firebase auth with google - failed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
