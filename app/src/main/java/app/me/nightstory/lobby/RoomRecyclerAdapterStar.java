package app.me.nightstory.lobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;

public class RoomRecyclerAdapterStar extends FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolderStar> {

    public Context context;
    private final String userID;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final FirebaseUser firebaseUser;
    private AlertDialog photoDialog;
    private ImageView imageView, pp;
    private final DocumentReference userRef;

    public RoomRecyclerAdapterStar(FirestoreRecyclerOptions recyclerOptions) {
        super(recyclerOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();
        userRef = db.collection("Users").document(userID);


    }

    @Override
    protected void onBindViewHolder(RoomViewHolderStar holder, final int position, final LobbyPostModel model) {
        holder.setTitle(model.getTitle());
        holder.setTot_Views(model.getTot_views());
        holder.setUsername(model.getHostName());
        holder.setPicture(model.getImageURL());
        holder.setTime(model.getTimestamp().getTime());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(context, R.style.MyGravity);
                pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                pd.setCancelable(false);
                pd.show();

                                db.collection("Lobbies").document(model.getLobbyID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Long tot_views = documentSnapshot.getLong("tot_views");
                                        tot_views = tot_views + 1;


                                        db.collection("Lobbies").document(model.getLobbyID()).update("tot_views", tot_views).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                MainActivity.inLobbyID = model.getLobbyID();
                                                Intent i1 = new Intent(context, LobbyActivity.class);
                                                context.startActivity(i1);
                                                pd.dismiss();
                                            }
                                        });

                                    }
                        });
            }
        });


    }

    @Override
    public RoomViewHolderStar onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lobby_recycler_card_big, parent, false);

        pp = view.findViewById(R.id.post_pp);


        photoDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.lobby_photo, null);
        photoDialog.setView(v);

        imageView = v.findViewById(R.id.lobbyPhoto);

        return new RoomViewHolderStar(view);
    }


}
