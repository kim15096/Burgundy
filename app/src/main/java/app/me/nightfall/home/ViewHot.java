package app.me.nightfall.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import app.me.nightfall.R;
import app.me.nightfall.lobby.LobbiesRecyclerAdapter;
import app.me.nightfall.lobby.LobbyPostModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewHot extends Fragment {

    private RecyclerView rv_hot;
    private LobbiesRecyclerAdapter recyclerAdapter;
    private List<LobbyPostModel> hot_list;
    private List<String> indexList;

    private FirebaseFirestore db;

    public ViewHot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        db = FirebaseFirestore.getInstance();

        indexList =new ArrayList<>();
        hot_list =new ArrayList<>();

        View view = inflater.inflate(R.layout.vp_hot, container, false);

        rv_hot = view.findViewById(R.id.rv_hot);
        recyclerAdapter = new LobbiesRecyclerAdapter(hot_list);
        rv_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_hot.setAdapter(recyclerAdapter);
        rv_hot.setHasFixedSize(true);
        rv_hot.setNestedScrollingEnabled(false);

        db.collection("Lobbies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    LobbyPostModel lobbyPost = doc.getDocument().toObject(LobbyPostModel.class);


                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        if (doc.getDocument().getLong("cur_views") > 10){
                            indexList.add(0, doc.getDocument().get("lobbyID").toString());
                            hot_list.add(0, lobbyPost);
                            recyclerAdapter.notifyItemInserted(0);
                            recyclerAdapter.notifyDataSetChanged();
                            rv_hot.scheduleLayoutAnimation();

                        }

                    }

                    if (doc.getType() == DocumentChange.Type.REMOVED){
                        Number index = indexList.indexOf(doc.getDocument().get("lobbyID").toString());
                        indexList.remove((int) index);
                        hot_list.remove((int)index);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;


    }

}
