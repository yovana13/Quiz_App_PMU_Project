package com.example.quizapp3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizFragment extends Fragment {

    private String selectedTopicName = "";

    private String currentLevel = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);

        final LinearLayout history = v.findViewById(R.id.historyLayout);
        final LinearLayout language = v.findViewById(R.id.languageLayout);
        final LinearLayout literature = v.findViewById(R.id.literatureLayout);
        final LinearLayout geography = v.findViewById(R.id.geographyLayout);

        final Button stratBtn = v.findViewById(R.id.startQuizBtn);

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "history";
                getLevel(selectedTopicName);

                history.setBackgroundResource(R.drawable.green_frame);

                language.setBackgroundResource(R.drawable.white_frame);
                literature.setBackgroundResource(R.drawable.white_frame);
                geography.setBackgroundResource(R.drawable.white_frame);
            }
        });

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "language";
                getLevel(selectedTopicName);

                language.setBackgroundResource(R.drawable.green_frame);

                history.setBackgroundResource(R.drawable.white_frame);
                literature.setBackgroundResource(R.drawable.white_frame);
                geography.setBackgroundResource(R.drawable.white_frame);
            }
        });

        literature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "literature";
                getLevel(selectedTopicName);

                literature.setBackgroundResource(R.drawable.green_frame);

                history.setBackgroundResource(R.drawable.white_frame);
                language.setBackgroundResource(R.drawable.white_frame);
                geography.setBackgroundResource(R.drawable.white_frame);
            }
        });

        geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTopicName = "geography";
                getLevel(selectedTopicName);

                geography.setBackgroundResource(R.drawable.green_frame);

                history.setBackgroundResource(R.drawable.white_frame);
                language.setBackgroundResource(R.drawable.white_frame);
                literature.setBackgroundResource(R.drawable.white_frame);
            }
        });


        stratBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedTopicName.isEmpty()) {
                    Toast.makeText(getActivity(), "Моля изберете категория.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getActivity(), QuestionsActivity.class);
                    intent.putExtra("selectedTopic", selectedTopicName);
                    intent.putExtra("questionId", "1");
                    intent.putExtra("currentLevel", currentLevel);
                    intent.putExtra("currentPoints", 0);
                    startActivity(intent);
                }
            }
        });

        return v;
    }


    private void getLevel(String selectedTopicName) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    if(selectedTopicName.equals("history"))
                        currentLevel = user.history;
                    else if(selectedTopicName.equals("language"))
                        currentLevel = user.language;
                    else if (selectedTopicName.equals("literature"))
                        currentLevel = user.literature;
                    else if (selectedTopicName.equals("geography"))
                        currentLevel = user.geography;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}