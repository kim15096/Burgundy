package app.me.nightstory.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private EditText username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        final Button enterButton = findViewById(R.id.enterBtn);
        username =  findViewById(R.id.username_et);

        enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        enterButton.setEnabled(false);
        enterButton.setTextColor(Color.GRAY);


        /*final EasyCountDownTextview countDownTextview = findViewById(R.id.easyCountDownTextview);
        countDownTextview.setTime(0, 0, 0, 3);
        countDownTextview.startTimer();
        countDownTextview.setOnTick(new CountDownInterface() {
            @Override
            public void onTick(long time) {

            }
            @Override
            public void onFinish() {
                enterButton.getBackground().setColorFilter(null);
                enterButton.setEnabled(true);
                enterButton.setTextColor(getResources().getColor(R.color.textColorGray));
                frameLayout.setVisibility(View.INVISIBLE);
                FadeOut(countDownTextview);
            }
        });*/

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


    private void FadeOut(EasyCountDownTextview view){

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(100);
        fadeOut.setDuration(500);

        view.setAnimation(fadeOut);
        view.setVisibility(View.INVISIBLE);
    }

    private void TranslateDown(TextView view){

        Animation down = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 0f,
                TranslateAnimation.RELATIVE_TO_SELF, 2f);
        down.setInterpolator(new DecelerateInterpolator()); //and this
        down.setStartOffset(100);
        down.setDuration(1000);
        down.setFillAfter(true);

        view.setAnimation(down);
    }
}