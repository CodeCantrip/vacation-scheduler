package com.example.vacationscheduler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;

import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.repository.Repository;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.adapters.ExcursionAdapter;
import com.example.vacationscheduler.entities.Excursion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.List;

public class VacationDetails extends AppCompatActivity{

    private EditText titleEdit;

    private EditText hotelEdit;

    private EditText startDateEdit;

    private EditText endDateEdit;

    private Repository repository;

    private ExcursionAdapter excursionAdapter;

    private int vacationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)
                !=PackageManager.PERMISSION_GRANTED){
              ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS},100);
            }
        }

        titleEdit = findViewById(R.id.vacationTitleEdit);
        hotelEdit = findViewById(R.id.vacationHotelEdit);
        startDateEdit = findViewById(R.id.vacationStartDateEdit);
        endDateEdit = findViewById(R.id.vacationEndDateEdit);
        RecyclerView excursionRecyclerView = findViewById(R.id.excursionRecyclerView);

        Button saveButton = findViewById(R.id.saveVacationButton);

        Button setVacationAlertButton = findViewById(R.id.setVacationAlertButton);

        Button cancelButton = findViewById(R.id.cancelVacationButton);

        Button deleteButton = findViewById(R.id.deleteVacationButton);

        Button addExcursionButton = findViewById(R.id.addExcursionButton);

        Button shareButton = findViewById(R.id.shareButton);



        repository = new Repository(getApplication());

        vacationID = getIntent().getIntExtra("vacationID", -1);

        List<Excursion> associatedExcursions;

        if (vacationID != -1) {
            associatedExcursions = repository.getAssociatedExcursions(vacationID);

            titleEdit.setText(getIntent().getStringExtra("title"));
            hotelEdit.setText(getIntent().getStringExtra("hotel"));
            startDateEdit.setText(getIntent().getStringExtra("startDate"));
            endDateEdit.setText(getIntent().getStringExtra("endDate"));

        }

        else {
            associatedExcursions = new ArrayList<>();
            deleteButton.setVisibility(View.GONE);
            addExcursionButton.setVisibility(View.GONE);
            setVacationAlertButton.setVisibility(View.GONE);
        }

        excursionAdapter = new ExcursionAdapter(associatedExcursions);
        excursionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionRecyclerView.setAdapter(excursionAdapter);

        saveButton.setOnClickListener(view -> {
                    String title = titleEdit.getText().toString();
                    String hotel = hotelEdit.getText().toString();
                    String startDate = startDateEdit.getText().toString();
                    String endDate = endDateEdit.getText().toString();

                    if (title.isEmpty() || hotel.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                        Toast.makeText(this, "Please fill out empty fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                    sdf.setLenient(false);


                    try {
                        Date vacationStart = sdf.parse(startDate);
                        Date vacationEnd = sdf.parse(endDate);

                        if (vacationStart.after(vacationEnd)) {
                            Toast.makeText(this, "Vacations dates are invalid", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    } catch (ParseException e) {
                        Toast.makeText(this, "Please enter date as MM/dd/yyyy", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Vacation vacation;
                    if (vacationID == -1) {
                        vacation = new Vacation(0, title, hotel, startDate, endDate);
                        repository.insert(vacation);
                    } else {
                        vacation = new Vacation(vacationID, title, hotel, startDate, endDate);
                        repository.update(vacation);

                    }
                    finish();
                });


            setVacationAlertButton.setOnClickListener( view ->{
                String title = titleEdit.getText().toString();
                String startDate = startDateEdit.getText().toString();
                String endDate = endDateEdit.getText().toString();

                if(title.isEmpty() || endDate.isEmpty() || startDate.isEmpty()) {
                    Toast.makeText(
                            this, "Please fill out all fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat sdf =
                        new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                sdf.setLenient(false);

                try {
                    Date vacationStart = sdf.parse(startDate);
                    Date vacationEnd = sdf.parse(endDate);

                    if (vacationStart.after(vacationEnd)){
                        Toast.makeText(
                                this,
                                "Vacation dates are invalid",
                                Toast.LENGTH_SHORT
                        ).show();
                        return;
                    }


                }   catch (ParseException e) {
                    Toast.makeText(
                            this,
                            "Please enter date in MM/dd/yyyy format",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int startNotificationID = (int) System.currentTimeMillis();
                int endNotificationID = startNotificationID + 1;

                scheduleNotification(
                        startDate,
                        title + " is starting today!",
                        startNotificationID
                );

                scheduleNotification(
                        endDate,
                        title + " is ending today!",
                        endNotificationID
                );

            });


        cancelButton.setOnClickListener(view->
                finish());

        deleteButton.setOnClickListener(view->{
                if(vacationID == -1) {
            finish();
            return;
        }


         List<Excursion>excursions = repository.getAssociatedExcursions(vacationID);
                if(!excursions.isEmpty()) {
                    Toast.makeText(this, "Cannot delete vacation that includes excursions", Toast.LENGTH_SHORT).show();
                    return;
                }
        Vacation vacation = new Vacation(
                vacationID,
                titleEdit.getText().toString(),
                hotelEdit.getText().toString(),
                startDateEdit.getText().toString(),
                endDateEdit.getText().toString()
        );
                new AlertDialog.Builder(VacationDetails.this)
                        .setTitle("Delete Vacation")
                        .setMessage("Are you sure you want to delete vacation?")
                        .setPositiveButton("Delete", (dialog, which)->{
                            repository.delete(vacation);
                            finish();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
        });


        addExcursionButton.setOnClickListener(view -> {
            Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
            intent.putExtra("vacationID", vacationID);
            startActivity(intent);
        });


        shareButton.setOnClickListener(view->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Vacation: " + titleEdit.getText().toString() +
                          "\nHotel: " + hotelEdit.getText().toString() +
                          "\nStart Date: " + startDateEdit.getText().toString() +
                          "\nEnd Date: " + endDateEdit.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share Vacation Details");
            startActivity(shareIntent);

        });
    }

    @Override
        protected void onResume(){
            super.onResume();

            if(excursionAdapter != null && vacationID !=-1){
                excursionAdapter.setExcursions(
                        repository.getAssociatedExcursions(vacationID)
                );
            }

    }
    private void scheduleNotification(String dateString, String message, int notificationID ){

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            sdf.setLenient(false);

            Date date = sdf.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND, 0);

            Intent intent = new Intent(VacationDetails.this, Receiver.class);
                    intent.putExtra("message", message);
                    intent.putExtra("notificationID", notificationID);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    VacationDetails.this,
                    notificationID,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            AlarmManager alarmManager =
                    (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            Toast.makeText(this, "Notification Scheduled",Toast.LENGTH_SHORT).show();


        }   catch (ParseException e){
            Toast.makeText(this, "Could not schedule notification", Toast.LENGTH_SHORT).show();
        }
    }
}