package app.me.nightfall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.me.nightfall.home.MainActivity;

public class LobbyActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String lobbyID, hostID;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        title_tv = findViewById(R.id.lobbyTitle_tv);

        Intent intent = getIntent(); // gets the previously created intent
        hostID = intent.getStringExtra("lobbyHostID"); // will return "FirstKeyValue"
        lobbyID = intent.getStringExtra("lobbyID");

        if (lobbyID != null) {

            firestore.collection("Lobbies").document(lobbyID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    String lobbyTitle = documentSnapshot.get("title").toString();
                    title_tv.setText(lobbyTitle);
                }
            });
        }
        else {
            title_tv.setText("not finished");
        }





    }

    public void exitLobby(final View view){
        FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid()).update("inLobby", false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                lobby_back(view);
            }
        });
    }

    public void lobby_back(View view){
        super.onBackPressed();
    }

}
