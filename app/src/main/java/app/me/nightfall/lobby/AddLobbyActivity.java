package app.me.nightfall.lobby;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private String category;


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


    }


    public void createLobby(final View view){
        final String lobby_title = title.getText().toString();

        final FieldValue timestamp = FieldValue.serverTimestamp();

        final String id = db.collection("Lobbies").document().getId();


        Map<String, Object> createLobby = new HashMap<>();
        createLobby.put("title", lobby_title);
        createLobby.put("hostID", firebaseUser.getUid());
        createLobby.put("lobbyID", id);
        createLobby.put("p1_ID", "");
        createLobby.put("p2_ID", "");
        createLobby.put("p3_ID", "");
        createLobby.put("timestamp", timestamp);
        createLobby.put("count", 1);
        createLobby.put("category", category);

        db.collection("Lobbies").document(id).set(createLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                MainActivity.openLobby = true;
                //MainActivity.inLobby = true;


                db.collection("Users").document(firebaseUser.getUid()).update("inLobby",  id);

                final Map<String, Object> createlobby = new HashMap<>();
                createlobby.put("senderID", "bot");
                createlobby.put("username", firebaseUser.getUid());
                createlobby.put("message", "nf_joined");
                createlobby.put("timestamp", FieldValue.serverTimestamp());

                db.collection("Lobbies").document(firebaseUser.getUid()).collection("Chat").document().set(createlobby).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent mainIntent = new Intent(AddLobbyActivity.this, LobbyActivity_temp.class);
                        AddLobbyActivity.this.startActivity(mainIntent);
                        AddLobbyActivity.this.finish();

                    }
                });


            }
        });
    }

    public void createlobby_back(View view){
        super.onBackPressed();
    }
}
