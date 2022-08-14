package app.burgundy.com.lobby;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieResult;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.burgundy.com.R;
import app.burgundy.com.home.MainActivity;

public class LobbyActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference userRef, lobbyRef;
    private String fireBaseID, URL;
    public static String hostID;
    private TextView lobby_title, host_name, cur_views, lobby_desc;
    private AlertDialog alertDialog;
    private EditText chat_et;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat dateFormatHour = new SimpleDateFormat("aa hh:mm");

    private RecyclerView story_recycler, comments_recycler;
    private CommentsRecyclerAdapter commentsRecyclerAdapter;
    private List<StoryPostModel> a;
    private List<CommentsPostModel> comments;
    private FirestoreRecyclerAdapter<StoryPostModel, StoryViewHolder> storyRecyclerAdapter;

    private MediaPlayer mediaPlayer;
    private LottieResult<LottieComposition> lottieResult1;
    private ConstraintLayout emojiPopup;
    private LottieResult<LottieComposition> lottieResult2;

    private ImageView sendBtn, lobbyPhoto;
    private LottieAnimationView empty_anim;
    private CircularImageView pp1;
    private Boolean emojipop = false;
    private Long cur_view;
    private CardView chatCardView, viewCardView, sendCardView;
    private AlertDialog photoDialog;
    private CircularImageView hostPP;

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

        photoDialog = new AlertDialog.Builder(LobbyActivity.this).create();
        View v = getLayoutInflater().inflate(R.layout.lobby_photo, null);
        photoDialog.setView(v);

        lobbyPhoto = v.findViewById(R.id.lobbyPhoto);

        empty_anim = findViewById(R.id.anim_empty);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        fireBaseID = firebaseUser.getUid();

        userRef = firestore.collection("Users").document(fireBaseID);
        lobbyRef = firestore.collection("Lobbies").document(MainActivity.inLobbyID);

        lobbyIDLocal = MainActivity.inLobbyID;
        lobby_title = findViewById(R.id.lobby_title);
        sendCardView = findViewById(R.id.lobbySendCV);
        lobby_desc = findViewById(R.id.lobby_desc);
        sendBtn = findViewById(R.id.viewer_sendBtn);
        chat_et = findViewById(R.id.chat_view_et);
        cur_views = findViewById(R.id.cur_views_tv);

        host_name = findViewById(R.id.lobby_hostNameText);
        hostPP = findViewById(R.id.lobby_hostPP);

        chat_et.setHorizontallyScrolling(false);
        chat_et.setMaxLines(5);



        lobbyRef.addSnapshotListener(LobbyActivity.this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                    String lobbyTitle = value.get("title").toString();
                    String hostName = value.get("hostName").toString();
                    String hostIDa = value.get("hostID").toString();
                    URL = value.get("imageURL").toString();
                    String active = value.get("active").toString();
                    String lobbyDesc = value.get("desc").toString();
                    hostID = value.get("hostID").toString();
                    lobbyTitle = "제 직업은 " + lobbyTitle + "입니다. 뭐든지 물어봐요!";

                    lobby_title.setText(lobbyTitle);
                    lobby_desc.setText(lobbyDesc);
                    host_name.setText(hostName);

                    firestore.collection("Users").document(hostIDa).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            String hostImgURL = documentSnapshot.get("imageURL").toString();
                            Glide.with(LobbyActivity.this).load(hostImgURL).centerCrop().into(hostPP);
                        }
                    });

                    if (fireBaseID.equals(hostIDa)){
                        sendCardView.setVisibility(View.GONE);
                    }
                    else{
                        sendCardView.setVisibility(View.VISIBLE);
                    }



                }
            });




        story_recycler = findViewById(R.id.story_recycler);
        story_recycler.setLayoutManager(new LinearLayoutManager(this));

        // storyboard recycler


        Query storyQuery = FirebaseFirestore.getInstance()
                .collection("Lobbies").document(MainActivity.inLobbyID).collection("Story")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<StoryPostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<StoryPostModel>()
                .setQuery(storyQuery, StoryPostModel.class)
                .setLifecycleOwner(this)
                .build();

        storyRecyclerAdapter = new StoryRecyclerAdapterFB(recyclerOptions);


        story_recycler.setAdapter(storyRecyclerAdapter);


        story_recycler.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if ( i3 < i7 && storyRecyclerAdapter.getItemCount()!=0) {
                    story_recycler.smoothScrollToPosition(storyRecyclerAdapter.getItemCount()-1);
                }
            }
        });

        /*lobbyRef.collection("Story").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        });*/



        // comments recycler


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!chat_et.getText().toString().equals("")){
                    String msg = chat_et.getText().toString();

                    final String id = lobbyRef.collection("Story").document().getId();

                    final Map<String, Object> story = new HashMap<>();
                    story.put("text", msg);
                    story.put("chatUsername", MainActivity.nickname);
                    story.put("userID", fireBaseID);
                    story.put("response", "");
                    story.put("commentID", id);
                    story.put("ppURL", MainActivity.myPpURL);
                    story.put("timestamp", FieldValue.serverTimestamp());

                    chat_et.setText("");

                    lobbyRef.collection("Story").document(id).set(story).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            story_recycler.smoothScrollToPosition(0);
                        }
                    });

                }}
        });


    }

    public void lobby_back(View view){
        onBackPressed();

    }

    public void showPhoto(View view){
        Glide.with(LobbyActivity.this).load(URL).centerCrop().into(lobbyPhoto);
        photoDialog.show();
        photoDialog.getWindow().setLayout(700,700);
    }

    @Override
    public void onBackPressed() {
        LobbyActivity.super.onBackPressed();
    }
}
