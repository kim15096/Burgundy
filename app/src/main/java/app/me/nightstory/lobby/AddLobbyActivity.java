package app.me.nightstory.lobby;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class AddLobbyActivity extends AppCompatActivity {

    private EditText title, info;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private String desc = "";
    private Button createBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this, "ko");
        setContentView(R.layout.activity_add_lobby);

        title = findViewById(R.id.lobbyTitle);
        info = findViewById(R.id.addLobby_info);
        createBtn = findViewById(R.id.createlob_btn);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        createBtn.setEnabled(false);
        createBtn.setTextColor(Color.GRAY);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().equals("") || info.getText().toString().equals("")){

                    createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    createBtn.setEnabled(false);
                    createBtn.setTextColor(Color.GRAY);

                }
                else {
                    createBtn.getBackground().setColorFilter(null);
                    createBtn.setEnabled(true);
                    createBtn.setTextColor(getResources().getColor(R.color.textColorGray));
                }
            }
        });

        info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().equals("") || info.getText().toString().equals("")){

                    createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    createBtn.setEnabled(false);
                    createBtn.setTextColor(Color.GRAY);

                }
                else {
                    createBtn.getBackground().setColorFilter(null);
                    createBtn.setEnabled(true);
                    createBtn.setTextColor(getResources().getColor(R.color.textColorGray));
                }
            }
        });



    }


    public void createLobby(final View view){
        final String lobby_title = title.getText().toString();
        String lobby_desc = info.getText().toString();

        if (lobby_title.trim().isEmpty()){
            Toast.makeText(this, "Please choose a title", Toast.LENGTH_SHORT).show();

        }

        else {

            final FieldValue timestamp = FieldValue.serverTimestamp();

            final String id = db.collection("Lobbies").document().getId();


            final Map<String, Object> createLobby = new HashMap<>();
            createLobby.put("title", lobby_title);
            createLobby.put("lobbyID", id);
            createLobby.put("hostID", firebaseUser.getUid());
            createLobby.put("hostName", MainActivity.nickname);
            createLobby.put("timestamp", timestamp);
            createLobby.put("tot_views", 1);
            createLobby.put("cur_views", 1);
            createLobby.put("active", "true");
            createLobby.put("desc", lobby_desc);

            db.collection("Lobbies").document(id).set(createLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    MainActivity.inLobbyID = id;

                    db.collection("Users").document(firebaseUser.getUid()).update("inLobby", id).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent mainIntent = new Intent(AddLobbyActivity.this, LobbyActivity.class);
                            AddLobbyActivity.this.startActivity(mainIntent);
                            AddLobbyActivity.this.finish();
                        }
                    });



                }
            });
        }
    }

    public void createlobby_back(View view){
        super.onBackPressed();
    }

}
