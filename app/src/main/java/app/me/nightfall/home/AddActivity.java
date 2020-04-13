package app.me.nightfall.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import app.me.nightfall.LobbyActivity;
import app.me.nightfall.R;
import app.me.nightfall.login.LoginPage;

public class AddActivity extends AppCompatActivity {

    private EditText title,desc;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private Long lobbyCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.create_title);
        //desc = findViewById(R.id.create_desc);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    public void createLobby(final View view){
        final String lobby_title = title.getText().toString();
        final String lobby_desc = desc.getText().toString();

        db.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                lobbyCount = documentSnapshot.getLong("lobby count");
                String username = documentSnapshot.get("username").toString();
                String lobbyID = firebaseUser.getUid() + lobbyCount.toString();

                Map<String, Object> createLobby = new HashMap<>();
                createLobby.put("title", lobby_title);
                createLobby.put("desc", lobby_desc);
                createLobby.put("userID", firebaseUser.getUid());
                createLobby.put("username", username);
                createLobby.put("lobbyID", lobbyID);
                createLobby.put("featured", false);

                db.collection("Lobbies").document(lobbyID).set(createLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        lobbyCount += 1;
                        db.collection("Users").document(firebaseUser.getUid()).update("lobby count", lobbyCount).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent mainIntent = new Intent(AddActivity.this, LobbyActivity.class);
                                AddActivity.this.startActivity(mainIntent);
                                AddActivity.this.finish();
                            }
                        });
                    }
                });
            }
        });


    }

    public void createlobby_back(View view){
        super.onBackPressed();
    }
}
