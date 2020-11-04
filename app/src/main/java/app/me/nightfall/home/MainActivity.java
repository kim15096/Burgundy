package app.me.nightfall.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnSuccessListener;
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
import app.me.nightfall.lobby.LobbyActivity;
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
    private TextView username;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    public static String nickname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        username = findViewById(R.id.username);
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

        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.textColorGray);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inLobbyID = documentSnapshot.get("inLobby").toString();

                if (!inLobbyID.equals("")){
                    Intent mainIntent = new Intent(MainActivity.this, LobbyActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                }
            }
        });

        db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                nickname = documentSnapshot.get("username").toString();
                username.setText("Welcome " + nickname + "!");


            }
        });

        create_fab = findViewById(R.id.create_fab);

        indexList =new ArrayList<>();



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

   public void signOut(View view){

       new AlertDialog.Builder(this, R.style.dialogTheme)
               .setTitle("Rename your sparkling?")
               .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       firebaseAuth.signOut();
                       Intent mainIntent = new Intent(MainActivity.this, Login.class);
                       MainActivity.this.startActivity(mainIntent);
                       MainActivity.this.finish();
                   }
               })

               .setNegativeButton("No!", null)
               .show();
   }

   }
