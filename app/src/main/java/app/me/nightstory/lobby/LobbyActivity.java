package app.me.nightstory.lobby;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
    private TextView lobby_title, host_name, cur_views, category_tv, sendBtn;
    private AlertDialog alertDialog;
    private EditText chat_et, chat_viewer_et;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat dateFormatHour = new SimpleDateFormat("aa hh:mm");

    private RecyclerView story_recycler, comments_recycler;
    private StoryRecyclerAdapter storyRecyclerAdapter;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<StoryPostModel> a;
    private List<CommentsPostModel> comments;

    private VideoView view;
    private MediaPlayer mediaPlayer;
    private LottieResult<LottieComposition> lottieResult1;
    private ConstraintLayout emojiPopup;
    private LottieResult<LottieComposition> lottieResult2;

    private ImageView viewer_SendBtn;
    private LottieAnimationView ch1;
    private Boolean emojipop = false;
    private Long cur_view;
    private CardView chatCardView, viewCardView;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLocale("ko");
        setContentView(R.layout.activity_lobby);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fireBaseID = firebaseUser.getUid();

        userRef = firestore.collection("Users").document(fireBaseID);
        lobbyRef = firestore.collection("Lobbies").document(MainActivity.inLobbyID);

        chatCardView = findViewById(R.id.hostLobbyCardView);
        viewCardView = findViewById(R.id.viewLobbyCard);
        emojiPopup = findViewById(R.id.emojiPanel);
        category_tv = findViewById(R.id.lobby_cat_tv);
        lobby_title = findViewById(R.id.lobby_title);
        host_name = findViewById(R.id.host_name);
        sendBtn = findViewById(R.id.msg_sendBtn);
        viewer_SendBtn = findViewById(R.id.viewer_sendBtn);
        chat_et = findViewById(R.id.chat_et);
        chat_viewer_et = findViewById(R.id.chat_view_et);
        ch1 = findViewById(R.id.ch1);
        cur_views = findViewById(R.id.cur_views_tv);

        lobbyRef.addSnapshotListener(LobbyActivity.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    String lobbyTitle = value.get("title").toString();
                    String hostName = value.get("hostName").toString();
                    String category = value.get("category").toString();
                    String emoji = value.get("emoji").toString();
                    String hostIDa = value.get("hostID").toString();

                    hostID = value.get("hostID").toString();
                    cur_view = value.getLong("cur_views");

                    if (hostIDa.equals("")) {

                        userRef.update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LobbyActivity.this, R.string.main_closedNotice, Toast.LENGTH_SHORT).show();
                                LobbyActivity.this.finish();

                            }
                        });
                    }

                    switch (emoji) {
                        case "Laugh":
                            AnimateLaugh();
                            break;

                        case "Tired":
                            AnimateTired();
                            break;

                        case "Admire":
                            AnimateAdmire();
                            break;

                        case "Sad":
                            AnimateSad();
                            break;
                    }

                    category_tv.setText(category);
                    host_name.setText(hostName);
                    lobby_title.setText(lobbyTitle);
                    cur_views.setText(cur_view.toString());

                    if (hostID.equals(firebaseUser.getUid())) {
                        chatCardView.setVisibility(View.VISIBLE);
                        viewCardView.setVisibility(View.INVISIBLE);
                    } else {
                        chatCardView.setVisibility(View.INVISIBLE);
                        viewCardView.setVisibility(View.VISIBLE);

                    }


                }
            });




        mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(5, 5);
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


        story_recycler = findViewById(R.id.story_recycler);
        comments_recycler = findViewById(R.id.viewers_rv);

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

        lobbyRef.collection("Story").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
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



        // comments recycler

        comments = new ArrayList();
        commentsRecyclerAdapter = new CommentsRecyclerAdapter(comments);
        comments_recycler.setLayoutManager(new LinearLayoutManager(this));
        comments_recycler.setAdapter(commentsRecyclerAdapter);
        comments_recycler.setHasFixedSize(true);
        comments_recycler.setNestedScrollingEnabled(false);


        lobbyRef.collection("Comments").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    CommentsPostModel commentPost = doc.getDocument().toObject(CommentsPostModel.class);

                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        comments.add(commentPost);
                        commentsRecyclerAdapter.notifyDataSetChanged();
                        comments_recycler.smoothScrollToPosition(comments.size()-1);

                        AnimateChat();
                    }
                }
            }
        });






        //host ver
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chat_et.getText().toString().equals("")){
                    String msg = chat_et.getText().toString();

                    final Map<String, Object> story = new HashMap<>();
                    story.put("text", msg);
                    story.put("timestamp", FieldValue.serverTimestamp());

                    chat_et.setText("");

                    lobbyRef.collection("Story").document().set(story).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });

                }}
        });

        //viewer ver

        viewer_SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chat_viewer_et.getText().toString().equals("")){
                    String comment = chat_viewer_et.getText().toString();

                    final Map<String, Object> comments = new HashMap<>();
                    comments.put("comment", comment);
                    comments.put("username", MainActivity.nickname);
                    comments.put("timestamp", FieldValue.serverTimestamp());

                    chat_viewer_et.setText("");

                    lobbyRef.collection("Comments").document().set(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                        }
                    });

                }}
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        mediaPlayer.start();


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

    public void removePop(View view){
        emojiPopup.setVisibility(View.GONE);
        emojipop = false;
    }

    public void AdmireBtn(View view){
        lobbyRef.update("emoji", "Admire");
    }
    public void LaughBtn(View view){
        lobbyRef.update("emoji", "Laugh");
    }
    public void SadBtn(View view){
        lobbyRef.update("emoji", "Sad");
    }
    public void TiredBtn(View view){
        lobbyRef.update("emoji", "Tired");
    }


    public void AnimateAdmire(){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_admire);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();

        lobbyRef.update("emoji", "");


    }

    public void AnimateTired(){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_tired);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();

        lobbyRef.update("emoji", "");

    }

    public void AnimateSad(){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_sad);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();

        lobbyRef.update("emoji", "");

    }

    public void AnimateLaugh(){

        ch1.clearAnimation();
        lottieResult2 = LottieCompositionFactory.fromRawResSync(getBaseContext(), R.raw.ch1_laugh);
        ch1.setComposition(lottieResult2.getValue());
        ch1.playAnimation();

        lobbyRef.update("emoji", "");

    }

    @Override
    public void onBackPressed() {
        if (hostID.equals(fireBaseID)) {

            new AlertDialog.Builder(this, R.style.dialogTheme)
                    .setTitle(R.string.lobby_exit)
                    .setPositiveButton(R.string.lobby_exitBtn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            lobbyRef.update("hostID", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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

                                    Long curView = cur_view - 1;
                                    lobbyRef.update("cur_views", curView);
                                    LobbyActivity.super.onBackPressed();
                                }
                            });
                        }
                    })

                    .setNegativeButton(R.string.lobby_stay, null)
                    .show();
        }
    }
    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
