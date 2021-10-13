package app.me.nightstory.home;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import app.me.nightstory.R;
import app.me.nightstory.article.ArticlePostModel;
import app.me.nightstory.article.ArticleRecyclerAdapter;
import app.me.nightstory.article.ArticleViewHolder;
import app.me.nightstory.lobby.LobbyPostModel;
import app.me.nightstory.lobby.RoomRecyclerAdapter;
import app.me.nightstory.lobby.RoomViewHolder;
import app.me.nightstory.lobby.StoryRecyclerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostsActivity extends AppCompatActivity {

    private RecyclerView rv_myposts;
    private FirestoreRecyclerAdapter<ArticlePostModel, ArticleViewHolder> recyclerAdapter;
    private Query myPostsQuery;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this, "ko");
        setContentView(R.layout.activity_myposts);

        rv_myposts = findViewById(R.id.rv_myposts);

        rv_myposts.setLayoutManager(new LinearLayoutManager(this));
        rv_myposts.setHasFixedSize(true);
        rv_myposts.setNestedScrollingEnabled(false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        myPostsQuery = FirebaseFirestore.getInstance()
                .collection("Users").document(firebaseUser.getUid()).collection("MyPosts")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        // Set up Recycler Adapter
        FirestoreRecyclerOptions<ArticlePostModel> recyclerOptions = new FirestoreRecyclerOptions.Builder<ArticlePostModel>()
                .setQuery(myPostsQuery, ArticlePostModel.class)
                .setLifecycleOwner(this)
                .build();

        recyclerAdapter = new MyPostsRecyclerAdapter(recyclerOptions);

        rv_myposts.setAdapter(recyclerAdapter);

    }

    public void backBtnPressed(View view){
        super.onBackPressed();
    }

}
