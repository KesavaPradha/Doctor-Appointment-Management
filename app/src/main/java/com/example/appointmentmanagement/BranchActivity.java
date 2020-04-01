package com.example.appointmentmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BranchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        this.customSimpleAdapterListView();

    }

    // This method shows how to customize SimpleAdapter to show image and text in ListView.
    private void customSimpleAdapterListView()
    {

        String[] titleArr = { "Cardiologist",
                "ENT"};
                /*"Neurologist",
                "Orthologist",
                "Surgeons",
                "Pshycologist",
                "Radiologist",
                "Opthalmologist",
                "Nephrologist",
                "Urologist",
                "Obsterics and Gynecologist",
                "Pediatrician",
                "Gastrologist",
                "Dermatologist",
                "Anaesthesiologist"};*/
        String[] descArr = {"Heart Specialist",
                "Ears, Nose, Tongue",
                "Nerves and Brain specialist",
                "Bones specialist",
                "",
                "Pshycology and counselling",
                "X-ray and scan",
                "Eyes specialist",
                "Kidney specialist",
                "Urine specialist",
                "Pregnant",
                "Child specialist",
                "Stomach specialist",
                "Skin care and hair",
                "Anaesthetic" };


        Object[] images ={R.mipmap.doctor_cardio_round,R.mipmap.doctor_ent_round};


        ArrayList<Map<String,Object>> itemDataList = new ArrayList<Map<String,Object>>();;

        int titleLen = titleArr.length;
        for(int i =0; i < titleLen; i++) {
            Map<String,Object> listItemMap = new HashMap<String,Object>();
            listItemMap.put("imageId", images[i]);
            listItemMap.put("title", titleArr[i]);
            listItemMap.put("description", descArr[i]);
            itemDataList.add(listItemMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,itemDataList,R.layout.activity_schedule,
                new String[]{"imageId","title","description"},new int[]{R.id.userImage, R.id.userTitle, R.id.userDesc});

        ListView listView = (ListView)findViewById(R.id.listViewExample);
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                HashMap clickItemMap = (HashMap)clickItemObj;
                String itemTitle = (String)clickItemMap.get("title");

                Intent intent = new Intent( getApplicationContext(), DoctorActivity.class );
                intent.putExtra("branch",itemTitle);
                startActivity(intent);

                Toast.makeText(BranchActivity.this, "You select item is  " + itemTitle, Toast.LENGTH_SHORT).show();
            }
        });
    }
}