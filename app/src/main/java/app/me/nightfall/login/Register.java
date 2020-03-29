package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import app.me.nightfall.R;
import app.me.nightfall.home.Home;


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
                                                   findViewById(R.id.register_username).setVisibility(View.INVISIBLE);
                                                   findViewById(R.id.register_password).setVisibility(View.INVISIBLE);
                                                   findViewById(R.id.register_email).setVisibility(View.INVISIBLE);
                                                   findViewById(R.id.register_btn).setVisibility(View.INVISIBLE);

                                                   findViewById(R.id.register_pbar).setVisibility(View.VISIBLE);

                                                   //test from here
                                                   new Handler().postDelayed(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           testChange();
                                                       }
                                                   }, 3000);



                                               }
                                           }
                                       });
    }







    public void testChange(){
        Intent mainIntent = new Intent(Register.this, Home.class);
        Register.this.startActivity(mainIntent);
        Register.this.finish();
    }



    //From here is to go back

    public void toLogin(View view) {
        Intent mainIntent = new Intent(Register.this, Login.class);
        Register.this.startActivity(mainIntent);
        Register.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(Register.this, Login.class);
        Register.this.startActivity(mainIntent);
        Register.this.finish();

    }
}
