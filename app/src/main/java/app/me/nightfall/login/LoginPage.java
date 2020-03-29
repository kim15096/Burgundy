package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.me.nightfall.R;
import app.me.nightfall.home.Home;

public class LoginPage extends AppCompatActivity {

    private static final String LOG_TAG = LoginPage.class.getSimpleName();

    private FirebaseAuth mAuth;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_act);

        // Initialize Firebase Auth
        // Note that the checking whether the user is signed in is done in Splash
        mAuth = FirebaseAuth.getInstance();

        // Initialize View private variables
        mEmailEditText = findViewById(R.id.login_email);
        mPasswordEditText = findViewById(R.id.login_password);

    }

    public void toLogin(View view) {
        Intent mainIntent = new Intent(LoginPage.this, Login.class);
        LoginPage.this.startActivity(mainIntent);
        LoginPage.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(LoginPage.this, Login.class);
        LoginPage.this.startActivity(mainIntent);
        LoginPage.this.finish();

    }

    public void signIn(View view) {
        Log.d(LOG_TAG, "signIn is called");
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        // Sign in with email using method from Firebase Auth
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(LOG_TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            changeToHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginPage.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Change Activity from LoginPage to Home upon successful sign in
    private void changeToHome() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
