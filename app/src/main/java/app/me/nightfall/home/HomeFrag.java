package app.me.nightfall.home;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import app.me.nightfall.R;

public class HomeFrag extends Fragment {

    private RecyclerView home_recycler;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private List<HomePostModel> post_list;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        post_list = new ArrayList<>();

        /*home_recycler = view.findViewById(R.id.home_recycler);
        homeRecyclerAdapter = new HomeRecyclerAdapter(post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        home_recycler.setLayoutManager(linearLayoutManager);
        home_recycler.setAdapter(homeRecyclerAdapter);*/


        viewPager = view.findViewById(R.id.home_viewpg);
        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        tabLayout = view.findViewById(R.id.home_tab);
        tabLayout.setupWithViewPager(viewPager);

        /*tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(getContext(), R.color.lightGray);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );*/

        viewPager.setCurrentItem(0);











        return view;
    }
}
