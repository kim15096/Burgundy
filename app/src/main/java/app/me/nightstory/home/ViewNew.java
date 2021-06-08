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
import app.me.nightstory.article.ArticlePostModel;
import app.me.nightstory.article.ArticleRecyclerAdapter;
import app.me.nightstory.article.ArticleViewHolder;
import app.me.nightstory.lobby.LobbyPostModel;
import app.me.nightstory.lobby.RoomRecyclerAdapter;
import app.me.nightstory.lobby.RoomViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNew extends Fragment {

    private RecyclerView rv_fresh;
    private FirestoreRecyclerAdapter<ArticlePostModel, ArticleViewHolder> recyclerAdapter;
    private Query ArticleQuery;

    public ViewNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.vp_new, container, false);

        rv_fresh = view.findViewById(R.id.rv_fresh);
        rv_fresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_fresh.setNestedScrollingEnabled(false);


        ArticleQuery = FirebaseFirestore.getInstance()
                .collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<ArticlePostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<ArticlePostModel>()
                .setQuery(ArticleQuery, ArticlePostModel.class)
                .setLifecycleOwner(this)
                .build();

        recyclerAdapter = new ArticleRecyclerAdapter(recyclerOptions);


        rv_fresh.setAdapter(recyclerAdapter);

        return view;
    }

}
