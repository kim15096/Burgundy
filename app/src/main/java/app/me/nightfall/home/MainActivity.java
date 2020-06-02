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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

import app.me.nightfall.R;

import app.me.nightfall.ProfileActivity;
import app.me.nightfall.lobby.AddLobbyActivity;
import app.me.nightfall.lobby.LobbiesRecyclerAdapter;
import app.me.nightfall.lobby.LobbyPostModel;
import app.me.nightfall.lobby.LobbyFrag;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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

        toLobbyBtn = findViewById(R.id.tolobby_btn);
        account_btn = findViewById(R.id.account_btn);
        create_fab = findViewById(R.id.create_fab);
        viewPager = findViewById(R.id.home_viewpg);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.home_tab);
        tabLayout.setupWithViewPager(viewPager);
        lobby_recycler = findViewById(R.id.lobbies_recycler);

        db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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

    @Override
    protected void onResume() {
        super.onResume();

        if (inLobby) {
            create_fab.hide();
            toLobbyBtn.setVisibility(View.VISIBLE);
        }
        if (openLobby){
            showLobby(null);
            openLobby = false;
        }
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





    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new NormalLobbies(); //ChildFragment2 at position 1
                case 1:
                    return new CategoriesFrag();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Lobbies";

                case 1:
                    return "Categories";

            }
            return null;
        }
    }


}
