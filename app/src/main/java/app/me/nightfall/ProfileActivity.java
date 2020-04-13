package app.me.nightfall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import app.me.nightfall.home.MainActivity;
import app.me.nightfall.login.Login;
import app.me.nightfall.login.LoginPage;
import app.me.nightfall.login.Splash;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void signOut(View view){
        firebaseAuth.signOut();
        Intent mainIntent = new Intent(ProfileActivity.this, Login.class);
        ProfileActivity.this.startActivity(mainIntent);
        finishAffinity();
    }

    public void toMain(View view){
        super.onBackPressed();
    }
}