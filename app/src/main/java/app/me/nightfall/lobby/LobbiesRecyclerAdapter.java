package app.me.nightfall.lobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import app.me.nightfall.R;
import app.me.nightfall.home.MainActivity;

public class LobbiesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<LobbyPostModel> lobbyList;
    public Context context;
    public MainActivity mainActivity;
    public static String lobbyID;
    private ImageView hostIcon;


    public LobbiesRecyclerAdapter(List<LobbyPostModel> lobbyList, MainActivity mainActivity){
        this.lobbyList = lobbyList;
        this.mainActivity = mainActivity;

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


        hostIcon = MainView.findViewById(R.id.lobby_hostIcon);

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
                String category = lobbyList.get(position).getCategory();

                viewHolder0.setTitle(title);
                viewHolder0.setCategory(category);

                viewHolder0.joinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lobbyID = lobbyList.get(position).getLobbyID();

                        final ProgressDialog pd = new ProgressDialog(context, R.style.dialogTheme);
                        pd.setMessage("Joining lobby...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("inLobby", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        LobbyFrag LobbyFrag = new LobbyFrag();
                                        Fragment lobbyFrag  = mainActivity.getSupportFragmentManager().findFragmentByTag("lobby");


                                        if (lobbyFrag == null){
                                            mainActivity.getSupportFragmentManager().beginTransaction().add(R.id.lobby_frag_container, LobbyFrag, "lobby").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(LobbyFrag).addToBackStack(null).commit();
                                            pd.dismiss();
                                        }
                                        else {
                                            mainActivity.getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(lobbyFrag).addToBackStack(null).commit();
                                            pd.dismiss();
                                        }

                                    }

                                });
                                pd.dismiss();
                            }
                        }, 1000);


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

    }
    public class ViewHolderAds extends RecyclerView.ViewHolder{

        private View mView;

        public ViewHolderAds(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

    }

}
