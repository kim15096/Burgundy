package app.me.nightstory.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.me.nightstory.R;
import app.me.nightstory.lobby.AddLobbyActivity;
import app.me.nightstory.lobby.LobbyActivity;
import app.me.nightstory.login.Closed;
import app.me.nightstory.login.Login;
import app.me.nightstory.login.Sign;
import app.me.nightstory.login.Splash;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton create_fab;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference userRef;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public static String inLobbyID = "";
    private ViewPagerAdapter viewPagerAdapter;
    private TextView username;
    private VideoView bgVid;
    private String path;
    private GoogleSignInClient googleSignInClient;

    public static String nickname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAppLocale("ko");
        setContentView(R.layout.main_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users").document(firebaseUser.getUid());

        username = findViewById(R.id.username);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        bgVid = findViewById(R.id.videoView);
        create_fab = findViewById(R.id.create_fab);


        path = "android.resource://" + getPackageName() + "/" + R.raw.login_bg;


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setInlineLabel(true);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(0).setText(R.string.tab_hot);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_trending_up_24);
        tabLayout.getTabAt(1).setText(R.string.tab_fresh);
        /*tabLayout.getTabAt(2).setIcon(R.drawable.ic_new);
        tabLayout.getTabAt(2).setText(R.string.tab_fresh);*/
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


        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inLobbyID = documentSnapshot.get("inLobby").toString();

                if (!inLobbyID.equals("")){
                    Intent mainIntent = new Intent(MainActivity.this, LobbyActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                }
            }
        });

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                nickname = documentSnapshot.get("username").toString();


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            bgVid.setAudioFocusRequest(AudioManager.AUDIOFOCUS_NONE);
        }
        bgVid.setVideoURI(Uri.parse(path));
        bgVid.start();

        bgVid.setOnPreparedListener (new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        SimpleDateFormat time = new SimpleDateFormat("k");
        Date currentTime = Calendar.getInstance().getTime();
        String timeRN = time.format(currentTime);
        final int timeINT = Integer.parseInt(timeRN);

        /*if (!((0<=timeINT && timeINT<=4) || (timeINT<=24 && timeINT>=18))) {
            Intent mainIntent = new Intent(MainActivity.this, Closed.class);
            MainActivity.this.startActivity(mainIntent);
            MainActivity.this.finish();
        }*/

        // deleting lobbies
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Query query = db.collection("Lobbies").whereEqualTo("hostID", "");
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
            }
        }, 1000);

    }


    public void showInfo(View view){

        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setTitle(R.string.mainInfo_title)
                .setMessage(R.string.mainInfo_msg)
                .setNegativeButton("로그아웃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmLogout();
                    }
                })
                .setPositiveButton(R.string.main_signout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDel();
                    }
                })
                .show();
    }

    public void createRoom(View view){
        Intent mainIntent = new Intent(MainActivity.this, AddLobbyActivity.class);
        MainActivity.this.startActivity(mainIntent);
    }

    public void confirmLogout() {


        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    firebaseAuth.signOut();

                    SimpleDateFormat time = new SimpleDateFormat("k");
                    Date currentTime = Calendar.getInstance().getTime();
                    String timeRN = time.format(currentTime);
                    final int timeINT = Integer.parseInt(timeRN);

                    if ((0 <= timeINT && timeINT <= 4) || (timeINT <= 24 && timeINT >= 18)) {
                        Intent mainIntent = new Intent(MainActivity.this, Sign.class);
                        MainActivity.this.startActivity(mainIntent);
                        MainActivity.this.finish();
                    } else {
                        Intent mainIntent = new Intent(MainActivity.this, Closed.class);
                        MainActivity.this.startActivity(mainIntent);
                        MainActivity.this.finish();
                    }

                }

            }
        });
    }

    public void confirmDel(){

        new AlertDialog.Builder(this, R.style.dialogTheme)
                .setIcon(R.drawable.ic_round_warning)
                .setTitle("계정을 삭제하면 기존에 있는 데이터가 모두 삭제됩니다.")
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.main_signout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

                        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    firebaseAuth.signOut();

                                    SimpleDateFormat time = new SimpleDateFormat("k");
                                    Date currentTime = Calendar.getInstance().getTime();
                                    String timeRN = time.format(currentTime);
                                    final int timeINT = Integer.parseInt(timeRN);

                                    if ((0<=timeINT && timeINT<=4) || (timeINT<=24 && timeINT>=18)) {
                                        Intent mainIntent = new Intent(MainActivity.this, Sign.class);
                                        MainActivity.this.startActivity(mainIntent);
                                        MainActivity.this.finish();
                                    }
                                    else{
                                        Intent mainIntent = new Intent(MainActivity.this, Closed.class);
                                        MainActivity.this.startActivity(mainIntent);
                                        MainActivity.this.finish();
                                    }

                                }
                            }
                        });
                    }
                })
                .show();
    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(localeCode.toLowerCase()));
        resources.updateConfiguration(configuration, displayMetrics);
        configuration.locale = new Locale(localeCode.toLowerCase());
        resources.updateConfiguration(configuration, displayMetrics);
    }

   }
