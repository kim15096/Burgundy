package app.burgundy.com.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.burgundy.com.R;
import app.burgundy.com.home.MainActivity;

public class Splash extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();




        SimpleDateFormat time = new SimpleDateFormat("k");
        Date currentTime = Calendar.getInstance().getTime();
        String timeRN = time.format(currentTime);
        final int timeINT = Integer.parseInt(timeRN);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                if (currentUser != null) {
                    toHome();
                } else {
                    toLogin();
                }

        }}, 2000);
    }

    private void toHome(){
        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }


    private void toLogin() {
        Intent mainIntent = new Intent(Splash.this, Sign.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

}
