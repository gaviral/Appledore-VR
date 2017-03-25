package com.vroneinc.vrone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
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

// TODO: Implement Android to FPGA shit
// TODO: Clean up

public class AuthBaseActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "AuthBaseActivity";

    //Sign in Class Variables
    protected static final int RC_SIGN_IN = 9001;
    protected FirebaseUser mFirebaseUser;
    protected FirebaseAuth mAuth;
    protected FirebaseAuth.AuthStateListener mAuthListener;
    protected GoogleSignInOptions mGso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        Log.i("RESULT", "IS SIGNING IN");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void signOut(GoogleApiClient googleApiClient){
        mAuth.signOut();

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
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
    // TODO: Might create a data class for Users and move this in there
    protected void uploadUser(){
        // TODO: If a data class for Users created, might want to split this function into several setters/getters
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            DatabaseReference users = FirebaseDatabase.getInstance().getReference("Users");
            // Set the name of the user in the database
            users.child(userId).child("Name").setValue(userName);
            // Set the email of the user in the database
            users.child(userId).child("Email").setValue(userEmail);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}