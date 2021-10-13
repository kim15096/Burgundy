package app.me.nightstory.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import app.me.nightstory.R;
import app.me.nightstory.article.ArticlePostModel;
import app.me.nightstory.article.ArticleRecyclerAdapter;
import app.me.nightstory.article.ArticleRecyclerAdapterHot;
import app.me.nightstory.article.ArticleViewHolder;
import app.me.nightstory.lobby.LobbyPostModel;
import app.me.nightstory.lobby.RoomRecyclerAdapter;
import app.me.nightstory.lobby.RoomViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewHot extends Fragment {

    private RecyclerView rv_hot;
    private FirestoreRecyclerAdapter<ArticlePostModel, ArticleViewHolder> recyclerAdapter;
    private Query ArticleQuery;
    private ConstraintLayout mainToolbar;

    public ViewHot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.vp_hot, container, false);

        mainToolbar = getActivity().findViewById(R.id.mainHead);





        rv_hot = view.findViewById(R.id.rv_hot);
        rv_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_hot.setNestedScrollingEnabled(false);

        ((SimpleItemAnimator) rv_hot.getItemAnimator()).setSupportsChangeAnimations(false);


        ArticleQuery = FirebaseFirestore.getInstance()
                .collection("Posts")
                .whereGreaterThanOrEqualTo("likes", 50)
                .orderBy("likes", Query.Direction.DESCENDING);

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<ArticlePostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<ArticlePostModel>()
                .setQuery(ArticleQuery, ArticlePostModel.class)
                .setLifecycleOwner(this)
                .build();

        recyclerAdapter = new ArticleRecyclerAdapterHot(recyclerOptions);


        rv_hot.setAdapter(recyclerAdapter);

        rv_hot.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

}
