package app.me.nightfall.lobby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class AddLobbyActivity extends AppCompatActivity {

    private EditText title,desc;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private Long lobbyCount;
    private String category = "", lang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lobby);

        title = findViewById(R.id.lobbyTitle);
        //desc = findViewById(R.id.create_desc);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        MaterialSpinner spinner = findViewById(R.id.categorySpinner);
        spinner.setItems("Relationship", "Family", "Career", "Social", "School", "Health", "AMA", "Miscellaneous");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                category = item;
            }
        });

        MaterialSpinner langSpinner = findViewById(R.id.langSpinner);
        langSpinner.setItems("English", "Chinese", "Korean", "Spanish", "Japanese");
        langSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                lang = item;
            }
        });

        langSpinner.setSelectedIndex(0);


    }


    public void createLobby(final View view){
        final String lobby_title = title.getText().toString();

        if (lobby_title.trim().isEmpty()){
            Toast.makeText(this, "Please choose a title", Toast.LENGTH_SHORT).show();

        }

        else if (category.equals("")){
            Toast.makeText(this, "Please choose a category", Toast.LENGTH_SHORT).show();
        }

        else {


            final FieldValue timestamp = FieldValue.serverTimestamp();

            final String id = db.collection("Lobbies").document().getId();


            final Map<String, Object> createLobby = new HashMap<>();
            createLobby.put("title", lobby_title);
            createLobby.put("lobbyID", id);
            createLobby.put("hostID", firebaseUser.getUid());
            createLobby.put("hostName", MainActivity.nickname);
            createLobby.put("lang", lang);
            createLobby.put("timestamp", timestamp);
            createLobby.put("tot_views", 1);
            createLobby.put("cur_views", 1);
            createLobby.put("category", category);

            db.collection("Lobbies").document(id).set(createLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    MainActivity.inLobbyID = id;
                    //MainActivity.inLobby = true

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
