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
public class ViewHot extends Fragment {

    private RecyclerView rv_hot;
    private FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolder> mAdapter;
    private Query LobbyQuery;

    private FirebaseFirestore db;

    public ViewHot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.vp_hot, container, false);

        rv_hot = view.findViewById(R.id.rv_hot);

        rv_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_hot.setNestedScrollingEnabled(false);


        LobbyQuery = FirebaseFirestore.getInstance()
                .collection("Lobbies")
                .whereGreaterThan("cur_views", 10)
                .orderBy("cur_views")
                .orderBy("timestamp");

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<LobbyPostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<LobbyPostModel>()
                .setQuery(LobbyQuery, LobbyPostModel.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new RoomRecyclerAdapter(recyclerOptions);


        rv_hot.setAdapter(mAdapter);


        return view;


    }


}

