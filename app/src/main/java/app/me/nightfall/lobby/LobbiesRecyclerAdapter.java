package app.me.nightfall.lobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class LobbiesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<LobbyPostModel> lobbyList;
    public Context context;
    private String userID;
    public MainActivity mainActivity;
    public static String lobbyID;
    private ImageView hostIcon;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    public static Integer playerPos = 0;



    public LobbiesRecyclerAdapter(List<LobbyPostModel> lobbyList, MainActivity mainActivity){
        this.lobbyList = lobbyList;
        this.mainActivity = mainActivity;
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();
    }

    private class VIEW_TYPES {
        public static final int Normal = 1;
        public static final int Ads = 2;
    }



    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPES.Normal;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View MainView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lobby_recycler_card, parent, false);
        View AdView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_recycler_ad, parent, false);


        if (viewType == VIEW_TYPES.Normal){
            return new ViewHolder0(MainView);
        }
        else if(viewType == VIEW_TYPES.Ads){
            return new ViewHolderAds(AdView);
        }
        else{
            return new ViewHolder0(MainView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.Ads:
                ViewHolderAds viewHolderAds = (ViewHolderAds) holder;
                break;
            case VIEW_TYPES.Normal:
                ViewHolder0 viewHolder0 = (ViewHolder0) holder;

                final String title = lobbyList.get(position).getTitle();
                final String category = lobbyList.get(position).getCategory();
                String count = lobbyList.get(position).getCount().toString();

                viewHolder0.setTitle(title);
                viewHolder0.setCategory(category);
                viewHolder0.setCount(count);

                viewHolder0.joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lobbyID = lobbyList.get(position).getLobbyID();


                        if (lobbyList.get(position).getCount() >= 4){
                            Toast.makeText(context, "Room is full!", Toast.LENGTH_SHORT).show();
                        }
                        else if (!MainActivity.inLobby.equals("")){
                            Toast.makeText(context, "Already in lobby", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            final ProgressDialog pd = new ProgressDialog(context, R.style.dialogTheme);
                            pd.setMessage("Joining lobby...");
                            pd.setCancelable(false);
                            pd.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    db.collection("Users").document(userID).update("inLobby", lobbyList.get(position).getLobbyID()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            if (lobbyList.get(position).getP1_ID().equals("")){
                                                db.collection("Lobbies").document(lobbyList.get(position).getLobbyID()).update("p1_ID", userID);
                                                playerPos = 1;
                                            }
                                            else if (lobbyList.get(position).getP2_ID().equals("")){
                                                db.collection("Lobbies").document(lobbyList.get(position).getLobbyID()).update("p2_ID", userID);
                                                playerPos = 2;
                                            }
                                            else if (lobbyList.get(position).getP3_ID().equals("")){
                                                db.collection("Lobbies").document(lobbyList.get(position).getLobbyID()).update("p3_ID", userID);
                                                playerPos = 3;
                                            }

                                            final Map<String, Object> joinLobby = new HashMap<>();
                                            joinLobby.put("senderID", "bot");
                                            joinLobby.put("username", userID);
                                            joinLobby.put("message", "nf_joined");
                                            joinLobby.put("timestamp", FieldValue.serverTimestamp());

                                            db.collection("Lobbies").document(lobbyList.get(position).getLobbyID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Long count = documentSnapshot.getLong("count");
                                                    count = count + 1;
                                                    db.collection("Lobbies").document(lobbyList.get(position).getLobbyID()).update("count", count);

                                                    db.collection("Lobbies").document(lobbyList.get(position).getLobbyID()).collection("Chat").document().set(joinLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Intent i1 = new Intent (context, LobbyActivity_temp.class);
                                                            context.startActivity(i1);
                                                            pd.dismiss();

                                                        }
                                                    });
                                                }
                                            });


                                        }

                                    });

                                }
                            }, 1000);

                        }
                    }
                });



        }
    }

    @Override
    public int getItemCount() {
        return lobbyList.size();
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder{

        private View mView;
        private ImageView joinBtn;

        public ViewHolder0(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
            joinBtn = mView.findViewById(R.id.joinBtn);
        }

        public void setTitle(String text){
            TextView title_tv = mView.findViewById(R.id.post_title);
            title_tv.setText(text);

        }

        public void setCategory(String text){
            TextView category = mView.findViewById(R.id.post_category);
            category.setText(text);
        }

        public void setCount(String count){
            TextView lobbyCount = mView.findViewById(R.id.lobby_count);
            String text = count + "/4";

            lobbyCount.setText(text);
        }

    }
    public class ViewHolderAds extends RecyclerView.ViewHolder{

        private View mView;

        public ViewHolderAds(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

    }

}
