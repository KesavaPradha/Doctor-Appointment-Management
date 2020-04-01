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

public class DialogBox extends AppCompatDialogFragment {

    private TextView dConfirmTxt;
    private TextView dTokenTxt;
    private String  token;
    private String day, time, doctor, branch;
    private DatabaseReference dDatabase,aDatabase,uDatabase;
    private FirebaseAuth dAuth;

    public DialogBox(String token, String day, String time , String branch, String doctor){

        this.token = token;
        this.day = day;
        this.time = time;
        this.branch = branch;
        this.doctor = doctor;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder dBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater dInflator = getActivity().getLayoutInflater();
        final View dView = dInflator.inflate(R.layout.activity_dialog_box,null);


        dConfirmTxt = dView.findViewById(R.id.confirm_txt);
        dTokenTxt = dView.findViewById(R.id.token_txt);
        dAuth = FirebaseAuth.getInstance();

        dDatabase = FirebaseDatabase.getInstance().getReference().child("doctors").child(branch).child(doctor);

        dBuilder.setView(dView).setTitle("Appointment Confirmation")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dDatabase.child(day.toLowerCase()).child("count").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){


                            dDatabase.child("id").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String currentUser =  FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    String doctorId = dataSnapshot.getValue().toString();
                                    aDatabase = FirebaseDatabase.getInstance().getReference().child("doctorName").child(doctorId).child("day").child(day.toLowerCase());

                                    uDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(currentUser);

                                    uDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String name = dataSnapshot.child("name").getValue().toString();
                                            String phone = dataSnapshot.child("phone").getValue().toString();
                                            String id = dAuth.getCurrentUser().getUid();

                                            Map<String,String> map = new HashMap<String, String>();

                                            map.put("patient",name);
                                            map.put("contact",phone);
                                            map.put("id",id);

                                            aDatabase.child(token+"").setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                  //  Intent intent = new Intent(getContext(),BranchActivity.class);
                                                  //  startActivity(intent);
                                                    aDatabase.child("count").setValue(token);
                                                }
                                            });

                                            uDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    Map<String,String> userMap = new HashMap<>();

                                                    userMap.put("doctor", doctor);
                                                    userMap.put("time", time );
                                                    userMap.put("token", token);
                                                    Log.d("token", token);
                                                    userMap.put("branch",branch);


                                                    uDatabase.child("bookList").child(day.toLowerCase()).setValue(userMap);

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


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        }

                    }
                });

            }
        });

        String confirm = dConfirmTxt.getText().toString();
        String count = dTokenTxt.getText().toString();

        dConfirmTxt.setText(confirm + day.toLowerCase() + " at " + time);
        dTokenTxt.setText(count + token );


        return dBuilder.create();

    }

}