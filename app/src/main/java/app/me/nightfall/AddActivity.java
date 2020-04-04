package app.me.nightfall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

public class AddActivity extends AppCompatActivity {

    private EditText title,desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.create_title);
        desc = findViewById(R.id.create_desc);
    }


    public void createLobby(View view){
        String lobby_title = title.getText().toString();
        String lobby_desc = desc.getText().toString();

    }
}
