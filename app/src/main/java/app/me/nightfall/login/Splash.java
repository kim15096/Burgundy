package app.me.nightfall.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.home.MainActivity;
import io.opencensus.tags.Tag;

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

        if (!(0<=timeINT && timeINT<=3) || (timeINT<=24 && timeINT>=20)) {
            final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            if (currentUser != null) {
                toHome();
            } else {
                toLogin();
            }
        }
        else {
            toClosed();
        }
    }

    private void toHome(){
        Intent mainIntent = new Intent(Splash.this, MainActivity.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

    private void toVerify(){

    }

    private void toLogin(){
        Intent mainIntent = new Intent(Splash.this, Login.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

    private void toClosed() {
        Intent mainIntent = new Intent(Splash.this, Closed.class);
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

}
