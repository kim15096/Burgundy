package app.me.nightfall.lobby;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
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

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieCompositionFactory;
import com.airbnb.lottie.LottieResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class LobbyActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String lobbyID, hostID, username, senderID;
    private TextView lobby_title, host_name, cur_views;
    private AlertDialog alertDialog;
    private EditText chat_et;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatHour = new SimpleDateFormat("aa hh:mm");

    private RecyclerView story_recycler;
    private StoryRecyclerAdapter storyRecyclerAdapter;
    private List<StoryPostModel> a;

    private VideoView view;
    private MediaPlayer mediaPlayer;
    private LottieResult<LottieComposition> lottieResult1;
    private ConstraintLayout emojiPopup;
    private LottieResult<LottieComposition> lottieResult2;

    private ImageView sendBtn;
    private LottieAnimationView ch1, ch2;
    private LottieComposition composition;
    private Boolean emojipop = false;
    private Long cur_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        emojiPopup = findViewById(R.id.emojiPanel);
        lobby_title = findViewById(R.id.lobby_title);
        host_name = findViewById(R.id.host_name);
        sendBtn = findViewById(R.id.msg_sendBtn);
        chat_et = findViewById(R.id.chat_et);
        ch1 = findViewById(R.id.ch1);
        cur_views = findViewById(R.id.cur_views_tv);



        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(10, 10);
        mediaPlayer.start();

        view = findViewById(R.id.lobby_bg_video);
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

        firestore.collection("Lobbies").document(MainActivity.inLobbyID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String lobbyTitle = documentSnapshot.get("title").toString();
                String hostName = documentSnapshot.get("hostName").toString();
                cur_view = documentSnapshot.getLong("cur_views");

                host_name.setText(hostName);
                lobby_title.setText(lobbyTitle);
                cur_views.setText(cur_view.toString());





            }
        });

        story_recycler = findViewById(R.id.story_recycler);

        ch1.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                lottieResult1 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_idle);
                ch1.clearAnimation();
                ch1.setComposition(lottieResult1.getValue());
                ch1.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

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


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chat_et.getText().toString().equals("")){
                    String msg = chat_et.getText().toString();

                    final Map<String, Object> story = new HashMap<>();
                    story.put("text", msg);
                    story.put("timestamp", FieldValue.serverTimestamp());

                    chat_et.setText("");

                    firestore.collection("Lobbies").document(lobbyID)
                            .collection("Chat").document().set(story).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });

                }}
        });




        firestore.collection("Lobbies").document(MainActivity.inLobbyID).collection("Chat").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    StoryPostModel chatPost = doc.getDocument().toObject(StoryPostModel.class);

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        a.add(chatPost);
                        storyRecyclerAdapter.notifyDataSetChanged();
                        story_recycler.smoothScrollToPosition(a.size()-1);

                        AnimateChat();
                    }
                }
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer.start();

        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                lobbyID = documentSnapshot.get("inLobby").toString();

            }
        });

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


    }

    @Override
    protected void onPause() {
        super.onPause();

        mediaPlayer.pause();
    }

    /*public void exitLobby(final View view){

        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setTitle("Exit lobby?")
                .setMessage("You will end the session and will not be able to return.")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        final ProgressDialog exitLoading = new ProgressDialog(LobbyActivity.this, R.style.dialogTheme); // this = YourActivity
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

                                        firestore.collection("Lobbies").document(lobbyID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                Long count = documentSnapshot.getLong("count");
                                                String p1 = documentSnapshot.get("p1_ID").toString();
                                                String p2 = documentSnapshot.get("p2_ID").toString();
                                                String p3 = documentSnapshot.get("p3_ID").toString();
                                                String p4 = documentSnapshot.get("p3_ID").toString();


                                                String username = firebaseUser.getDisplayName();
                                                if (p1.equals(username)){
                                                    firestore.collection("Lobbies").document(lobbyID).update("p1_ID", "");
                                                }
                                                else if (p2.equals(username)){
                                                    firestore.collection("Lobbies").document(lobbyID).update("p2_ID", "");
                                                }
                                                else if (p3.equals(username)){
                                                    firestore.collection("Lobbies").document(lobbyID).update("p3_ID", "");
                                                }

                                                else if (p4.equals(username)){
                                                    firestore.collection("Lobbies").document(lobbyID).update("p4_ID", "");
                                                }

                                                if (count >= 1) {
                                                    count = count - 1;
                                                    firestore.collection("Lobbies").document(lobbyID).update("count", count);
                                                }
                                                final Map<String, Object> leaveChat = new HashMap<>();
                                                leaveChat.put("senderID", "bot");
                                                leaveChat.put("username", firebaseUser.getUid());
                                                leaveChat.put("message", "left");
                                                leaveChat.put("position", LobbiesRecyclerAdapter.playerPos);
                                                leaveChat.put("timestamp", FieldValue.serverTimestamp());

                                                firestore.collection("Lobbies").document(lobbyID)
                                                        .collection("Chat").document().set(leaveChat).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        quitLobby();
                                                        exitLoading.dismiss();
                                                    }
                                                });
                                            }});
                                    }
                                });
                            }
                        });
                    }
                })

                .setNegativeButton("Stay", null)
                .show();



    }*/

    public void lobby_back(View view){
        onBackPressed();

    }

    public void emojiBtn(View view){
        if (!emojipop){
            emojiPopup.setVisibility(View.VISIBLE);
            emojipop = true;
        }
        else {
            emojiPopup.setVisibility(View.GONE);
            emojipop = false;
        }
    }


    public void AnimateChat(){

                ch1.clearAnimation();
                lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_talk);
                ch1.setComposition(lottieResult2.getValue());
                ch1.playAnimation();


    }

    public void AnimateAdmire(View view){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_admire);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();


    }

    public void AnimateTired(View view){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_tired);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();
    }

    public void AnimateSad(View view){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_sad);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();
    }

    public void AnimateLaugh(View view){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_laugh);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setTitle("Exit lobby?")
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firestore.collection("Users").document(firebaseUser.getUid()).update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Long curView = cur_view - 1;
                                firestore.collection("Lobbies").document(MainActivity.inLobbyID).update("cur_views", curView);
                                LobbyActivity.super.onBackPressed();
                            }
                        });
                    }
                })

                .setNegativeButton("Stay", null)
                .show();
    }
}
