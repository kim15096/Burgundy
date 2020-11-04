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
public class ViewNew extends Fragment {

    private RecyclerView rv_fresh;
    private LobbiesRecyclerAdapter recyclerAdapter;
    private List<LobbyPostModel> fresh_list;
    private List<String> indexList;

    private FirebaseFirestore db;

    public ViewNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.vp_new, container, false);

        db = FirebaseFirestore.getInstance();

        indexList =new ArrayList<>();
        fresh_list =new ArrayList<>();


        rv_fresh = view.findViewById(R.id.rv_fresh);
        recyclerAdapter = new LobbiesRecyclerAdapter(fresh_list);
        rv_fresh.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_fresh.setAdapter(recyclerAdapter);
        rv_fresh.setHasFixedSize(true);
        rv_fresh.setNestedScrollingEnabled(false);

        db.collection("Lobbies").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                    LobbyPostModel lobbyPost = doc.getDocument().toObject(LobbyPostModel.class);


                    if (doc.getType() == DocumentChange.Type.ADDED) {

                            indexList.add(0, doc.getDocument().get("lobbyID").toString());
                            fresh_list.add(0, lobbyPost);
                            recyclerAdapter.notifyItemInserted(0);
                            recyclerAdapter.notifyDataSetChanged();
                            rv_fresh.scheduleLayoutAnimation();


                    }

                    if (doc.getType() == DocumentChange.Type.REMOVED){
                        Number index = indexList.indexOf(doc.getDocument().get("lobbyID").toString());
                        indexList.remove((int) index);
                        fresh_list.remove((int)index);
                        recyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        return view;
    }

}
