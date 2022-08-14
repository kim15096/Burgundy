package app.burgundy.com.lobby;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import app.burgundy.com.R;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<CommentsPostModel> comments;


    public CommentsRecyclerAdapter(List<CommentsPostModel> comments){
        this.comments = comments;

    }

    private static class VIEW_TYPES {
        public static final int Viewer = 1;
    }



    @Override
    public int getItemViewType(int position) {

            return VIEW_TYPES.Viewer;

    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View UserView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_recycler_post, parent, false);

        return new UserViewHolder(UserView);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        switch(holder.getItemViewType()) {

            case VIEW_TYPES.Viewer:
                UserViewHolder userViewHolder = (UserViewHolder) holder;

                String comment = comments.get(position).getComment();
                String username = comments.get(position).getUsername();

                userViewHolder.setComment(comment);

                userViewHolder.setUsername(username);



                break;


        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);


        }
        public void setComment(String text){
            TextView comment_tv = itemView.findViewById(R.id.comment_tv);
            comment_tv.setText(text);

        }


        public void setUsername(String username) {
            TextView username_tv = itemView.findViewById(R.id.username_tv);
            username_tv.setText(username + " :");
        }
    }



}
