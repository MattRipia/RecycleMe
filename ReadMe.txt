This is the repository for the RecycleMe Android Application created by:

Matt Ripia
Jaime King
Zihan Rasheed
Reuben White

Of Auckland University of Technology

---- Version 1.0 ----

Current Features:
----------------------------------------------------------------------------------------------------
Google/Facebook Authentication using Firebase as a manager

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
