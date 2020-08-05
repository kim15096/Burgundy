package app.me.nightfall.login;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class Closed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closed);

        final Button enterButton = findViewById(R.id.enterBtn);
        final TextView closedTv = findViewById(R.id.closed_tv);
        final FrameLayout frameLayout = findViewById(R.id.closed_dimFrame);

        closedTv.setText("Opens in ...");
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
                enterButton.setTextColor(Color.WHITE);
                frameLayout.setVisibility(View.INVISIBLE);
                closedTv.setText("Come Tell Your Stories");
                FadeOut(countDownTextview);
                TranslateDown(closedTv);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    toHome();
                } else {
                    toLogin();
                }
            }
        });

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