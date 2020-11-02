package app.me.nightfall.home;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import app.me.nightfall.R;

import app.me.nightfall.lobby.AddLobbyActivity;
import app.me.nightfall.lobby.LobbiesRecyclerAdapter;
import app.me.nightfall.lobby.LobbyPostModel;
import app.me.nightfall.login.Login;

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
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static String inLobbyID = "";
    private ViewPagerAdapter viewPagerAdapter;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setInlineLabel(true);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(0).setText("Hot");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_trending);
        tabLayout.getTabAt(1).setText("Trending");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_new);
        tabLayout.getTabAt(2).setText("Fresh");
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorGray),
                getResources().getColor(R.color.colorPrimary));



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        create_fab = findViewById(R.id.create_fab);

        indexList =new ArrayList<>();

       db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {


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

       /* recyclerAdapter = new LobbiesRecyclerAdapter(lobbyList, this);
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
        }*/



        //when floating action button is clicked
        create_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, AddLobbyActivity.class);
                MainActivity.this.startActivity(mainIntent);

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

   public void signOut(View view){
       firebaseAuth.signOut();
       Intent mainIntent = new Intent(MainActivity.this, Login.class);
       MainActivity.this.startActivity(mainIntent);
   }

}
