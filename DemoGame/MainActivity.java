package com.example.demogame;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference dbRef;
    private TextView multiplierText;
    private ImageView plane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // UI Layout (Directly creating a simple view for demo)
        setContentView(R.layout.activity_game); 

        multiplierText = findViewById(R.id.multiplierText);
        plane = findViewById(R.id.plane);
        
        // Firebase Connection
        dbRef = FirebaseDatabase.getInstance().getReference("realtime_db");

        dbRef.child("start_game").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean start = snapshot.getValue(Boolean.class);
                if (start != null && start) {
                    // ৫ সেকেন্ড দেরি করে শুরু হবে যাতে প্রেডিক্টর আগে দেখায়
                    new android.os.Handler().postDelayed(() -> fetchMultiplierAndStart(), 5000);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void fetchMultiplierAndStart() {
        dbRef.child("target_multiplier").get().addOnSuccessListener(snapshot -> {
            Double target = snapshot.getValue(Double.class);
            if (target != null) {
                startAnimation(target);
            }
        });
    }

    private void startAnimation(double target) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f, (float) target);
        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();
            multiplierText.setText(String.format("%.2fx", value));
            plane.setTranslationY(-value * 50);
        });
        animator.start();
    }
}
