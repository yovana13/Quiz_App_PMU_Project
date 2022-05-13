package com.example.quizapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultsActivity extends AppCompatActivity {

    private String selectedTopicName = "";
    private String currentLevel = "";

    private int currentPoints;

    Button toMainActivity, nextLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        selectedTopicName = getIntent().getStringExtra("selectedTopic");
        currentLevel = getIntent().getStringExtra("currentLevel");
        currentPoints = getIntent().getIntExtra("currentPoints", 0);

        toMainActivity = findViewById(R.id.toMainActivity);
        toMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });

        nextLevel = findViewById(R.id.nextLevel);
        nextLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNextLevel();
            }
        });

        getCurrentPointsAndUpdateLevel();

        TextView correctAnswers = findViewById(R.id.correctAnswers);
        correctAnswers.setText("Верни отговори: "+ currentPoints +"/5");

        TextView congratsText = findViewById(R.id.congratsText);
        congratsText.setText("Браво, надграждаш своите знания! Завършихте ниво "+ currentLevel);
    }

    private  void getCurrentPointsAndUpdateLevel() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    reference.child("totalPoints").setValue(user.totalPoints + currentPoints);
                    reference.child(selectedTopicName).setValue(String.valueOf(Integer.parseInt(currentLevel)+1));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ResultsActivity.this, "Database error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startNextLevel() {
        Intent intent = new Intent(this, QuestionsActivity.class);
        intent.putExtra("selectedTopic", selectedTopicName);
        intent.putExtra("questionId", "1");
        intent.putExtra("currentLevel", String.valueOf(Integer.parseInt(currentLevel)+1));
        intent.putExtra("currentPoints", 0);
        startActivity(intent);
    }
}