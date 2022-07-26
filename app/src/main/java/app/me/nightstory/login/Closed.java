package app.me.nightstory.login;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;


public class Closed extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private String EVENT_DATE_TIME;
    private final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this,"ko");
        setContentView(R.layout.activity_closed);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        final TextView closedTv = findViewById(R.id.closed_tv);



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
            long timeDiff = event_date.getTime() - current_date.getTime();
            int d = (int) timeDiff / (24 * 60 * 60 * 1000);
            int h = (int) timeDiff / (60 * 60 * 1000) % 24;
            int m = (int) timeDiff / (60 * 1000) % 60;
            int s = (int) timeDiff / 1000 % 60;

        }


    }

    private void toHome(){
        Intent mainIntent = new Intent(Closed.this, MainActivity.class);
        Closed.this.startActivity(mainIntent);
        Closed.this.finish();
    }

    private void toLogin(){
        Intent mainIntent = new Intent(Closed.this, Sign.class);
        Closed.this.startActivity(mainIntent);
        Closed.this.finish();
    }


    public void adminEnter(View view){

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            toHome();
        } else {
            toLogin();
        }
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
