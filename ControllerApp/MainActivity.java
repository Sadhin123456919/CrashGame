package com.example.controller;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbRef = FirebaseDatabase.getInstance().getReference("realtime_db");

        // Simple Layout with a Button
        Button btnStart = new Button(this);
        btnStart.setText("Send Signal (Start Game)");
        setContentView(btnStart);

        btnStart.setOnClickListener(v -> {
            double randomMultiplier = 1.1 + new Random().nextDouble() * 4;
            dbRef.child("target_multiplier").setValue(randomMultiplier);
            dbRef.child("start_game").setValue(true);
            Toast.makeText(this, "Signal Sent: " + String.format("%.2f", randomMultiplier), Toast.LENGTH_SHORT).show();
        });
    }
}
