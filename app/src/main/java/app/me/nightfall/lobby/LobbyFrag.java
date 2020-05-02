package app.me.nightfall.lobby;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import app.me.nightfall.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LobbyFrag extends Fragment {

    public LobbyFrag() {
        // Required empty public constructor
    }

    private Button exitLobbyBtn;
    private TextView lobbyTitle_tv;
    private String lobbyTitle;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_lobby_frag, container, false);

        exitLobbyBtn = view.findViewById(R.id.exitLobbyBtn);
        lobbyTitle_tv = view.findViewById(R.id.lobbyTitle_tv);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        db.collection("Lobbies").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                lobbyTitle = documentSnapshot.get("title").toString();
                lobbyTitle_tv.setText(lobbyTitle);
            }
        });




















        exitLobbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("lobby");
                getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).hide(frag).commit();
                FloatingActionButton create_fab = getActivity().findViewById(R.id.create_fab);
                Button button = getActivity().findViewById(R.id.tolobby_btn);
                create_fab.show();
                button.setVisibility(View.GONE);
            }
        });



        return view;
    }
}
