package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;


public class Register extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser firebaseUser;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_act);


    }

    public void register(View view){

        MaterialEditText email_et = findViewById(R.id.register_email);
        MaterialEditText password_et = findViewById(R.id.register_password);
        String email = email_et.getText().toString().trim();
        String password = password_et.getText().toString().trim();
        final EditText username_et = findViewById(R.id.register_username);


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<AuthResult> task) {
                                               if (task.isSuccessful()) {

                                                    username = username_et.getText().toString().trim();

                                                   firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                                   String userID = firebaseUser.getUid();

                                                   final Map<String, Object> userInfo = new HashMap<>();
                                                   userInfo.put("userID", userID);
                                                   userInfo.put("username", username);
                                                   userInfo.put("inLobby", "");


                                                   FirebaseFirestore.getInstance().collection("Users").document(userID).set(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<Void> task) {
                                                           if (task.isSuccessful()){
                                                               UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                       .setDisplayName(username).build();

                                                               firebaseUser.updateProfile(profileUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                   @Override
                                                                   public void onSuccess(Void aVoid) {
                                                                       onBackPressed();
                                                                   }
                                                               });
                                                           }
                                                       }
                                                   });

                                               }
                                           }
                                       });
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
