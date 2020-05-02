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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class AddLobbyActivity extends AppCompatActivity {

    private EditText title,desc;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private Long lobbyCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lobby);

        title = findViewById(R.id.create_title);
        //desc = findViewById(R.id.create_desc);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    public void createLobby(final View view){
        final String lobby_title = title.getText().toString();

        db.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final FieldValue timestamp = FieldValue.serverTimestamp();

                String lobbyID = firebaseUser.getUid();

                Map<String, Object> createLobby = new HashMap<>();
                createLobby.put("title", lobby_title);
                createLobby.put("hostID", firebaseUser.getUid());
                createLobby.put("p1_ID", null);
                createLobby.put("lobbyID", lobbyID);
                createLobby.put("timestamp", timestamp);

                db.collection("Lobbies").document(lobbyID).set(createLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                                MainActivity.openLobby = true;
                                MainActivity.inLobby = true;

                                db.collection("Users").document(firebaseUser.getUid()).update("inLobby", true);

                                Intent mainIntent = new Intent(AddLobbyActivity.this, MainActivity.class);
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
