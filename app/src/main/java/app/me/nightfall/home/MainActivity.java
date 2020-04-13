package app.me.nightfall.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import app.me.nightfall.R;

import app.me.nightfall.ProfileActivity;
import app.me.nightfall.login.Login;
import app.me.nightfall.login.LoginPage;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        account_btn = findViewById(R.id.account_btn);
        create_fab = findViewById(R.id.create_fab);
        viewPager = findViewById(R.id.home_viewpg);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.home_tab);
        tabLayout.setupWithViewPager(viewPager);
        lobby_recycler = findViewById(R.id.lobbies_recycler);

        //when floating action button is clicked
        create_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent mainIntent = new Intent(MainActivity.this, AddActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                }
                else {
                    Intent mainIntent = new Intent(MainActivity.this, Login.class);
                    MainActivity.this.startActivity(mainIntent);
                }

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







    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FeaturedLobbies(); //ChildFragment1 at position 0
                case 1:
                    return new NormalLobbies(); //ChildFragment2 at position 1
                case 2:
                    return new CategoriesFrag();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Featured";

                case 1:
                    return "Lobbies";

                case 2:
                    return "Categories";

            }
            return null;
        }
    }


}
