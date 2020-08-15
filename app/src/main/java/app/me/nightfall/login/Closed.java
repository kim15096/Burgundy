package app.me.nightfall.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class Closed extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed);

        firebaseAuth = FirebaseAuth.getInstance();

        final Button enterButton = findViewById(R.id.enterBtn);
        final TextView closedTv = findViewById(R.id.closed_tv);
        final TextView userTV = findViewById(R.id.closed_usernameTV);
        final FrameLayout frameLayout = findViewById(R.id.closed_dimFrame);
        final TextView logBtn = findViewById(R.id.logBtn);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){

                    FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String displayName = documentSnapshot.get("username").toString();
                            userTV.setText("Welcome " + displayName + "!");
                            enterButton.setText("Enter Nightfall");
                            logBtn.setText("Logout");
                        }
                    });

                }

                else {
                    enterButton.setText("Register");
                    userTV.setText("");
                    logBtn.setText("Login");
                }
            }
        };

        closedTv.setText("Zzz");
        frameLayout.setVisibility(View.VISIBLE);

        enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        enterButton.setEnabled(false);
        enterButton.setTextColor(Color.GRAY);

        final EasyCountDownTextview countDownTextview = findViewById(R.id.easyCountDownTextview);
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
                closedTv.setText("Come Tell Your Stories");
                FadeOut(countDownTextview);
                TranslateDown(closedTv);
            }
        });

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logBtn.getText().equals("Login")){
                    Intent mainIntent = new Intent(Closed.this, LoginPage.class);
                    Closed.this.startActivity(mainIntent);
                }
                else {
                    firebaseAuth.signOut();
                }

            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    toHome();
                } else {
                    Intent mainIntent = new Intent(Closed.this, Register.class);
                    Closed.this.startActivity(mainIntent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void toHome(){
        Intent mainIntent = new Intent(Closed.this, MainActivity.class);
        Closed.this.startActivity(mainIntent);
        Closed.this.finish();
    }

    private void toLogin(){
        Intent mainIntent = new Intent(Closed.this, Login.class);
        Closed.this.startActivity(mainIntent);
        Closed.this.finish();
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