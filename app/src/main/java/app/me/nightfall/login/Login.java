package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.me.nightfall.R;
import app.me.nightfall.home.Home;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_act);

    }

    public void toLoginPage(View view) {
        Intent mainIntent = new Intent(Login.this, LoginPage.class);
        Login.this.startActivity(mainIntent);
        Login.this.finish();
    }

    public void toRegister(View view) {
        Intent mainIntent = new Intent(Login.this, Register.class);
        Login.this.startActivity(mainIntent);
        Login.this.finish();
    }

}
