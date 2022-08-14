package app.burgundy.com.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.constraintlayout.widget.ConstraintLayout;
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
import app.burgundy.com.lobby.RoomRecyclerAdapter;
import app.burgundy.com.lobby.RoomViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewHot extends Fragment {

    private RecyclerView rv_live;
    private FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolder> mAdapter;
    private Query LobbyQuery;
    private ConstraintLayout mainToolbar;
    private VideoView bgVid;


    public ViewHot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.vp_hot, container, false);

        mainToolbar = getActivity().findViewById(R.id.mainHead);

        rv_live = view.findViewById(R.id.rv_live);


        rv_live.setLayoutManager(new LinearLayoutManager(getActivity()));

        LobbyQuery = FirebaseFirestore.getInstance()
                .collection("Lobbies")
                .orderBy("tot_views", Query.Direction.DESCENDING);

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<LobbyPostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<LobbyPostModel>()
                .setQuery(LobbyQuery, LobbyPostModel.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new RoomRecyclerAdapter(recyclerOptions);


        rv_live.setAdapter(mAdapter);


// apply spacing

        /*rv_live.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
            private static final int HIDE_THRESHOLD = 50;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    controlsVisible = false;
                    mainToolbar.animate().setInterpolator(new LinearInterpolator()).translationY(-150).setDuration(200);
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    controlsVisible = true;
                    mainToolbar.animate().setInterpolator(new LinearInterpolator()).translationY(0).setDuration(200);

                    scrolledDistance = 0;
                }

                if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
                    scrolledDistance += dy;
                }

            }

        });*/


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

