package app.me.nightfall.login;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_act);

    }

    public void toLogin(View view) {
        super.onBackPressed();
    }

    public void loginBtn(View view){

        EditText email_et = findViewById(R.id.login_email);
        EditText pass_et = findViewById(R.id.login_pass);

        String emailInput = email_et.getText().toString().trim();
        String passwordInput = pass_et.getText().toString().trim();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            onBackPressed();

                        }
                    }
                });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
