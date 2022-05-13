package com.example.quizapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class QuestionsActivity extends AppCompatActivity {
    private TextView numberQuestions;
    private TextView question;

    private AppCompatButton a, b, c, d;

    private String correctAnswer = "";
    private String getSelectedTopicName = "";
    private String getQuestionId = "";
    private String currentLevel = "";

    private Integer currentPoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView selectedTopic = findViewById(R.id.selctedTopic);

        numberQuestions = findViewById(R.id.numberQuestions);
        question = findViewById(R.id.question);

        a = findViewById(R.id.a);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);
        d = findViewById(R.id.d);

        getSelectedTopicName = getIntent().getStringExtra("selectedTopic");
        getQuestionId = getIntent().getStringExtra("questionId");
        currentLevel = getIntent().getStringExtra("currentLevel");
        currentPoints = getIntent().getIntExtra("currentPoints", 0);

        getData();

        selectedTopic.setText("Ниво " + currentLevel);
        numberQuestions.setText(getQuestionId + "/5");


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCorrectAnswer();
                if(correctAnswer.equals("a")) {
                    Toast.makeText(QuestionsActivity.this, "Верен отговор",
                            Toast.LENGTH_SHORT).show();
                    currentPoints = currentPoints + 1;
                } else {
                    a.setBackgroundResource(R.drawable.red_bg);
                }
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextQuestion();
                    }
                }, 3000);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCorrectAnswer();
                if(correctAnswer.equals("b")) {
                    Toast.makeText(QuestionsActivity.this, "Верен отговор",
                            Toast.LENGTH_SHORT).show();
                    currentPoints = currentPoints + 1;
                } else {
                    b.setBackgroundResource(R.drawable.red_bg);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextQuestion();
                    }
                }, 3000);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctAnswer.equals("c")) {
                    getCorrectAnswer();
                    Toast.makeText(QuestionsActivity.this, "Верен отговор",
                            Toast.LENGTH_SHORT).show();
                    currentPoints = currentPoints + 1;
                } else {
                    c.setBackgroundResource(R.drawable.red_bg);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextQuestion();
                    }
                }, 3000);
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCorrectAnswer();
                if(correctAnswer.equals("d")) {
                    Toast.makeText(QuestionsActivity.this, "Верен отговор",
                            Toast.LENGTH_SHORT).show();
                    currentPoints = currentPoints + 1;
                } else {
                    d.setBackgroundResource(R.drawable.red_bg);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextQuestion();
                    }
                }, 3000);
            }
        });

    }

    private void getData() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("questionnaires");
        reference.child(getSelectedTopicName).child(currentLevel).child(getQuestionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String getQuestionnaireName = snapshot.child("question").getValue(String.class);
                question.setText(getQuestionnaireName);

                final String aValue = snapshot.child("a").getValue(String.class);
                a.setText(aValue);

                final String bValue = snapshot.child("b").getValue(String.class);
                b.setText(bValue);

                final String cValue = snapshot.child("c").getValue(String.class);
                c.setText(cValue);

                final String dValue = snapshot.child("d").getValue(String.class);
                d.setText(dValue);

                correctAnswer = snapshot.child("answer").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void nextQuestion() {
        if(getQuestionId.equals("5")) {
            Intent intent = new Intent(this, ResultsActivity.class);
            intent.putExtra("selectedTopic", getSelectedTopicName);
            intent.putExtra("currentPoints", currentPoints);
            intent.putExtra("currentLevel", currentLevel);
            startActivity(intent);
        } else {
            getQuestionId = String.valueOf(Integer.parseInt(getQuestionId) + 1);
            Intent intent = new Intent(this, QuestionsActivity.class);
            intent.putExtra("selectedTopic", getSelectedTopicName);
            intent.putExtra("questionId", getQuestionId);
            intent.putExtra("currentLevel", currentLevel);
            intent.putExtra("currentPoints", currentPoints);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void getCorrectAnswer() {
        if(correctAnswer.equals("a")) {
            a.setBackgroundResource(R.drawable.green_bg);
        } else if (correctAnswer.equals("b")) {
            b.setBackgroundResource(R.drawable.green_bg);
        } else if(correctAnswer.equals("c")) {
            c.setBackgroundResource(R.drawable.green_bg);
        } else if (correctAnswer.equals("d")) {
            d.setBackgroundResource(R.drawable.green_bg);
        }
    }
}