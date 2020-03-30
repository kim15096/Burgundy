package app.me.nightfall.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import app.me.nightfall.R;

public class HomeFrag extends Fragment {

    private RecyclerView home_recycler;
    private HomeRecyclerAdapter homeRecyclerAdapter;
    private List<HomePostModel> post_list;

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

        home_recycler = view.findViewById(R.id.home_recycler);
        homeRecyclerAdapter = new HomeRecyclerAdapter(post_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        home_recycler.setLayoutManager(linearLayoutManager);
        home_recycler.setAdapter(homeRecyclerAdapter);














        return view;
    }
}
