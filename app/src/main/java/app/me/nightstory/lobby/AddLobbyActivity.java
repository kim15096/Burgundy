package app.me.nightstory.lobby;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opensooq.supernova.gligar.GligarPicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import app.me.nightstory.R;
import app.me.nightstory.home.MainActivity;
import io.grpc.Compressor;

public class AddLobbyActivity extends AppCompatActivity {

    private EditText title, info;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private String desc = "";
    private UploadTask uploadTask;
    private String path, imageURL_STR = "", imageURL = "";
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private StorageReference filepath;
    private DocumentReference userRef;
    private Button createBtn;
    public static final int PICK_IMAGE = 1;
    private Uri resultUri;
    private Boolean imgUploaded = false;
    private ProgressDialog pd;
    private ImageView img;
    private TextView createView;
    private byte[] imageByte;

    public static Integer PICKER_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.setLocale(this, "ko");
        setContentView(R.layout.activity_add_lobby);

        title = findViewById(R.id.lobbyTitle);
        info = findViewById(R.id.addLobby_info);
        createBtn = findViewById(R.id.createlob_btn);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        createView = findViewById(R.id.createTxt);
        createView.setClickable(false);
        createView.setTextColor(Color.GRAY);

        /*createBtn.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        createBtn.setEnabled(false);
        createBtn.setTextColor(Color.GRAY);*/

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().equals("") || info.getText().toString().equals("") || imgUploaded == false){

                    createView.setClickable(false);
                    createView.setTextColor(Color.GRAY);

                }
                else {

                    createView.setClickable(true);
                    createView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });

        info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (title.getText().toString().equals("") || info.getText().toString().equals("") || imgUploaded == false){

                    createView.setClickable(false);
                    createView.setTextColor(Color.GRAY);
                }
                else {
                    createView.setClickable(true);
                    createView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
            }
        });



    }

    public void createRoom2(String url){
        String lobby_title = title.getText().toString();
        String lobby_desc = info.getText().toString();



        final FieldValue timestamp = FieldValue.serverTimestamp();

        final String id = db.collection("Lobbies").document().getId();



        final Map<String, Object> createLobby = new HashMap<>();
        createLobby.put("title", lobby_title);
        createLobby.put("lobbyID", id);
        createLobby.put("hostID", firebaseUser.getUid());
        createLobby.put("hostName", MainActivity.nickname);
        createLobby.put("timestamp", timestamp);
        createLobby.put("imageURL", url);
        createLobby.put("tot_views", 1);
        createLobby.put("active", "true");
        createLobby.put("desc", lobby_desc);

        db.collection("Lobbies").document(id).set(createLobby).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                MainActivity.inLobbyID = id;

                db.collection("Users").document(firebaseUser.getUid()).update("inLobby", id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent mainIntent = new Intent(AddLobbyActivity.this, LobbyActivity.class);
                        AddLobbyActivity.this.startActivity(mainIntent);
                        AddLobbyActivity.this.finish();
                        pd.dismiss();
                    }
                });



            }
        });
    }


    public void createLobby(final View view){

        pd = new ProgressDialog(AddLobbyActivity.this, R.style.MyGravity);

        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        pd.setCancelable(false);
        pd.show();

        filepath = storageRef.child("Posts").child(firebaseUser.getUid()).child(UUID.randomUUID().toString());
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
                    createRoom2(imageURL_STR);
                } else {
                    Toast.makeText(AddLobbyActivity.this, "DOWNLOAD URL DOESNT WORK FFS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        }


    public void addPhoto(View view){

        new GligarPicker().limit(1).requestCode(PICKER_REQUEST_CODE).withActivity(this).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Log.e("onActivityResult", "successful");

            String path[] = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT);
            resultUri = Uri.fromFile(new File(path[0]));

            ImageView photo = findViewById(R.id.add_photo);

            photo.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photo.setImageURI(resultUri);

            imgUploaded = true;

            if (!title.getText().toString().equals("") && !info.getText().toString().equals("")) {

                createView.setClickable(true);
                createView.setTextColor(getResources().getColor(R.color.colorPrimary));

            }
        }
        else {
            return;
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKER_REQUEST_CODE && resultCode == RESULT_OK) {

            resultUri = data.getData();

            Bitmap bmp = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG, 25, stream);

            imageByte = stream.toByteArray();

            try {
                stream.close();
                stream = null;
            } catch (IOException e) {

                e.printStackTrace();
            }

            ImageView photo = findViewById(R.id.add_photo);

            photo.setImageBitmap(orientedBitmap);


            imgUploaded = true;

            if (!title.getText().toString().equals("") && !info.getText().toString().equals("")) {

                createBtn.getBackground().setColorFilter(null);
                createBtn.setEnabled(true);
                createBtn.setTextColor(Color.WHITE);

            }
        }

            else if (resultCode == Activity.RESULT_CANCELED) {


                }

    }*/


    public void createlobby_back(View view){
        super.onBackPressed();
    }

}


