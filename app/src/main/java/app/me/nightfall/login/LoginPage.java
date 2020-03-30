package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import app.me.nightfall.R;
import app.me.nightfall.home.Home;

public class LoginPage extends AppCompatActivity {

    private static final String LOG_TAG = LoginPage.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;

    // Firebase authorization
    private FirebaseAuth mAuth;

    // Email login variables
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    // Google sign-in variables
    private GoogleSignInClient mGoogleSignInClient;

    // Facebook login variables
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_act);

        // Initialize Firebase Auth
        // Note that the checking whether the user is signed in is done in Splash Activity
        mAuth = FirebaseAuth.getInstance();

        // ---------- Initialize email login ---------- //
        mEmailEditText = findViewById(R.id.login_email);
        mPasswordEditText = findViewById(R.id.login_password);

        // ---------- Initialize Google sign-in ---------- //
        // Configure Google sign-in
        GoogleSignInOptions mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified above
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGso);
        SignInButton mGoogleSignInBtn = findViewById(R.id.google_signin_btn);
        mGoogleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

        // ---------- Initialize Facebook Login ---------- //
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.fb_login_btn);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(LOG_TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(LOG_TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(LOG_TAG, "facebook:onError", error);
                // ...
            }
        });

        Log.d(LOG_TAG, "onCreate success");
    }

    /* Handle login Activity results from Google sign-in and Facebook login */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) /* Google sign-in */ {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(LOG_TAG, "Google sign in failed", e);
                // ...
            }
        } else /* Facebook login */ {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    /* Move to Login Activity when clicking bottom back button */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(LoginPage.this, Login.class);
        LoginPage.this.startActivity(mainIntent);
        LoginPage.this.finish();

    }

    /* Login with email. Called when login_btn is clicked. */
    public void emailLogin(View view) {
        Log.d(LOG_TAG, "signIn is called");
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        // Sign in with email using method from Firebase Auth
        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(LOG_TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                toHome();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginPage.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /* Sign in with Google. Called when google_signin_btn is clicked. */
    public void googleSignIn() {
        Log.d(LOG_TAG, "googleSignIn is called");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /* Exchange ID token from GoogleSignInAccount for Firebase credential and authenticate with Firebase.
     * Called when onActivityResult is invoked after Google sign-in activity. */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(LOG_TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            toHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.login_page_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    /* Exchange ID token from Facebook AccessToken for Firebase credential and authenticate with Firebase.
     * Called when Facebook login callback is successful. */
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(LOG_TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            toHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // Change Activity from LoginPage to Home upon successful sign in
    private void toHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }

    /* Move to Login Activity when clicking top back button */
    public void toLogin(View view) {
        Intent mainIntent = new Intent(LoginPage.this, Login.class);
        LoginPage.this.startActivity(mainIntent);
        LoginPage.this.finish();
    }

    public void toForgotPassword(View view) {
        Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
        startActivity(intent);
    }
}
