package app.me.nightfall.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.me.nightfall.home.MainActivity;

public class Splash extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        SimpleDateFormat time = new SimpleDateFormat("k");
        Date currentTime = Calendar.getInstance().getTime();
        String timeRN = time.format(currentTime);
        int timeINT = Integer.parseInt(timeRN);


        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            toHome();
        } else {
            toClosed();
        }

        /*if ((0<=timeINT && timeINT<=4) || (timeINT<=24 && timeINT>=20)) {
            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                toHome();
            } else {
                toClosed();
            }
        }
        else {
            toClosed();
        }*/
    }

    private void toHome(){
        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }


    private void toClosed() {
        Intent mainIntent = new Intent(Splash.this, Login.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

}
