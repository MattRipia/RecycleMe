This is the repository for the RecycleMe Android Application created by:

Matt Ripia
Jaime King
Zihan Rasheed
Reuben White

Of Auckland University of Technology

---- Version 1.0 -----------------------------------------------------------------------------------

Applications purpose and main goal:
----------------------------------------------------------------------------------------------------
This application was designed to help Aucklanders appropriately recycle items depending on their zone/location and also tells the users which items can be recycled by scaning the barcode of an item to return a recyling number. Currently only recycling numbers 1 and 2 can be recycled in Auckland and depending on the item scanned tells the user whether or not their item is recylible or not. Other features of the application includes detailing the makeup of these different types of plastics, determining the next recycling day based off where the user lives as well as being able to set notifications to notify a user to take their bins out the day before pickup.


Current Features:
----------------------------------------------------------------------------------------------------
- Google/Facebook Authentication using Firebase as a manager/centralized service.
- Tracking of users, item data and scan history though a microsoft sql server hosted on Azure cloud platform.
- Barcode scanner api which returns the barcode of an item and references the database for additional info.
- The ability to determine a users current location.
- The ability to determine whether or not an item is recyclible or not.


To come:
----------------------------------------------------------------------------------------------------
- Using the users current location to determine their next pickup day.
- Using the next pickup day to set notifications reminding users the day before pickup.


Dependencies / Repositories / Imports:
----------------------------------------------------------------------------------------------------

   Permissions (androidManifest.xml)
        <uses-permission android:name="android.permission.INTERNET" />

   Firebase Authentication
        https://console.firebase.google.com/project/recycleme-autuni/overview
   
   Dependencies (build.grade Module: app)
        implementation 'com.google.firebase:firebase-auth:16.0.3'
        implementation 'com.google.firebase:firebase-core:16.0.3'
        implementation 'com.google.android.gms:play-services-auth:15.0.1'
        implementation 'com.facebook.android:facebook-android-sdk:[4,5)'
   
   Repositories (build.gradle Project: Recycle Me)
        google()
        jcenter()
        mavenCentral()
   
   Imports (MainActivity.java)
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
----------------------------------------------------------------------------------------------------
Database Connection-  Using Microsoft azure cloud services - sqlServer
  
     Permissions (androidManifest.xml)
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
  
     Module
        jtds-1.2.8.jar / Included in the application

     Repositories (build.gradle Project: Recycle Me)
         mavenCentral()

     Imports (MainActivity.java)
         import java.sql.SQLException;
         import java.sql.Connection;
         import java.sql.DriverManager;
         import java.sql.Statement;
----------------------------------------------------------------------------------------------------
