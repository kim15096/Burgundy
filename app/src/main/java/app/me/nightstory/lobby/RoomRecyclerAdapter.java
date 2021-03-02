package app.me.nightstory.lobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class RoomRecyclerAdapter extends FirestoreRecyclerAdapter<LobbyPostModel, RoomViewHolder> {

    public Context context;
    private final String userID;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore db;
    private final FirebaseUser firebaseUser;
    private final DocumentReference userRef;

    public RoomRecyclerAdapter(FirestoreRecyclerOptions recyclerOptions) {
        super(recyclerOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getCurrentUser().getUid();
        userRef = db.collection("Users").document(userID);

    }

    @Override
    protected void onBindViewHolder(RoomViewHolder holder, final int position, final LobbyPostModel model) {
        holder.setTitle(model.getTitle());
        holder.setCategory(model.getCategory());
        holder.setCur_views(model.getCur_views());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(context, R.style.dialogTheme);
                pd.setMessage(context.getString(R.string.joining));
                pd.setCancelable(false);
                pd.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        userRef.update("inLobby", model.getLobbyID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                db.collection("Lobbies").document(model.getLobbyID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Long tot_views = documentSnapshot.getLong("tot_views");
                                        tot_views = tot_views + 1;

                                        Long cur_views = documentSnapshot.getLong("cur_views");
                                        cur_views = cur_views + 1;

                                        db.collection("Lobbies").document(model.getLobbyID()).update("tot_views", tot_views);
                                        db.collection("Lobbies").document(model.getLobbyID()).update("cur_views", cur_views);

                                        MainActivity.inLobbyID = model.getLobbyID();
                                        Intent i1 = new Intent(context, LobbyActivity.class);
                                        context.startActivity(i1);
                                        pd.dismiss();

                                    }
                                });


                            }

                        });

                    }
                }, 1000);
            }
        });

        holder.joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog pd = new ProgressDialog(context, R.style.dialogTheme);
                pd.setMessage(context.getString(R.string.joining));
                pd.setCancelable(false);
                pd.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        userRef.update("inLobby", model.getLobbyID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                db.collection("Lobbies").document(model.getLobbyID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        Long tot_views = documentSnapshot.getLong("tot_views");
                                        tot_views = tot_views + 1;

                                        Long cur_views = documentSnapshot.getLong("cur_views");
                                        cur_views = cur_views + 1;

                                        db.collection("Lobbies").document(model.getLobbyID()).update("tot_views", tot_views);
                                        db.collection("Lobbies").document(model.getLobbyID()).update("cur_views", cur_views);

                                        MainActivity.inLobbyID = model.getLobbyID();
                                        Intent i1 = new Intent(context, LobbyActivity.class);
                                        context.startActivity(i1);
                                        pd.dismiss();

                                    }
                                });


                            }

                        });

                    }
                }, 1000);


            }

        });
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lobby_recycler_card, parent, false);

        return new RoomViewHolder(view);
    }

}
