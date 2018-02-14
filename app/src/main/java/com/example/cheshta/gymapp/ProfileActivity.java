package com.example.cheshta.gymapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    DatabaseReference mUserDatabase;
    FirebaseUser mcurrentUser;
    StorageReference mImageStorage;

    EditText edDate;
    RadioGroup lifestyleBtnGroup;
    RadioButton lfBtn;
    Button btnNext;
    CircleImageView civImage;
    Calendar myCalender = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

civImage = findViewById(R.id.civImage);
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mcurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentId = mcurrentUser.getUid();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentId);
        mUserDatabase.keepSynced(true);
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

                if(!image.equals("default")){
                    Picasso.with(ProfileActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .into(civImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.male).into(civImage);
                        }
                    });
                }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setContentView(R.layout.activity_profile);
        edDate =findViewById(R.id.dob);
        lifestyleBtnGroup = findViewById(R.id.lifestyleBtnGrp);
        btnNext = findViewById(R.id.btnNext);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, date);
                updateLabel();
            }
        };
        edDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = lifestyleBtnGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                lfBtn = (RadioButton) findViewById(selectedId);
                startActivity(new Intent(ProfileActivity.this, GoalActivity.class));
            }
        });

    }

    private void updateLabel(){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edDate.setText(sdf.format(myCalender.getTime()));
    }
}
