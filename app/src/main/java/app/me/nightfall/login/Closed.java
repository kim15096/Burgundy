package app.me.nightfall.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        enterButton.setVisibility(View.GONE);
        closedTv.setText("CLOSED");
        frameLayout.setVisibility(View.VISIBLE);

        EasyCountDownTextview countDownTextview = findViewById(R.id.easyCountDownTextview);
        countDownTextview.setTime(0, 0, 0, 5);
        countDownTextview.startTimer();
        countDownTextview.setOnTick(new CountDownInterface() {
            @Override
            public void onTick(long time) {

            }
            @Override
            public void onFinish() {
                enterButton.setVisibility(View.VISIBLE);
                frameLayout.setVisibility(View.INVISIBLE);
                closedTv.setText("OPENED");
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
}