package app.me.nightfall.lobby;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class LobbyActivity_temp extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String lobbyID, hostID, username, senderID;
    private TextView title_tv;
    private AlertDialog alertDialog;
    private EditText chat_et;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatHour = new SimpleDateFormat("aa hh:mm");

    private RecyclerView chat_recycler;
    private ChatRecyclerAdapter chatRecyclerAdapter;
    private List<ChatPostModel> a;

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

        VideoView view = findViewById(R.id.lobby_bg_video);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.chat_bg;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);
        }
        view.setVideoURI(Uri.parse(path));
        view.start();

        view.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        //user details
        senderID = firebaseUser.getUid();

        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                lobbyID = documentSnapshot.get("inLobby").toString();
                username = documentSnapshot.get("username").toString();

                firestore.collection("Lobbies").document(lobbyID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String lobbyTitle = documentSnapshot.get("title").toString();
                        title_tv.setText(lobbyTitle);
                    }
                });
            }
        });





        //recycler setting up


        a = new ArrayList();
        chatRecyclerAdapter = new ChatRecyclerAdapter(a);
        chat_recycler.setLayoutManager(new LinearLayoutManager(this));
        chat_recycler.setAdapter(chatRecyclerAdapter);
        chat_recycler.setHasFixedSize(true);
        chat_recycler.setNestedScrollingEnabled(false);
        //((LinearLayoutManager)chat_recycler.getLayoutManager()).setStackFromEnd(true);

        chat_recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if ( i3 < i7 && chatRecyclerAdapter.getItemCount()!=0) {
                    chat_recycler.smoothScrollToPosition(chatRecyclerAdapter.getItemCount()-1);
                }
            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String msg = chat_et.getText().toString();

            final Map<String, Object> chat = new HashMap<>();
            chat.put("senderID", senderID);
            chat.put("username", username);
            chat.put("message", msg);
            chat.put("timestamp", FieldValue.serverTimestamp());

            chat_et.setText("");

            firestore.collection("Lobbies").document(lobbyID)
                    .collection("Chat").document().set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
        });



        

        firestore.collection("Lobbies").document(MainActivity.inLobby).collection("Chat").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    ChatPostModel chatPost = doc.getDocument().toObject(ChatPostModel.class);

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        a.add(chatPost);
                        chatRecyclerAdapter.notifyDataSetChanged();
                        chat_recycler.smoothScrollToPosition(a.size()-1);

                    }

                    if (doc.getType() == DocumentChange.Type.REMOVED){

                    }
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                lobbyID = documentSnapshot.get("inLobby").toString();
            }
        });
    }

    public void exitLobby(final View view){

        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setTitle("Exit lobby?")
                .setMessage("You will end the session and will not be able to return.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        final ProgressDialog exitLoading = new ProgressDialog(LobbyActivity_temp.this, R.style.dialogTheme); // this = YourActivity
                        exitLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        exitLoading.setIndeterminate(true);
                        exitLoading.setMessage("Exiting lobby...");
                        exitLoading.setCanceledOnTouchOutside(false);
                        exitLoading.show();

                        firestore.collection("Users").document(firebaseUser.getUid()).update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                firestore.collection("Lobbies").document(firebaseUser.getUid()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        final Map<String, Object> leaveChat = new HashMap<>();
                                        leaveChat.put("senderID", "bot");
                                        leaveChat.put("username", firebaseUser.getUid());
                                        leaveChat.put("message", "nf_leave");
                                        leaveChat.put("timestamp", FieldValue.serverTimestamp());

                                        firestore.collection("Lobbies").document(lobbyID)
                                                .collection("Chat").document().set(leaveChat).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                lobby_back(null);
                                                exitLoading.dismiss();
                                            }
                                        });
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



}
