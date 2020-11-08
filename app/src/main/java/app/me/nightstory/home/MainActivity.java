package app.me.nightstory.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import app.me.nightstory.R;

import app.me.nightstory.lobby.AddLobbyActivity;
import app.me.nightstory.lobby.LobbyActivity;
import app.me.nightstory.login.Login;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton create_fab;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static String inLobbyID = "";
    private ViewPagerAdapter viewPagerAdapter;
    private TextView username;

    public static String nickname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        username = findViewById(R.id.username);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setInlineLabel(true);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(0).setText("Hot");
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_trending);
        tabLayout.getTabAt(1).setText("Trending");
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_new);
        tabLayout.getTabAt(2).setText("Fresh");
        tabLayout.setTabTextColors(getResources().getColor(R.color.textColorGray),
                getResources().getColor(R.color.colorPrimary));

        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).getIcon().setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        tabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(MainActivity.this, R.color.textColorGray);
                        tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        db = FirebaseFirestore.getInstance();

        db.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inLobbyID = documentSnapshot.get("inLobby").toString();

                if (!inLobbyID.equals("")){
                    Intent mainIntent = new Intent(MainActivity.this, LobbyActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                }
            }
        });

        db.collection("Users").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                nickname = documentSnapshot.get("username").toString();
                username.setText("Welcome " + nickname + "!");


            }
        });

        // deleting lobbies



        Query query = db.collection("Lobbies").whereEqualTo("state", "closed");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        db.collection("Lobbies").document(document.getId()).delete();
                    }
                } else {
                    Log.d("a", "Error getting documents: ", task.getException());
                }
            }
        });


        create_fab = findViewById(R.id.create_fab);




        VideoView view = findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.login_bg;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);
        }
        view.setVideoURI(Uri.parse(path));
        view.start();

        view.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        //when floating action button is clicked
        create_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, AddLobbyActivity.class);
                MainActivity.this.startActivity(mainIntent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        VideoView view = findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.login_bg;
        view.setVideoURI(Uri.parse(path));
        view.start();

        view.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

    }

    public void changeName(View view){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.dialogTheme);

        alert.setTitle("Change nickname");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setTextColor(getResources().getColor(R.color.textColorGray));
        input.setTextSize(13);
        input.setPadding(16, 16, 16, 16);

        alert.setView(input);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int whichButton) {

               String username = input.getText().toString();
               db.collection("Users").document(firebaseUser.getUid()).update("username", username).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       dialog.cancel();
                   }
               });
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

   public void signOut(View view){

       new AlertDialog.Builder(this, R.style.dialogTheme)
               .setTitle("Back to home screen?")
               .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       firebaseAuth.signOut();
                       Intent mainIntent = new Intent(MainActivity.this, Login.class);
                       MainActivity.this.startActivity(mainIntent);
                       MainActivity.this.finish();
                   }
               })

               .setNegativeButton("No!", null)
               .show();
   }

   }
