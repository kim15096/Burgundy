package app.me.nightstory.article;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
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
    private Button createBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this, "ko");
        setContentView(R.layout.activity_add_post);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        title = findViewById(R.id.createpst_Title);
        content = findViewById(R.id.createpst_content);
        createBtn = findViewById(R.id.createpst_btn);

        createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        createBtn.setEnabled(false);
        createBtn.setTextColor(Color.GRAY);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().equals("") || content.getText().toString().equals("")){

                    createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    createBtn.setEnabled(false);
                    createBtn.setTextColor(Color.GRAY);

                }
                else {
                    createBtn.getBackground().setColorFilter(null);
                    createBtn.setEnabled(true);
                    createBtn.setTextColor(getResources().getColor(R.color.textColorGray));
                }
            }
        });

        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().equals("") || content.getText().toString().equals("")){

                    createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                    createBtn.setEnabled(false);
                    createBtn.setTextColor(Color.GRAY);

                }
                else {
                    createBtn.getBackground().setColorFilter(null);
                    createBtn.setEnabled(true);
                    createBtn.setTextColor(getResources().getColor(R.color.textColorGray));
                }
            }
        });

    }

    public void createLobby(final View view){
        final String pst_Title = title.getText().toString().trim();
        final String pst_Content = content.getText().toString().trim();

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
        createPost.put("likes", 0);

        db.collection("Posts").document(id).set(createPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                db.collection("Users").document(firebaseUser.getUid()).collection("MyPosts").document(id).set(createPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        AddPost.this.finish();
                    }
                });



                }
            });

    }

    public void createpstBack(View view){
        super.onBackPressed();
    }


}