package com.example.appointmentmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookedActivity extends AppCompatActivity {

    private ListView bListView;

    private FirebaseAuth bAuth;
    private DatabaseReference bDatabase, sDatabase, dDatabase;
    String temp;
    int j;
    String[] doctor,day,time,token,branch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked);

        bListView = (ListView)findViewById(R.id.available_lst);

        bAuth = FirebaseAuth.getInstance();

        bDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(bAuth.getCurrentUser().getUid());

        Log.d("uid " , bAuth.getCurrentUser().getUid());

        bDatabase.child("bookList").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long length =  new Long(dataSnapshot.getChildrenCount());
                Log.d("len " , length +"");

                doctor = new String[length.intValue()];
                day = new String[length.intValue()];
                branch = new String[length.intValue()];
                time = new String[length.intValue()];
                token = new String[length.intValue()];
                int i = 0;

                for( DataSnapshot ds: dataSnapshot.getChildren() ) {

                    day[i] = ds.getKey();


                    doctor[i] = ds.child("doctor").getValue().toString();
                    branch[i] = ds.child("branch").getValue().toString();
                    time[i] = ds.child("time").getValue().toString();
                    token[i] = ds.child("token").getValue().toString();
                    Log.d("value " , day[i] + " " +doctor[i] + " " + branch[i] + " " + time[i] + " " + token[i]);



                    i++;

                }

                customSimpleAdapterListView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void customSimpleAdapterListView()
    {

        ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

        int titleLen = doctor.length;
        for(int i =0; i < titleLen; i++) {

            Map<String,Object> listItemMap = new HashMap<String,Object>();
            listItemMap.put("doctor",doctor[i]);
            listItemMap.put("day", day[i].toUpperCase());
            listItemMap.put("time", time[i]);
            listItemMap.put("token", "Your token is : " + token[i]);
            listItemMap.put("branch", branch[i]);
            itemDataList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,itemDataList,R.layout.activity_booked,
                new String[]{"doctor","day", "time","token", "branch"},new int[]{R.id.doctor,R.id.day,R.id.time,R.id.token,R.id.status});

        bListView.setAdapter(simpleAdapter);

        bListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Object clickItemObj = adapterView.getAdapter().getItem(i);
                HashMap clickItemMap = (HashMap)clickItemObj;
                String doctor = (String)clickItemMap.get("doctor");
                String branch = (String)clickItemMap.get("branch");

                String day = (String)clickItemMap.get("day");

                Log.d("dialog ", doctor + " " + branch + " " + day);


                Dialog2 dialog2 = new Dialog2(branch,doctor,day.toLowerCase());
                dialog2.show(getSupportFragmentManager(), "dialog");

            }
        });



    }
}
