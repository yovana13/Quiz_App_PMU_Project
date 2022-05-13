package com.example.quizapp3;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {

    TextView tvNameAndSurname, tvEmail, tvCountry, tvCity, tvPoints;
    TextView historyLevel, langaugeLevel, literatureLevel, geographyLevel;
    BottomSheetDialog dialog;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
        }

        tvNameAndSurname = v.findViewById(R.id.tvNameAndSurname);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvCountry = v.findViewById(R.id.tvCountry);
        tvCity = v.findViewById(R.id.tvCity);
        tvPoints = v.findViewById(R.id.tvPoints);
        historyLevel = v.findViewById(R.id.historyLevel);
        langaugeLevel = v.findViewById(R.id.langaugeLevel);
        literatureLevel = v.findViewById(R.id.literatureLevel);
        geographyLevel = v.findViewById(R.id.geographyLevel);

        dialog = new BottomSheetDialog(getActivity());
        createDialog();

        Button btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        Button changePasswordBtn  = v.findViewById(R.id.changePasswordBtn);
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvNameAndSurname.setText(user.firstName + "  " + user.lastName);
                    tvEmail.setText(user.email);
                    email = user.email;
                    tvCity.setText("Град: " + user.city);
                    tvCountry.setText("Държава: " + user.country);
                    tvPoints.setText("Общи точки: " + user.totalPoints);

                    historyLevel.setText("Ниво " + user.history);
                    literatureLevel.setText("Ниво " + user.literature);
                    langaugeLevel.setText("Ниво " + user.language);
                    geographyLevel.setText("Ниво " + user.geography);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }

    private void createDialog() {
        View view = getLayoutInflater().inflate(R.layout.bottom_dialog, null, false);

        Button saveChangesBtn = view.findViewById(R.id.saveChangesBtn);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        EditText oldPassword = view.findViewById(R.id.oldPassword);
        EditText newPassword = view.findViewById(R.id.newPassword);

        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                checkPassword(email, oldPassword.getText().toString(), newPassword.getText().toString());

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Паролата е успешно променена.", Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void checkPassword(String email, String oldPassword, String newPassword) {

        if (email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(getActivity(), "Моля попълнете всички полета", Toast.LENGTH_LONG).show();
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, oldPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            changePassword(newPassword);
                        } else {
                            Toast.makeText(getActivity(), "Старата парола не е верна.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}