package app.me.nightstory.article;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;
import app.me.nightstory.lobby.AddLobbyActivity;
import app.me.nightstory.lobby.LobbyActivity;

public class AddPost extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private EditText title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this, "ko");
        setContentView(R.layout.activity_add_post);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        title = findViewById(R.id.createpst_Title);
        content = findViewById(R.id.createpst_content);

    }

    public void createLobby(final View view){
        final String pst_Title = title.getText().toString().trim();
        final String pst_Content = content.getText().toString().trim();

        if (pst_Title.trim().isEmpty()){
            Toast.makeText(this, "Please write a title", Toast.LENGTH_SHORT).show();

        }

        else if (pst_Content.trim().isEmpty()){
            Toast.makeText(this, "Please write content", Toast.LENGTH_SHORT).show();
        }

        else {

            final FieldValue timestamp = FieldValue.serverTimestamp();

            final String id = db.collection("Posts").document().getId();


            final Map<String, Object> createPost = new HashMap<>();
            createPost.put("title", pst_Title);
            createPost.put("content", pst_Content);
            createPost.put("postID", id);
            createPost.put("posterID", firebaseUser.getUid());
            createPost.put("poster", MainActivity.nickname);
            createPost.put("timestamp", timestamp);
            createPost.put("tot_views", 1);
            createPost.put("likes", 1);

            db.collection("Posts").document(id).set(createPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AddPost.this.finish();

                }
            });
        }
    }

    public void createpstBack(View view){
        super.onBackPressed();
    }


}