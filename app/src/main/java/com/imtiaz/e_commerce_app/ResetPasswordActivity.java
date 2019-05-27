package com.imtiaz.e_commerce_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imtiaz.e_commerce_app.Admin.AdminCategoryActivity;
import com.imtiaz.e_commerce_app.Model.Users;
import com.imtiaz.e_commerce_app.Prevalent.Prevalent;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestions;
    private EditText phoneNumber, question1, question2;
    private Button verifyButton;
    private String parentDbName = "Users";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        check = getIntent().getStringExtra("check");

        pageTitle = findViewById(R.id.page_title);
        titleQuestions = findViewById(R.id.title_question);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 = findViewById(R.id.question_1);
        question2 = findViewById(R.id.question_2);
        verifyButton = findViewById(R.id.verify_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);

        if (check.equals("settings")){

            pageTitle.setText("Set Questions");
            titleQuestions.setText("Please set Answers for the Following Security Question?");
            verifyButton.setText("Set");




            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    setAnswer();
                    displayPreviousAnswer();



                }
            });



        } else if (check.equals("login")){

            phoneNumber.setVisibility(View.VISIBLE);


            final String phone = phoneNumber.getText().toString();
            final String a1 = question1.getText().toString();
            final String a2= question2.getText().toString();

            verifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    forgetPassword(phone, a1, a2);


                }
            });



        }
    }

    private void forgetPassword(final String phone1, final String ans1, final String ans2) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone1).exists())
                {
                    Users usersData = dataSnapshot.child(phone1).getValue(Users.class);
                    if (usersData.getPhone().equals(phone1))
                    {
                        if (usersData.getQuestion1().equals(ans1))
                        {

                            if (usersData.getQuestion2().equals(ans2))
                            {
                                Toast.makeText(ResetPasswordActivity.this, "Your Answers are correct", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this,"Answer 2 is incorrect", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ResetPasswordActivity.this,"Answer 1 is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ResetPasswordActivity.this,"Phone number does not exist", Toast.LENGTH_SHORT).show();
                    }

                } else
                {
                    Toast.makeText(ResetPasswordActivity.this,"Answer does not exists.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }


    private void setAnswer(){
        String answer1 = question1.getText().toString().toLowerCase();
        String answer2 = question2.getText().toString().toLowerCase();

        if (question1.equals("") && question2.equals("")){

            Toast.makeText(ResetPasswordActivity.this, "Please answer both questions", Toast.LENGTH_SHORT).show();

        }else {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String, Object> userdateMap = new HashMap<>();
            userdateMap.put("answer1", answer1);
            userdateMap.put("answer2", answer2);

            ref.child("Security Questions").updateChildren(userdateMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPasswordActivity.this,"You have set security questions successfully.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);

                            }

                        }
                    });

        }

    }

    private void displayPreviousAnswer(){

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Question").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String ans1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans2 = dataSnapshot.child("answer2").getValue().toString();

                    question1.setText(ans1);
                    question2.setText(ans2);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
