package app.me.nightfall.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.me.nightfall.R;

public class LobbiesRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<LobbyPostModel> lobbyList;

    public LobbiesRecyclerAdapter(List<LobbyPostModel> lobbyList){
        this.lobbyList = lobbyList;


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
                viewHolder0.setTitle(title);
        }
    }

    @Override
    public int getItemCount() {
        return lobbyList.size();
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder{

        private View mView;

        public ViewHolder0(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

        public void setTitle(String text){
            TextView title_tv = mView.findViewById(R.id.post_title);
            title_tv.setText(text);

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
