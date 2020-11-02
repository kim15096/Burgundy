package app.me.nightfall.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class Login extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        final Button enterButton = findViewById(R.id.enterBtn);


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


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signInAnonymously()
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    toHome();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
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