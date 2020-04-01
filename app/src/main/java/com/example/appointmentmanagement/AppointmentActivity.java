package com.example.appointmentmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

public class   AppointmentActivity extends AppCompatActivity {

    private Button aBookBtn;
    private Button aViewBtn;
    private Button aLogoutBtn;
    private FirebaseAuth pAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        aBookBtn = (Button)findViewById(R.id.book_btn);
        aViewBtn = (Button)findViewById(R.id.live_btn);
        aLogoutBtn = (Button)findViewById(R.id.logout_btn);
        pAuth = FirebaseAuth.getInstance();

        aBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), BranchActivity.class );
                startActivity(intent);

            }
        });

        aViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),BookedActivity.class);
                startActivity(intent);


            }
        });

        aLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pAuth.signOut();
                Intent intent = new Intent( getApplicationContext(), MainActivity.class );
                startActivity(intent);
                finish();

            }
        });

    }
}
