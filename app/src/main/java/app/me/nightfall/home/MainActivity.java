package app.me.nightfall.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import app.me.nightfall.R;

import app.me.nightfall.profile.ProfileFrag;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ProfileFrag profileFrag = new ProfileFrag();
    private RecyclerView home_recycler;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private List<HomePostModel> post_list;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        viewPager = findViewById(R.id.home_viewpg);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout = findViewById(R.id.home_tab);
        tabLayout.setupWithViewPager(viewPager);



    }


}
