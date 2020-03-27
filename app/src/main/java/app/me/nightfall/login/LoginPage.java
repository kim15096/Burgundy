package app.me.nightfall.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.me.nightfall.R;

public class LoginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page_act);

    }

    public void toLogin(View view) {
        Intent mainIntent = new Intent(LoginPage.this, Login.class);
        LoginPage.this.startActivity(mainIntent);
        LoginPage.this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent mainIntent = new Intent(LoginPage.this, Login.class);
        LoginPage.this.startActivity(mainIntent);
        LoginPage.this.finish();

    }
}
