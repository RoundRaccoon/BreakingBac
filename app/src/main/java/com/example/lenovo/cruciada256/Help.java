package com.example.lenovo.cruciada256;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Help extends AppCompatActivity {

    TextView q1,q2,q3,q4,q5,q6,q7,q8,q9;
    Dialog myDialog;
    DatabaseReference mDatabase;
    Boolean mProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        q1= (TextView)findViewById(R.id.q1);
        q2= (TextView)findViewById(R.id.q2);
        q3= (TextView)findViewById(R.id.q3);
        q4= (TextView)findViewById(R.id.q4);
        q5= (TextView)findViewById(R.id.q5);
        q6= (TextView)findViewById(R.id.q6);
        q7= (TextView)findViewById(R.id.q7);
        q8= (TextView)findViewById(R.id.q8);
        q9= (TextView)findViewById(R.id.q9);

        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("1");
            }
        });
        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("2");
            }
        });
        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("3");
            }
        });
        q4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("4");
            }
        });
        q5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("5");
            }
        });
        q6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("6");
            }
        });
        q7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("7");
            }
        });
        q8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("8");
            }
        });
        q9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAnswer("9");
            }
        });
    }

    public void DialogAnswer(final String nr) {

        mDatabase= FirebaseDatabase.getInstance().getReference().child("Raspunsuri");
        mProcess = true;

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(nr) && mProcess ) {

                    mProcess = false;

                    AlertDialog.Builder Dialog = new AlertDialog.Builder(Help.this);
                    View mView = getLayoutInflater().inflate(R.layout.answer1,null);

                    //String da = dataSnapshot.child(nr).getValue().toString();

                    //Dialog.setMessage(da);

                    final TextView answer = (TextView) mView.findViewById(R.id.dani);
                    answer.setText(dataSnapshot.child(nr).getValue().toString());


                    Dialog.setView(mView);
                    final AlertDialog dialog = Dialog.create();
                    dialog.show();

                    /*myDialog=new Dialog(Help.this);
                    View mView = getLayoutInflater().inflate(R.layout.answer1,null);
                    answer = (TextView) mView.findViewById(R.id.raspuns);
                    answer.setText(dataSnapshot.child(nr).getValue().toString().trim());
                    myDialog.setContentView(R.layout.answer1);
                    myDialog.show();*/
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}