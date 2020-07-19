package app.me.nightfall.lobby;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.me.nightfall.R;

public class LobbyActivity_temp extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String lobbyID, hostID, username, senderID;
    private TextView title_tv;
    private AlertDialog alertDialog;
    private EditText chat_et;

    private RecyclerView chat_recycler;
    private ChatRecyclerAdapter chatRecyclerAdapter;
    private List<ChatPostModel> chatList;

    private ImageView sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_lobby_frag);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        chat_recycler = findViewById(R.id.chat_recycler);

        title_tv = findViewById(R.id.lobbyTitle_tv);
        sendBtn = findViewById(R.id.msg_sendBtn);
        chat_et = findViewById(R.id.chat_et);

        Intent intent = getIntent(); // gets the previously created intent
        hostID = intent.getStringExtra("lobbyHostID"); // will return "FirstKeyValue"
        lobbyID = intent.getStringExtra("lobbyID");

        //user details
        final String senderID = firebaseUser.getUid();
        final String username = firebaseUser.getDisplayName();


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

        //recycler setting up


        final List a = new ArrayList();
        chatRecyclerAdapter = new ChatRecyclerAdapter(a);
        chat_recycler.setLayoutManager(new LinearLayoutManager(this));
        chat_recycler.setAdapter(chatRecyclerAdapter);
        chat_recycler.setHasFixedSize(true);
        chat_recycler.setNestedScrollingEnabled(false);
        ((LinearLayoutManager)chat_recycler.getLayoutManager()).setStackFromEnd(true);


        sendBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String msg = chat_et.getText().toString();

            final Map<String, Object> chat = new HashMap<>();
            chat.put("senderID", senderID);
            chat.put("username", username);
            chat.put("message", msg);
            chat.put("timestamp", 00);

            firestore.collection("Lobbies").document(lobbyID)
                    .collection("Chat").document().set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    chat_et.setText("");
                    a.add(0);
                }
            });
        }
    });



    }



    public void exitLobby(final View view){

        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setTitle("Exit lobby?")
                .setMessage("You will end the session and will not be able to return.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid()).update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                FirebaseFirestore.getInstance().collection("Lobbies").document(firebaseUser.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        lobby_back(view);
                                    }
                                });
                            }
                        });
                    }
                })
                .setNegativeButton("Stay", null)
                .show();



    }

    public void lobby_back(View view){
        super.onBackPressed();

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setTitle("Exit lobby?")
                .setMessage("You will end the session and will not be able to return.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid()).update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseFirestore.getInstance().collection("Lobbies").document(firebaseUser.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        lobby_back(null);
                                    }
                                });
                            }
                        });
                    }
                })

                .setNegativeButton("Stay", null)
                .show();
    }
}
