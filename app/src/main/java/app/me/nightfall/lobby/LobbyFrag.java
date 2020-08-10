package app.me.nightfall.lobby;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

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

        exitLobbyBtn = view.findViewById(R.id.quitBtn);
        lobbyTitle_tv = view.findViewById(R.id.lobbyTitle_tv);

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        db.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MainActivity.inLobby = documentSnapshot.get("inLobby").toString();
            }
        });


        db.collection("Lobbies").document(MainActivity.inLobby).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.get("title") != null) {
                    lobbyTitle = documentSnapshot.get("title").toString();
                    lobbyTitle_tv.setText(lobbyTitle);
                }
            }
        });



        exitLobbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                final Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("lobby");

                final FloatingActionButton create_fab = getActivity().findViewById(R.id.create_fab);
                db.collection("Users").document(userID).update("inLobby", "").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseFirestore.getInstance().collection("Lobbies").document(userID).delete();
                        create_fab.show();

                        getActivity().getSupportFragmentManager().popBackStack();



                    }
                });





            }
        });


        return view;
    }
}
