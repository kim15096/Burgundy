package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void toLoginPage(View view) {
        Intent mainIntent = new Intent(Login.this, LoginPage.class);
        Login.this.startActivity(mainIntent);

    }

    public void toRegister(View view) {

        Intent mainIntent = new Intent(Login.this, Register.class);
        Login.this.startActivity(mainIntent);


    }

    public void loginGuest(View view){
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("splash", "signInAnonymously:success");

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            String userID = user.getUid();

                            Log.d("splash", userID);

                            Map<String, Object> unknownUser = new HashMap<>();
                            unknownUser.put("userID", userID);
                            unknownUser.put("username", "guest_user");
                            unknownUser.put("lobby count", 0);

                            FirebaseFirestore.getInstance().collection("Users").document(userID).set(unknownUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent mainIntent = new Intent(Login.this, MainActivity.class);
                                    Login.this.startActivity(mainIntent);
                                    Login.this.finish();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("splash", "signInAnonymously:failure", task.getException());
                            Toast.makeText(Login.this, "Please try again.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

}
