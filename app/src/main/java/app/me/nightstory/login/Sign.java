package app.me.nightstory.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class Sign extends AppCompatActivity {

    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private String username;
    private Button anonyLog, gsignBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this,"ko");
        setContentView(R.layout.activity_sign);

        firebaseAuth = FirebaseAuth.getInstance();

        gsignBtn = findViewById(R.id.gsignBtn);
        anonyLog = findViewById(R.id.anonyLogBtn);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("69136461367-hbb3h9oe5uml44gei3jdosqpnan79t8e.apps.googleusercontent.com").requestProfile().build();



        googleSignInClient = GoogleSignIn.getClient(Sign.this,  googleSignInOptions);

        gsignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });

        anonyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(Sign.this, R.style.MyGravity);
                pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                pd.setCancelable(false);
                pd.show();

                firebaseAuth.signInAnonymously()
                        .addOnCompleteListener(Sign.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    final Map<String, Object> createUser = new HashMap<>();
                                    createUser.put("userID", user.getUid());
                                    createUser.put("inLobby", "");
                                    createUser.put("imageURL", "https://firebasestorage.googleapis.com/v0/b/nightfall-alpha.appspot.com/o/Admin%2Fpngwing.com.png?alt=media&token=41da1f77-f4b1-4138-9ad3-9f9f59457fc7");
                                    createUser.put("username", "Random User");

                                    FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(createUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    Intent mainIntent = new Intent(Sign.this, Login.class);
                                                    Sign.this.startActivity(mainIntent);
                                                    pd.dismiss();
                                                }
                                            }, 250);

                                        }
                                    });

                                } else {
                                    Toast.makeText(Sign.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);

            final ProgressDialog pd = new ProgressDialog(Sign.this, R.style.MyGravity);
            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            pd.setCancelable(false);
            pd.show();

            if (signInAccountTask.isSuccessful()){
                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount != null){
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){

                                    FirebaseUser user = firebaseAuth.getCurrentUser();


                                    final Map<String, Object> createUser = new HashMap<>();
                                    createUser.put("userID", user.getUid());
                                    createUser.put("inLobby", "");
                                    createUser.put("imageURL", "https://firebasestorage.googleapis.com/v0/b/nightfall-alpha.appspot.com/o/Admin%2Fpngwing.com.png?alt=media&token=41da1f77-f4b1-4138-9ad3-9f9f59457fc7");
                                    createUser.put("username", "Random User");

                                    FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(createUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    Intent mainIntent = new Intent(Sign.this, Login.class);
                                                    Sign.this.startActivity(mainIntent);
                                                    pd.dismiss();
                                                }
                                            }, 500);

                                        }
                                    });

                                } else {
                                    Toast.makeText(Sign.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                }}
                                );
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
            else {
                pd.dismiss();
            }
        }

    }

}