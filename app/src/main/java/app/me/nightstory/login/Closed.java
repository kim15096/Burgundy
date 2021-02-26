package app.me.nightstory.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;
import ir.samanjafari.easycountdowntimer.CountDownInterface;
import ir.samanjafari.easycountdowntimer.EasyCountDownTextview;

public class Closed extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private String EVENT_DATE_TIME;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLocale("ko");
        setContentView(R.layout.activity_closed);

        firebaseAuth = FirebaseAuth.getInstance();

        final Button enterButton = findViewById(R.id.enterBtn);
        final TextView closedTv = findViewById(R.id.closed_tv);
        final TextView userTV = findViewById(R.id.closed_usernameTV);
        final FrameLayout frameLayout = findViewById(R.id.closed_dimFrame);

        frameLayout.setVisibility(View.VISIBLE);

        enterButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        enterButton.setEnabled(false);
        enterButton.setTextColor(Color.GRAY);

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        Date date = new Date();
        date.setHours(18);
        date.setMinutes(0);
        date.setSeconds(0);
        EVENT_DATE_TIME = dateFormat.format(date);

        Date event_date = null;
        try {
            event_date = dateFormat.parse(EVENT_DATE_TIME);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date current_date = new Date();
        if (!current_date.after(event_date)) {
            long diff = event_date.getTime() - current_date.getTime();
            int Days = (int) diff / (24 * 60 * 60 * 1000);
            int Hours = (int) diff / (60 * 60 * 1000) % 24;
            int Minutes = (int) diff / (60 * 1000) % 60;
            int Seconds = (int) diff / 1000 % 60;

            final EasyCountDownTextview countDownTextview = findViewById(R.id.easyCountDownTextview);
            countDownTextview.setTime(Days, Hours, Minutes, Seconds);
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
                    closedTv.setText(R.string.closed_title);
                    FadeOut(countDownTextview);
                    TranslateDown(closedTv);
                }
            });
        }


        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    toHome();
                } else {
                    toLogin();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
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
