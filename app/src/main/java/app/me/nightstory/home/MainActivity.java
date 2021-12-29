package app.me.nightstory.home;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.canhub.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import app.me.nightstory.R;
import app.me.nightstory.article.AddPost;
import app.me.nightstory.lobby.AddLobbyActivity;
import app.me.nightstory.lobby.LobbyActivity;
import app.me.nightstory.login.Closed;
import app.me.nightstory.login.Sign;
import app.me.nightstory.slidinglayer.SlidingLayer;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton create_fab;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DocumentReference userRef;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    public static String inLobbyID = "";
    public static SlidingLayer slideLayer;
    public static String defaultPP =
            "https://firebasestorage.googleapis.com/v0/b/nightfall-alpha.appspot.com/o/Admin%2Fpngwing.com.png?alt=media&token=41da1f77-f4b1-4138-9ad3-9f9f59457fc7";
    public static String myPpURL = defaultPP;
    public static String myUserID;

    private ViewPagerAdapter viewPagerAdapter;
    private TextView username;
    private UploadTask uploadTask;
    private String path, imageURL_STR = "", imageURL = "";
    private GoogleSignInClient googleSignInClient;
    private CircularImageView ppSmall, ppBig;
    public static String nickname = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLocale(this, "ko");
        setContentView(R.layout.main_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        myUserID = firebaseUser.getUid();
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("Users").document(firebaseUser.getUid());

        username = findViewById(R.id.lobbyL_title);
        ppSmall = findViewById(R.id.main_pp);
        viewPager = findViewById(R.id.test_vp);
        tabLayout = findViewById(R.id.tabLayout);
        slideLayer = findViewById(R.id.slideView);


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setInlineLabel(true);
        //tabLayout.getTabAt(0).setIcon(R.drawable.ic_hot);
        tabLayout.getTabAt(0).setText("Open");
        tabLayout.getTabAt(1).setText("Join");
        //tabLayout.getTabAt(1).setIcon(R.drawable.ic_round_trending_up_24);


        //adminDeleteUsers
        //adminDeleteUsers();


        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                inLobbyID = documentSnapshot.get("inLobby").toString();

                if (!inLobbyID.equals("")) {
                    Intent mainIntent = new Intent(MainActivity.this, LobbyActivity.class);
                    MainActivity.this.startActivity(mainIntent);
                }
            }
        });


        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                nickname = documentSnapshot.get("username").toString();
                imageURL = documentSnapshot.get("imageURL").toString();

                if (!documentSnapshot.get("imageURL").toString().equals(MainActivity.defaultPP)) {
                    myPpURL = imageURL;
                }

                Glide.with(MainActivity.this).load(imageURL).centerCrop()
                        .into(ppSmall);


            }
        });

        ppSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(null);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

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
        /*new Handler().postDelayed(new Runnable() {
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
        }, 2000);*/

    }

    public void showInfo(View view) {

        final BottomSheetDialog profileBtmSheet = new BottomSheetDialog(MainActivity.this);
        profileBtmSheet.setContentView(R.layout.bottom_sheet_profile);

        TextView profileTv = profileBtmSheet.findViewById(R.id.profile_username);
        ppBig = profileBtmSheet.findViewById(R.id.profile_image);

        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                imageURL = documentSnapshot.get("imageURL").toString();

                Glide.with(profileBtmSheet.getContext()).load(imageURL).centerCrop()
                        .into(ppBig);

            }
        });

        LinearLayout settingsLay = profileBtmSheet.findViewById(R.id.profile_settingsLL);
        LinearLayout ppLay = profileBtmSheet.findViewById(R.id.profile_ppLL);
        LinearLayout myPosts = profileBtmSheet.findViewById(R.id.profile_postsLL);

        profileTv.setText(nickname);

        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(MainActivity.this, MyPostsActivity.class);
                MainActivity.this.startActivity(mainIntent);
            }
        });

        ppLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] menuOptions = {"프로필 사진 변경", "프로필 사진 제거"};

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this, R.style.dialogTheme);

                builder.setItems(menuOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {

                            CropImage.activity()
                                    .setActivityTitle("Edit")
                                    .setCropMenuCropButtonTitle("Save")
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setCropShape(CropImageView.CropShape.OVAL)
                                    .setMinCropResultSize(256, 256) // update image quality
                                    .setAspectRatio(1, 1)
                                    .setInitialCropWindowPaddingRatio((float) 0.1)
                                    .start(MainActivity.this);


                        } else if (which == 1) {
                            final ProgressDialog pd = new ProgressDialog(MainActivity.this, R.style.MyGravity);
                            pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            pd.setCancelable(false);
                            pd.show();
                            userRef.update("imageURL", MainActivity.defaultPP).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        settingsLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this, R.style.dialogTheme)
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
        });

        profileBtmSheet.show();

    }

    public void launchTest(View view) {
        Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
    }

    public void createRoom(View view) {
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

    public void confirmDel() {

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
                })
                .show();
    }

    public void changeName(View view) {

        final View vw = getLayoutInflater().inflate(R.layout.nicknamelayout, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.dialogTheme);

        alert.setTitle("닉네임 변경");

        final EditText input = vw.findViewById(R.id.nameET);

// Set an EditText view to get user input
        alert.setView(vw);

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
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

        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                final ProgressDialog pd = new ProgressDialog(MainActivity.this, R.style.MyGravity);
                pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                pd.setCancelable(false);
                pd.show();

                Uri resultUri = result.getOriginalUri();
                final StorageReference filepath = storageRef.child("Profile Images").child(firebaseUser.getUid()).child("Profile Image.jpg");
                uploadTask = filepath.putFile(resultUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        // Continue with the task to get the download URL
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            imageURL_STR = downloadUri.toString();
                            userRef.update("imageURL", imageURL_STR).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "DOWNLOAD URL DOESNT WORK FFS", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (slideLayer.isOpened()) {
            slideLayer.closeLayer(true);
        } else if (slideLayer.isClosed()) {
            super.onBackPressed();
        }
    }

    private void adminDeleteUsers() {
        db.collection("Lobbies").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (task.isSuccessful()) {
                        if (document.get("imageURL").equals("")) {
                            db.collection("Users").document(document.getId()).delete();
                        }
                    }
                }
            }
        });
    }
}
