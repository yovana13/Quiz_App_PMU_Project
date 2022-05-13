package com.example.quizapp3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChallengeFragment extends Fragment {

    ProgressDialog tempDialog;
    CountDownTimer mCountDownTimer;

    List<NameAndPoints> challengeList;
    TextView first, second, third, forth, fifth, sixth, seventh;
    TextView firstPoints, secondPoints, thirdPoints, fourthPoints, fifthPoints, sixthPoints, seventhPoints;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_challenge, container, false);

        challengeList= new ArrayList<>();

        tempDialog = new ProgressDialog(getActivity());
        tempDialog.setMessage("Моля изчякайте...");
        tempDialog.setProgress(0);
        tempDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        tempDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));

        TextView title = v.findViewById(R.id.title);
        first = v.findViewById(R.id.first);
        second = v.findViewById(R.id.second);
        third = v.findViewById(R.id.third);
        forth = v.findViewById(R.id.fourth);
        fifth = v.findViewById(R.id.fifth);
        sixth = v.findViewById(R.id.sixth);
        seventh = v.findViewById(R.id.seventh);

        firstPoints = v.findViewById(R.id.firstPoints);
        secondPoints = v.findViewById(R.id.secondPoints);
        thirdPoints = v.findViewById(R.id.thirdPoints);
        fourthPoints = v.findViewById(R.id.fourthPoints);
        fifthPoints = v.findViewById(R.id.fifthPoints);
        sixthPoints = v.findViewById(R.id.sixthPoints);
        seventhPoints = v.findViewById(R.id.seventhPoints);

        getAllUsers();

        tempDialog.show();
        mCountDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {
                tempDialog.setMessage("Моля изчякайте...");
            }

            @Override
            public void onFinish() {
                tempDialog.dismiss();
                sortByPoints();
            }
        }.start();


        return v;
    }

    private void getAllUsers() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("users");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String name = ds.child("firstName").getValue(String.class) + " "+ds.child("lastName").getValue(String.class);
                    int points = ds.child("totalPoints").getValue(Integer.class);
                    NameAndPoints nameAndPoints = new NameAndPoints(name, points);
                    challengeList.add(nameAndPoints);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
    }

    private void sortByPoints() {
        for(int i=0; i<challengeList.size(); i++) {
            for(int j=i+1; j<challengeList.size(); j++) {
                NameAndPoints tmp;
                if(challengeList.get(i).points < challengeList.get(j).points) {
                    tmp = challengeList.get(i);
                    challengeList.set(i, challengeList.get(j));
                    challengeList.set(j, tmp);
                }
            }
        }
        displayData();
    }

    private void displayData() {
        first.setText((challengeList.get(0).name).toString());
        second.setText((challengeList.get(1).name).toString());
        third.setText((challengeList.get(2).name).toString());
        forth.setText((challengeList.get(3).name).toString());
        fifth.setText((challengeList.get(4).name).toString());
        sixth.setText((challengeList.get(5).name).toString());
        seventh.setText((challengeList.get(6).name).toString());

        firstPoints.setText(String.valueOf(challengeList.get(0).points)+" точки");
        secondPoints.setText(String.valueOf(challengeList.get(1).points)+" точки");
        thirdPoints.setText(String.valueOf(challengeList.get(2).points)+" точки");
        fourthPoints.setText(String.valueOf(challengeList.get(3).points)+" точки");
        fifthPoints.setText(String.valueOf(challengeList.get(4).points)+" точки");
        sixthPoints.setText(String.valueOf(challengeList.get(5).points)+" точки");
        seventhPoints.setText(String.valueOf(challengeList.get(6).points)+" точки");
    }
}