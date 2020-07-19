package app.me.nightfall.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.me.nightfall.R;

public class    ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<ChatPostModel> chatList;

    public ChatRecyclerAdapter(List<ChatPostModel> chatList){
        this.chatList = chatList;

    }

    private class VIEW_TYPES {
        public static final int User = 1;
        public static final int Other = 2;
    }



    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPES.User;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View UserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_user, parent, false);
        View OtherView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler_other, parent, false);

        if (viewType == VIEW_TYPES.User){
            return new UserViewHolder(UserView);
        }
        else if(viewType == VIEW_TYPES.Other){
            return new OtherViewHolder(OtherView);
        }
        else{
            return new UserViewHolder(UserView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.User:
                UserViewHolder userViewHolder = (UserViewHolder) holder;
                break;
            case VIEW_TYPES.Other:
                OtherViewHolder otherViewHolder = (OtherViewHolder) holder;
                break;


        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);


        }

    }
    public class OtherViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        public OtherViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;

        }

    }

}
