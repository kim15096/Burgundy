package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;


public class Register extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_act);

        firestore = FirebaseFirestore.getInstance();



    }

    public void register(View view){

        MaterialEditText email_et = findViewById(R.id.register_email);
        MaterialEditText password_et = findViewById(R.id.register_password);
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();

        Snackbar.make(view, email, Snackbar.LENGTH_SHORT).show();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if (task.isSuccessful()) {

                                                   EditText username_et = findViewById(R.id.register_username);
                                                   String username = username_et.getText().toString();
                                                   String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                   Map<String, Object> userString = new HashMap<>();
                                                   userString.put("userID", userID);
                                                   userString.put("username", username);
                                                   userString.put("lobby count", 0);

                                                   firestore.collection("Users").document(userID).set(userString).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void aVoid) {
                                                           authenticate();
                                                       }
                                                   });

                                               }
                                           }
                                       });
    }







    public void authenticate(){
        Intent mainIntent = new Intent(Register.this, MainActivity.class);
        Register.this.startActivity(mainIntent);
        Register.this.finish();
    }



    //From here is to go back

    public void toLogin(View view) {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
}
