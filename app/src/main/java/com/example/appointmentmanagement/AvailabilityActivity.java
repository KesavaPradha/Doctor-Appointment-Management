package com.example.appointmentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AvailabilityActivity extends AppCompatActivity {

    private ListView aListView;
    private DatabaseReference aDatabase;

    String doctor,branch;
    String day[]={};
    String start[] = {};
    String count[] = {};

    ArrayAdapter<String> aAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        Intent intent = getIntent();
        doctor = intent.getStringExtra("doctor");
        branch = intent.getStringExtra("branch");

        Log.d("doctor ", doctor + "  " + branch);

        aListView = (ListView)findViewById(R.id.available_lst);
        aDatabase = FirebaseDatabase.getInstance().getReference().child("doctors").child(branch);

        aDatabase.child(doctor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               Long length = dataSnapshot.getChildrenCount()-3;
               Log.d( "length", " " + length);
               day = new String[length.intValue()];
               start = new String[length.intValue()];
               count = new String[length.intValue()];
               int i = 0;

               for( DataSnapshot ds : dataSnapshot.getChildren()){

                    String value = ds.getKey().toUpperCase();
                    if( !( value.equals("EXPERIENCE") || value.equals("PHONE") || value.equals("ID")) ) {
                        day[i] = value;

                        start[i] = ds.child("start").getValue().toString() + " - " + ds.child("end").getValue().toString();
                        count[i] = ds.child("count").getValue().toString();

                        Log.d("day ", start[i]);
                        i++;
                    }


               }

               customSimpleAdapterListView();

               //aAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1,day);
               //aListView.setAdapter(aAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void customSimpleAdapterListView()
    {


        ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

        int titleLen = day.length;
        for(int i =0; i < titleLen; i++) {
            Map<String,Object> listItemMap = new HashMap<String,Object>();
            listItemMap.put("title", day[i]);
            listItemMap.put("description", start[i]);
            itemDataList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,itemDataList,R.layout.activity_availability,
                new String[]{"title","description"},new int[]{ R.id.userTitle, R.id.userDesc});

        aListView.setAdapter(simpleAdapter);

        aListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("i ", i+"");

                if( Integer.parseInt(count[i] ) < 9 ){


                    DialogBox aDialogBox = new DialogBox((Integer.parseInt(count[i]) +1)+"" , day[i] , start[i], branch, doctor);
                    aDialogBox.show(getSupportFragmentManager(), "dialog Box");

                } else {

                    Toast.makeText(getApplicationContext(), "Sorry...Not Available..\nTry another day... ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
