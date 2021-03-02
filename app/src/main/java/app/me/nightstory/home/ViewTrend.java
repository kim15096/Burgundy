package app.me.nightstory.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import app.me.nightstory.R;
import app.me.nightstory.lobby.LobbyPostModel;
import app.me.nightstory.lobby.RoomRecyclerAdapter;
import app.me.nightstory.lobby.RoomViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewTrend extends Fragment {

    private RecyclerView rv_trend;
    private FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolder> mAdapter;
    private Query LobbyQuery;


    public ViewTrend() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vp_trend, container, false);


        rv_trend = view.findViewById(R.id.rv_trend);

        rv_trend.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_trend.setNestedScrollingEnabled(false);


        LobbyQuery = FirebaseFirestore.getInstance()
                .collection("Lobbies")
                .whereGreaterThanOrEqualTo("cur_views", 4)
                .whereLessThanOrEqualTo("cur_views", 7)
                .orderBy("cur_views")
                .orderBy("timestamp");

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<LobbyPostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<LobbyPostModel>()
                .setQuery(LobbyQuery, LobbyPostModel.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new RoomRecyclerAdapter(recyclerOptions);


        rv_trend.setAdapter(mAdapter);



        // Inflate the layout for this fragment
        return view;
    }

}
