package app.me.nightstory.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText username;
    private CheckBox checkBox;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this,"ko");
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        final Button enterButton = findViewById(R.id.enterBtn);
        username =  findViewById(R.id.username_et);
        checkBox = findViewById(R.id.login_checkbox);

        enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        enterButton.setEnabled(false);
        enterButton.setTextColor(Color.GRAY);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!username.getText().toString().equals("") && checkBox.isChecked()==true){

                    enterButton.getBackground().setColorFilter(null);
                    enterButton.setEnabled(true);
                    enterButton.setTextColor(getResources().getColor(R.color.textColorGray));

                }
                else {
                    enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    enterButton.setEnabled(false);
                    enterButton.setTextColor(Color.GRAY);
                }
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked==true && !username.getText().toString().equals("")){
                   enterButton.getBackground().setColorFilter(null);
                   enterButton.setEnabled(true);
                   enterButton.setTextColor(getResources().getColor(R.color.textColorGray));

               }
               else {
                   enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                   enterButton.setEnabled(false);
                   enterButton.setTextColor(Color.GRAY);

               }
            }
        });


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!username.getText().toString().equals("")) {

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    final ProgressDialog pd = new ProgressDialog(Login.this, R.style.MyGravity);
                    pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    pd.setCancelable(false);
                    pd.show();



                    firestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).update("username", username.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    toHome();
                                    pd.dismiss();
                                }
                            }, 500);
                        }
                    });





                }

            }
        });

    }


    private void toHome(){
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        Login.this.startActivity(mainIntent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).delete();
            user.delete();

        }

        GoogleSignInClient googleSignInClient;

        googleSignInClient = GoogleSignIn.getClient(Login.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Login.super.onBackPressed();
                    Toast.makeText(getBaseContext(), "계정만들기를 실패했습니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loginBack(View view){
        onBackPressed();
    }
}