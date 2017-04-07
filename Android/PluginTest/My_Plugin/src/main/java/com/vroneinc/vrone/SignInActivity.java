package com.vroneinc.vrone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

import static android.graphics.Color.BLACK;

public class SignInActivity extends AuthBaseActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";

    //Class variables for authentication
    private SignInButton mSignInButton;
    private Button mSkipButton;
    private GoogleApiClient mGoogleApiClient;
    private RelativeLayout mContentView;

    private int mFailedAttempt = 0;

    private static final int GoogleSignInButton = R.id.GoogleSignInButton;
    private static final int SkipSignInButton = R.id.skipSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mContentView = (RelativeLayout) findViewById(R.id.activity_sign_in);

        //getActionBar().hide();
        Window window = getWindow();
        window.setStatusBarColor(BLACK);

        mSignInButton = (SignInButton) findViewById(R.id.GoogleSignInButton);
        mSignInButton.setOnClickListener(this);

        mSkipButton = (Button) findViewById(R.id.skipSignInButton);
        mSkipButton.setOnClickListener(this);

        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setColorScheme(SignInButton.COLOR_DARK);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();


        if(mGoogleApiClient.isConnected()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onStart(){
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {
        //switch (v.getId()) {
        if (v.getId() == GoogleSignInButton){
            //case GoogleSignInButton: {
            if(isNetworkAvailable(this)) {
                signIn();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.network_unavailable_toast), Toast.LENGTH_SHORT);
                toast.show();
            }
                //break;
            //}
        }
        else if (v.getId() == SkipSignInButton) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }

            handleSignInResult(result);
        }
    }


    // Method to test for network connectivity
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // test for connection
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void signIn(){
        super.signIn(mGoogleApiClient);
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            Intent intent = new Intent(this, MainActivity.class);
            super.handleSignInResult(result, intent);
        } else {
            Log.i("Result", "Not successful");
            if (mSkipButton.getVisibility() == View.GONE && mFailedAttempt > 0)
                mSkipButton.setVisibility(View.VISIBLE);
            mFailedAttempt++;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        backPressGoHome();
    }

}
