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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Locale;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLocale("ko");
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        final Button enterButton = findViewById(R.id.enterBtn);
        username =  findViewById(R.id.username_et);

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
                if (username.getText().toString().equals("")){

                    enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    enterButton.setEnabled(false);
                    enterButton.setTextColor(Color.GRAY);

                }
                else {
                    enterButton.getBackground().setColorFilter(null);
                    enterButton.setEnabled(true);
                    enterButton.setTextColor(getResources().getColor(R.color.textColorGray));
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

                    firebaseAuth.signInAnonymously()
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = firebaseAuth.getCurrentUser();

                                        final Map<String, Object> createUser = new HashMap<>();
                                        createUser.put("userID", user.getUid());
                                        createUser.put("inLobby", "");
                                        createUser.put("username", username.getText().toString());

                                        FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(createUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    public void run() {
                                                        toHome();
                                                        pd.dismiss();
                                                    }
                                                }, 500);

                                            }
                                        });

                                    } else {
                                        Toast.makeText(Login.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

            }
        });

    }


    private void toHome(){
        Intent mainIntent = new Intent(Login.this, MainActivity.class);
        Login.this.startActivity(mainIntent);
        Login.this.finish();
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
}