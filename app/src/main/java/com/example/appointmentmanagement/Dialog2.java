package com.example.appointmentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Dialog2 extends AppCompatDialogFragment {

    private String day, doctor, branch;
    private DatabaseReference dDatabase;
    private String temp;
    private String status;
    private FirebaseAuth dAuth;

    private TextView text;
    public Dialog2(String branch, String doctor, String day) {

        this.branch = branch;
        this.doctor = doctor;
        Log.d("doctor ", doctor);
        this.day = day;
        Log.d("branch ",branch);
        Log.d("day" ,day);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder dBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater dInflator = getActivity().getLayoutInflater();
        final View dView = dInflator.inflate(R.layout.activity_dialog2, null);


        dAuth = FirebaseAuth.getInstance();


        dDatabase = FirebaseDatabase.getInstance().getReference().child("doctors").child(branch).child(doctor).child("id");

        dDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String id = dataSnapshot.getValue().toString();
                Log.d("id " , id);

                dDatabase = FirebaseDatabase.getInstance().getReference().child("doctorName").child(id).child("day").child(day.toLowerCase());

                dDatabase.child("updation").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {


                        temp = dataSnapshot2.getValue().toString();
                        Log.d(" temp ", temp);

                        text= (TextView)dView.findViewById(R.id.confirm_txt);


                        String message = text.getText().toString() + temp;
                        text.setText(message);




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dBuilder.setView(dView).setTitle("Current status")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {




            }
        });


        return dBuilder.create();


    }

}

