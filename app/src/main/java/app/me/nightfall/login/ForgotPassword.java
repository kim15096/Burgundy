package app.me.nightfall.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import app.me.nightfall.R;

public class ForgotPassword extends AppCompatActivity {

    private static final String LOG_TAG = LoginPage.class.getSimpleName();
    private FirebaseAuth mAuth;
    private EditText recoveryEmailEditText;
    // private Button resetPasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        recoveryEmailEditText = findViewById(R.id.recovery_email);
        // resetPasswordBtn = findViewById(R.id.reset_password_button);
    }

    public void sendRecoverEmail(View view) {
        String email = recoveryEmailEditText.getText().toString();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(LOG_TAG, "Email sent.");

                    // Return to LoginPage
                    finish();
                }
            }
        });
    }

}
