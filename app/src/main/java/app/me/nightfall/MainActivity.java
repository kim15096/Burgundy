package app.me.nightfall;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import app.me.nightfall.categories.CategoryFrag;
import app.me.nightfall.home.HomeFrag;
import app.me.nightfall.lobby.LobbyFrag;
import app.me.nightfall.addpost.PostFrag;
import app.me.nightfall.profile.ProfileFrag;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFrag homeFrag = new HomeFrag();
    private ProfileFrag profileFrag = new ProfileFrag();
    private CategoryFrag categoryFrag = new CategoryFrag();
    private PostFrag postFrag = new PostFrag();
    private LobbyFrag lobbyFrag = new LobbyFrag();

    private  final Fragment[] currentFragment = {homeFrag};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);


        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().add(R.id.main_frag_container, postFrag, "post").hide(postFrag).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frag_container, lobbyFrag, "lobby").hide(homeFrag).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frag_container, categoryFrag, "category").hide(categoryFrag).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frag_container, profileFrag, "profile").hide(postFrag).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frag_container, homeFrag, "home").show(homeFrag).commit();


        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
            // change to whichever id should be default
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(homeFrag).commit();
                        currentFragment[0] = homeFrag;

                        //bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);

                        return true;


                    case R.id.nav_categories:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(categoryFrag).commit();
                        currentFragment[0] = categoryFrag;

                        return true;

                    case R.id.nav_add:
                        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this);

                        View sheetView = getLayoutInflater().inflate(R.layout.bottom_add, null);

                        dialog.setContentView(sheetView);

                        dialog.show();

                        return true;

                    case R.id.nav_lobby:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(lobbyFrag).commit();
                        currentFragment[0] = lobbyFrag;
                        return true;

                    case R.id.nav_account:
                        getSupportFragmentManager().beginTransaction().hide(currentFragment[0]).show(profileFrag).commit();
                        currentFragment[0] = profileFrag;

                        //bottomNavigationView.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(150);

                        return true;

                }
                return false;
            }
        });


    }


}
