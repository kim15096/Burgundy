package app.me.nightstory.home;


import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
public class ViewLive extends Fragment {

    private RecyclerView rv_live;
    private FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolder> mAdapter;
    private Query LobbyQuery;
    private ConstraintLayout mainToolbar;
    private VideoView bgVid;


    public ViewLive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.vp_live, container, false);

        mainToolbar = getActivity().findViewById(R.id.mainHead);

        rv_live = view.findViewById(R.id.rv_live);

        rv_live.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        bgVid = view.findViewById(R.id.videoBG);

        LobbyQuery = FirebaseFirestore.getInstance()
                .collection("Lobbies")
                .orderBy("cur_views")
                .orderBy("timestamp");

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<LobbyPostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<LobbyPostModel>()
                .setQuery(LobbyQuery, LobbyPostModel.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new RoomRecyclerAdapter(recyclerOptions);


        rv_live.setAdapter(mAdapter);

        final int spacing = 10;

// apply spacing
        rv_live.setPadding(spacing, 175, spacing, spacing);
        rv_live.setClipToPadding(false);
        rv_live.setClipChildren(false);
        rv_live.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(spacing, spacing, spacing, spacing);
            }
        });

        rv_live.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    mainToolbar.animate().setInterpolator(new LinearInterpolator()).translationY(-140).setDuration(200);
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

        });


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.login_bg;

        bgVid.setVideoURI(Uri.parse(path));
        bgVid.start();

        bgVid.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}

