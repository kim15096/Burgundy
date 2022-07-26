package app.me.nightstory.lobby;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class LobbyActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference userRef, lobbyRef;
    private String hostID, fireBaseID;
    private TextView lobby_title, host_name, cur_views, lobby_desc;
    private AlertDialog alertDialog;
    private EditText chat_et;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateFormatHour = new SimpleDateFormat("aa hh:mm");

    private RecyclerView story_recycler, comments_recycler;
    private StoryRecyclerAdapter storyRecyclerAdapter;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<StoryPostModel> a;
    private List<CommentsPostModel> comments;

    private MediaPlayer mediaPlayer;
    private LottieResult<LottieComposition> lottieResult1;
    private ConstraintLayout emojiPopup;
    private LottieResult<LottieComposition> lottieResult2;

    private ImageView sendBtn;
    private LottieAnimationView ch1;
    private CircularImageView pp1;
    private Boolean emojipop = false;
    private Long cur_view;
    private CardView chatCardView, viewCardView;

    public static String lobbyIDLocal = "";


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this,"ko");
        setContentView(R.layout.activity_lobby);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fireBaseID = firebaseUser.getUid();


        userRef = firestore.collection("Users").document(fireBaseID);
        lobbyRef = firestore.collection("Lobbies").document(MainActivity.inLobbyID);

        lobbyIDLocal = MainActivity.inLobbyID;


        viewCardView = findViewById(R.id.viewLobbyCard);
        lobby_title = findViewById(R.id.lobby_title);
        lobby_desc = findViewById(R.id.lobby_desc);
        host_name = findViewById(R.id.host_name);
        sendBtn = findViewById(R.id.viewer_sendBtn);
        chat_et = findViewById(R.id.chat_view_et);
        cur_views = findViewById(R.id.cur_views_tv);

        lobbyRef.addSnapshotListener(LobbyActivity.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    String lobbyTitle = value.get("title").toString();
                    String hostName = value.get("hostName").toString();
                    String hostIDa = value.get("hostID").toString();
                    String active = value.get("active").toString();
                    String lobbyDesc = value.get("desc").toString();
                    hostID = value.get("hostID").toString();
                    cur_view = value.getLong("cur_views");


                    if (!hostID.equals(fireBaseID) && active.equals("false")) {

                        userRef.update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LobbyActivity.this, R.string.main_closedNotice, Toast.LENGTH_SHORT).show();
                                LobbyActivity.this.finish();

                            }
                        });
                    }
                    host_name.setText(hostName);
                    lobby_title.setText(lobbyTitle);
                    lobby_desc.setText(lobbyDesc);
                    cur_views.setText(cur_view.toString());


                }
            });

        story_recycler = findViewById(R.id.story_recycler);

        // storyboard recycler

        a = new ArrayList();
        storyRecyclerAdapter = new StoryRecyclerAdapter(a);
        story_recycler.setLayoutManager(new LinearLayoutManager(this));
        story_recycler.setAdapter(storyRecyclerAdapter);
        story_recycler.setHasFixedSize(true);
        story_recycler.setNestedScrollingEnabled(false);
        //((LinearLayoutManager)chat_recycler.getLayoutManager()).setStackFromEnd(true);

        story_recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if ( i3 < i7 && storyRecyclerAdapter.getItemCount()!=0) {
                    story_recycler.smoothScrollToPosition(storyRecyclerAdapter.getItemCount()-1);
                }
            }
        });

        lobbyRef.collection("Story").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    StoryPostModel chatPost = doc.getDocument().toObject(StoryPostModel.class);

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        a.add(chatPost);
                        storyRecyclerAdapter.notifyDataSetChanged();
                        story_recycler.smoothScrollToPosition(a.size()-1);

                    }
                }
            }
        });



        // comments recycler


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chat_et.getText().toString().equals("")){
                    String msg = chat_et.getText().toString();

                    final Map<String, Object> story = new HashMap<>();
                    story.put("text", msg);
                    story.put("chatUsername", MainActivity.nickname);
                    story.put("userID", fireBaseID);
                    story.put("ppURL", MainActivity.myPpURL);
                    story.put("timestamp", FieldValue.serverTimestamp());

                    chat_et.setText("");

                    lobbyRef.collection("Story").document().set(story).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });

                }}
        });


    }

    public void lobby_back(View view){
        onBackPressed();

    }

    @Override
    public void onBackPressed() {
        if (hostID.equals(fireBaseID)) {

            new AlertDialog.Builder(this, R.style.dialogTheme)
                    .setTitle(R.string.lobby_exit)
                    .setPositiveButton(R.string.lobby_exitBtn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            lobbyRef.update("active", "false").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    userRef.update("inLobby", "");
                                            LobbyActivity.this.finish();

                                }
                            });
                        }
                    })

                    .setNegativeButton(R.string.lobby_stay, null)
                    .show();

        } else {
            new AlertDialog.Builder(this, R.style.dialogTheme)
                    .setTitle(R.string.lobby_exit)
                    .setPositiveButton(R.string.lobby_exitBtn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            userRef.update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    lobbyRef.update("cur_views", FieldValue.increment(-1));
                                    LobbyActivity.super.onBackPressed();
                                }
                            });
                        }
                    })

                    .setNegativeButton(R.string.lobby_stay, null)
                    .show();
        }
    }
}
