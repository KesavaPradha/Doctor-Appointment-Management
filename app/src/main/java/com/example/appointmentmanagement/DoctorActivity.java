package com.example.appointmentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DoctorActivity extends AppCompatActivity {

    private DatabaseReference dDatabase;
    private ListView dListView;
    private String branch;
    private ArrayAdapter<String> dAdapter;
    String doctors[] = {};
    String experience[] = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Intent intent = getIntent();
        branch = intent.getStringExtra("branch");

        dListView = (ListView)findViewById(R.id.doctor_lst);

        dDatabase = FirebaseDatabase.getInstance().getReference().child("doctors");

        dDatabase.child(branch).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Long length =  new Long(dataSnapshot.getChildrenCount());
                Log.d("len " , length +"");

                doctors = new String[length.intValue()];
                experience = new String[length.intValue()];
                int i = 0;

                for( DataSnapshot doctor: dataSnapshot.getChildren() ) {

                    doctors[i] = doctor.getKey();
                    experience[i] = doctor.child("experience").getValue().toString();
                    Log.d("value " , doctors[i] +" " + experience[i]);
                    i++;

                }

                //dAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,doctors);
                //dListView.setAdapter(dAdapter);

                customSimpleAdapterListView();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    // This method shows how to customize SimpleAdapter to show image and text in ListView.
    private void customSimpleAdapterListView()
    {

        ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

        int titleLen = doctors.length;
        for(int i =0; i < titleLen; i++) {

            Map<String,Object> listItemMap = new HashMap<String,Object>();
            listItemMap.put("title", doctors[i]);
            listItemMap.put("experience", "Experience : " + experience[i]);

            itemDataList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,itemDataList,R.layout.activity_doctor,
                new String[]{"title","experience"},new int[]{R.id.doctor_name,R.id.doctor_experience});

        dListView.setAdapter(simpleAdapter);

        dListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Object clickItemObj = adapterView.getAdapter().getItem(i);
                HashMap clickItemMap = (HashMap)clickItemObj;
                String itemTitle = (String)clickItemMap.get("title");

                Log.d("selected ",itemTitle);
                Intent intent = new Intent(getApplicationContext(),AvailabilityActivity.class);
                intent.putExtra("doctor",itemTitle);
                intent.putExtra("branch", branch);
                startActivity(intent);

            }
        });

    }
}