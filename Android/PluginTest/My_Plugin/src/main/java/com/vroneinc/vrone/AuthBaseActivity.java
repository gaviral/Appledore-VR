package com.vroneinc.vrone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vroneinc.vrone.data.User;


public class AuthBaseActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "AuthBaseActivity";

    //Sign in Class Variables
    protected static final int RC_SIGN_IN = 9001;
    protected FirebaseUser mFirebaseUser;
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected GoogleSignInOptions mGso;

    private Resources mResources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mResources = getResources();

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    // User is signed in
                    mFirebaseUser = firebaseUser;
                    uploadUser();
                    Log.d(TAG, "onAuthStateChanged_SignIn:signed_in:" + firebaseUser.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged_SignIn:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    protected void signIn(GoogleApiClient googleApiClient){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void signOut(GoogleApiClient googleApiClient){
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast toast = Toast.makeText(AuthBaseActivity.this,
                        mResources.getString(R.string.sign_out_text),
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 100);
                toast.show();

                Intent i = new Intent(AuthBaseActivity.this, SignInActivity.class);
                AuthBaseActivity.this.startActivity(i);
                AuthBaseActivity.this.finish();
            }
        });
    }

    protected void handleSignInResult(GoogleSignInResult result, Intent intent) {
        Log.d(TAG, "handledSignResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                uploadUser();
                startActivity(intent);
            }
        } else {
            //what to do if the sign in was not a success

        }
    }

    // Method to upload the user into the database
    protected void uploadUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            User userData = new User(user.getDisplayName(), user.getEmail(), user.getUid());
            userData.uploadUser();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    // Helper method for the extending classes to call on back press
    protected void backPressGoHome() {
        //go to home screen and clear back stack
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}