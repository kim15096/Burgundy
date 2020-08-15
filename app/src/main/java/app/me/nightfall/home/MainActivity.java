package app.me.nightfall.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import app.me.nightfall.R;

import app.me.nightfall.lobby.AddLobbyActivity;
import app.me.nightfall.lobby.LobbiesRecyclerAdapter;
import app.me.nightfall.lobby.LobbyActivity;
import app.me.nightfall.lobby.LobbyPostModel;
import app.me.nightfall.login.Splash;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton create_fab;
    private ImageView account_btn, shopBtn;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<LobbyPostModel> lobbyList;
    private List<String> indexList;
    private RecyclerView lobby_recycler;
    private LobbiesRecyclerAdapter recyclerAdapter;
    private Button toLobbyBtn, category;

    public static String inLobbyID = "";
    private SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        shopBtn = findViewById(R.id.shop_btn);



        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    inLobbyID = documentSnapshot.get("inLobby").toString();

                    if (inLobbyID != ""){
                        create_fab.hide();
                        toLobbyBtn.setVisibility(View.VISIBLE);
                    }
                    else{
                        create_fab.show();
                        toLobbyBtn.setVisibility(View.INVISIBLE);
                    }


                }
            }
        });


        account_btn = findViewById(R.id.account_btn);
        create_fab = findViewById(R.id.create_fab);
        lobby_recycler = findViewById(R.id.lobbies_recycler);
        category = findViewById(R.id.categoryBtn);
        toLobbyBtn = findViewById(R.id.back2lobbyBtn);

        indexList =new ArrayList<>();

       db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


            }
        });

        slidingUpPanelLayout = findViewById(R.id.sliding_layout);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING){
                    shopBtn.setVisibility(View.INVISIBLE);
                    account_btn.setVisibility(View.INVISIBLE);

                }
                else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED){
                    account_btn.setVisibility(View.INVISIBLE);
                    shopBtn.setVisibility(View.INVISIBLE);

                }
                else {
                    account_btn.setVisibility(View.VISIBLE);
                    shopBtn.setVisibility(View.VISIBLE);
                }
            }
        });


        VideoView view = findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.login_bg;
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


        lobbyList = new ArrayList<>();

        recyclerAdapter = new LobbiesRecyclerAdapter(lobbyList, this);
        lobby_recycler.setLayoutManager(new LinearLayoutManager(this));
        lobby_recycler.setAdapter(recyclerAdapter);
        lobby_recycler.setHasFixedSize(true);
        lobby_recycler.setNestedScrollingEnabled(false);

        db.collection("Lobbies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    LobbyPostModel lobbyPost = doc.getDocument().toObject(LobbyPostModel.class);


                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        indexList.add(0, doc.getDocument().get("p1_ID").toString());
                        lobbyList.add(0, lobbyPost);
                        recyclerAdapter.notifyItemInserted(0);
                        recyclerAdapter.notifyDataSetChanged();
                        lobby_recycler.scheduleLayoutAnimation();


                    }

                    if (doc.getType() == DocumentChange.Type.REMOVED){
                        Number index = indexList.indexOf(doc.getDocument().get("p1_ID").toString());
                        indexList.remove((int) index);
                        lobbyList.remove((int)index);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        if (!inLobbyID.equals("")) {

            db.collection("Lobbies").document(inLobbyID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                }
            });
        }



        //when floating action button is clicked
        create_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, AddLobbyActivity.class);
                MainActivity.this.startActivity(mainIntent);

            }
        });

        account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this, R.style.dialogTheme)
                        .setTitle("Options")
                        .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                Intent mainIntent = new Intent(MainActivity.this, Splash.class);
                                MainActivity.this.startActivity(mainIntent);
                                finishAffinity();
                            }
                        })
                        .setNegativeButton("Stay", null)
                        .show();
            }
        });

        toLobbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtoLobby();
            }
        });


        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED){
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

                }

                else if (slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

                }
            }
        });

        shopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();

        VideoView view = findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.login_bg;
        view.setVideoURI(Uri.parse(path));
        view.start();

        view.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }


   /* public void showLobby(View view){

        Fragment lobbyFrag  = getSupportFragmentManager().findFragmentByTag("lobby");
        if (lobbyFrag == null){

            LobbyFrag newLobby = new LobbyFrag();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.lobby_frag_container, newLobby, "lobby").replace(R.id.lobby_frag_container, newLobby).addToBackStack(null).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(lobbyFrag).addToBackStack(null).commit();
        }

        openLobby = false;


    }*/

   public void backtoLobby(){
       Intent mainIntent = new Intent(MainActivity.this, LobbyActivity.class);
       mainIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
       startActivityIfNeeded(mainIntent, 0);




   }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED || slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.DRAGGING) {
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return;
        }

        super.onBackPressed();


    }
}
