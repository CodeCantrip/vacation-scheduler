package com.example.vacationscheduler;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.repository.Repository;

import com.example.vacationscheduler.entities.Vacation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class ExcursionDetails extends AppCompatActivity {

    private EditText titleEdit;
    private EditText dateEdit;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);

        titleEdit = findViewById(R.id.excursionTitleEdit);
        dateEdit = findViewById(R.id.excursionDateEdit);


        Button saveButton = findViewById(R.id.saveExcursionButton);

        Button cancelButton = findViewById(R.id.cancelExcursionButton);

        Button deleteButton = findViewById(R.id.deleteExcursionButton);

        Button alertButton = findViewById(R.id.setExcursionAlertButton);

        repository = new Repository(getApplication());

        int vacationID = getIntent().getIntExtra("vacationID", -1);
        int excursionID = getIntent().getIntExtra("excursionID", -1);

        Vacation vacation = repository.getVacationByID(vacationID);

        if (vacation == null) {
            Toast.makeText(this, "Unable to find associated vacation", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (excursionID != -1) {
            titleEdit.setText(getIntent().getStringExtra("title"));
            dateEdit.setText(getIntent().getStringExtra("date"));
        } else {
            deleteButton.setVisibility(View.GONE);
            alertButton.setVisibility((View.GONE));

        }

        cancelButton.setOnClickListener(view ->
                finish());

        deleteButton.setOnClickListener(view -> {
            if (excursionID == -1) {
                finish();
                return;
            }
            Excursion excursion = new Excursion(
                    excursionID,
                    titleEdit.getText().toString(),
                    dateEdit.getText().toString(),
                    vacationID
            );
            new AlertDialog.Builder(ExcursionDetails.this)
                    .setTitle("Delete Excursion")
                    .setMessage("Are you sure you want to delete excursion?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        repository.delete(excursion);
                        finish();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        saveButton.setOnClickListener(view -> {
            String title = titleEdit.getText().toString();
            String date = dateEdit.getText().toString();

            if (title.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            sdf.setLenient(false);

            try {
                Date excursionDate = sdf.parse(date);
                Date vacationStart = sdf.parse(vacation.getStartDate());
                Date vacationEnd = sdf.parse(vacation.getEndDate());

                if (excursionDate.before(vacationStart) || excursionDate.after(vacationEnd)) {
                    Toast.makeText(this, "Excursion date must be within vacation dates", Toast.LENGTH_SHORT).show();
                    return;
                }

            } catch (ParseException e) {
                Toast.makeText(this, "Please enter date as MM/dd/yyyy", Toast.LENGTH_SHORT).show();
                return;
            }


            Excursion excursion;


            if (excursionID == -1) {
                excursion = new Excursion(0, title, date, vacationID);
                repository.insert(excursion);
            } else {
                excursion = new Excursion(excursionID, title, date, vacationID);
                repository.update(excursion);
            }



            finish();
        });
        alertButton.setOnClickListener( view ->{
            String title = titleEdit.getText().toString();
            String date = dateEdit.getText().toString();

            if(title.isEmpty() || date.isEmpty()) {
                Toast.makeText(
                        this, "Please fill out all fields",
                        Toast.LENGTH_SHORT).show();
                        return;
            }

            SimpleDateFormat sdf =
                    new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            sdf.setLenient(false);

            try {
                Date excursionDate = sdf.parse(date);
                Date vacationStart = sdf.parse(vacation.getStartDate());
                Date vacationEnd = sdf.parse(vacation.getEndDate());

                if (excursionDate.before(vacationStart) || excursionDate.after(vacationEnd)){

                   Toast.makeText(
                           this,"Date must fall withint the scheduled vacation dates",
                           Toast.LENGTH_SHORT).show();
                           return;
                }

            }   catch (ParseException e) {
                    Toast.makeText(
                            this,
                            "Please enter date in MM/dd/yyyy format",
                            Toast.LENGTH_SHORT).show();
                    return;
            }

            int notificationID =
                    (int) System.currentTimeMillis();

            scheduleNotification(
                    date,
                    title + " is happening today!",
                    notificationID
            );
        });

    }

    private void scheduleNotification(String dateString, String message, int notificationID) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            sdf.setLenient(false);

            Date date = sdf.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Intent intent = new Intent(ExcursionDetails.this, Receiver.class);
            intent.putExtra("message", message);
            intent.putExtra("notificationID", notificationID);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    ExcursionDetails.this,
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

            Toast.makeText(this, "Notification Scheduled", Toast.LENGTH_SHORT).show();


        } catch (ParseException e) {
            Toast.makeText(this, "Could not schedule notification", Toast.LENGTH_SHORT).show();
        }
    }
}

