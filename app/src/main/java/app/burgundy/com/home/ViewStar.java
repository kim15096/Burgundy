package app.burgundy.com.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import app.burgundy.com.R;
import app.burgundy.com.lobby.LobbyPostModel;
import app.burgundy.com.lobby.RoomRecyclerAdapterStar;
import app.burgundy.com.lobby.RoomViewHolderStar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewStar extends Fragment {

    private RecyclerView rv_star;
    private FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolderStar> mAdapter;
    private Query LobbyQuery;

    public ViewStar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.vp_star, container, false);


        rv_star = view.findViewById(R.id.rv_star);
        rv_star.setItemAnimator(null);


        rv_star.setLayoutManager(new LinearLayoutManager(getActivity()));

        LobbyQuery = FirebaseFirestore.getInstance()
                .collection("Lobbies")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<LobbyPostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<LobbyPostModel>()
                .setQuery(LobbyQuery, LobbyPostModel.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new RoomRecyclerAdapterStar(recyclerOptions);


        rv_star.setAdapter(mAdapter);


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

