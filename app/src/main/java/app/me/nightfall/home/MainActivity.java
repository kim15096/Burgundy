package app.me.nightfall.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.me.nightfall.R;

import app.me.nightfall.ProfileActivity;
import app.me.nightfall.lobby.AddLobbyActivity;
import app.me.nightfall.lobby.LobbiesRecyclerAdapter;
import app.me.nightfall.lobby.LobbyPostModel;
import app.me.nightfall.lobby.LobbyFrag;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton create_fab;
    private ImageView account_btn;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private List<LobbyPostModel> lobbyList;
    private RecyclerView lobby_recycler;
    private LobbiesRecyclerAdapter recyclerAdapter;
    private Button toLobbyBtn;
    public static Boolean inLobby = false;
    public static Boolean openLobby = false;
    private LobbyFrag LobbyFrag = new LobbyFrag();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        account_btn = findViewById(R.id.account_btn);
        create_fab = findViewById(R.id.create_fab);
        lobby_recycler = findViewById(R.id.lobbies_recycler);

        /*db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                inLobby = (Boolean) documentSnapshot.get("inLobby");

                if (inLobby){
                    create_fab.hide();
                    toLobbyBtn.setVisibility(View.VISIBLE);
                }
                else{
                    create_fab.show();
                    toLobbyBtn.setVisibility(View.INVISIBLE);
                }

            }
        });*/


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

                        lobbyList.add(0, lobbyPost);
                        recyclerAdapter.notifyItemInserted(0);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });



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
                Intent mainIntent = new Intent(MainActivity.this, ProfileActivity.class);
                MainActivity.this.startActivity(mainIntent);

            }
        });



    }
    
    public void showLobby(View view){

        Fragment lobbyFrag  = getSupportFragmentManager().findFragmentByTag("lobby");
        if (lobbyFrag == null){
            getSupportFragmentManager().beginTransaction().add(R.id.lobby_frag_container, LobbyFrag, "lobby").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(LobbyFrag).addToBackStack(null).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(LobbyFrag).addToBackStack(null).commit();
        }

        openLobby = false;


    }


   }
