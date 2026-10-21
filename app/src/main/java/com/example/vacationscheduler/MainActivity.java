package com.example.vacationscheduler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.adapters.ExcursionAdapter;
import com.example.vacationscheduler.adapters.VacationAdapter;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.repository.Repository;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Repository repository;
    private VacationAdapter adapter;

    private ExcursionAdapter excursionAdapter;

    private TableLayout tableLayout;

    private List<Vacation> vacationResults = new ArrayList<>();
    private List<Excursion> excursionResults = new ArrayList<>();

    private TextView reportTimestamp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new Repository(getApplication());

        List<Vacation> allVacations = repository.getAllVacations();
        SearchView searchView = findViewById(R.id.vacationSearchView);
        RecyclerView recyclerView = findViewById(R.id.vacationRecyclerView);
        reportTimestamp = findViewById(R.id.reportTimestamp);
        tableLayout = findViewById(R.id.reportTable);
        LinearLayout reportLayout = findViewById(R.id.reportLayout);
        Button addVacationButton = findViewById(R.id.addVacationButton);
        Button generateReportButton = findViewById(R.id.generateReportButton);
        Button closeReportButton = findViewById(R.id.closeReportButton);
        adapter = new VacationAdapter(allVacations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        RecyclerView excursionRecyclerView = findViewById(R.id.excursionRecyclerView);
        excursionAdapter = new ExcursionAdapter(new ArrayList<>());
        excursionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        excursionRecyclerView.setAdapter(excursionAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
                public boolean onQueryTextSubmit(String Query) {
                return false;
            }

            @Override
                public boolean onQueryTextChange(String newText){

                if (newText.isEmpty()) {
                    generateReportButton.setVisibility(View.GONE);
                    adapter.setVacations(repository.getAllVacations());
                    excursionAdapter.setExcursions(new ArrayList<>());

                    vacationResults = new ArrayList<>();
                    excursionResults = new ArrayList<>();
                }
                else {
                    generateReportButton.setVisibility(View.VISIBLE);
                    vacationResults = repository.searchVacations(newText);
                    excursionResults = repository.searchExcursions(newText);

                    adapter.setVacations(vacationResults);
                    excursionAdapter.setExcursions(excursionResults);
                }
                return true;
            }
        });



        addVacationButton.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this,VacationDetails.class);
            startActivity(intent);
        });

        generateReportButton.setOnClickListener(view ->{

            tableLayout.removeAllViews();
            generateReport(vacationResults, excursionResults);
            reportLayout.setVisibility(View.VISIBLE);
        });

        closeReportButton.setOnClickListener(view ->{
            reportLayout.setVisibility(View.GONE);
            tableLayout.removeAllViews();

        });


    }

    public void generateReport(List<Vacation> vacationResults,
                               List<Excursion> excursionResults) {

        SimpleDateFormat sdf =
                new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.US);

        reportTimestamp.setText(
                "Generated: " + sdf.format(new Date())
        );
        if(!vacationResults.isEmpty()) {

            TableRow headerRow = new TableRow(this);

            TextView titleHeader = new TextView(this);
            titleHeader.setText("Title");

            TextView hotelHeader = new TextView(this);
            hotelHeader.setText("Hotel");

            TextView startHeader = new TextView(this);
            startHeader.setText("Start Date");

            TextView endHeader = new TextView(this);
            endHeader.setText("End Date");

            headerRow.addView(titleHeader);
            headerRow.addView(hotelHeader);
            headerRow.addView(startHeader);
            headerRow.addView(endHeader);

            tableLayout.addView(headerRow);


            for (Vacation vacation : vacationResults) {
                TableRow row = new TableRow(this);

                TextView titleCell = new TextView(this);
                titleCell.setText(vacation.getTitle());

                TextView hotelCell = new TextView(this);
                hotelCell.setText(vacation.getHotel());

                TextView startDateCell = new TextView(this);
                startDateCell.setText(vacation.getStartDate());

                TextView endDateCell = new TextView(this);
                endDateCell.setText(vacation.getEndDate());

                row.addView(titleCell);
                row.addView(hotelCell);
                row.addView(startDateCell);
                row.addView(endDateCell);

                tableLayout.addView(row);
            }
        }

        if(!excursionResults.isEmpty()) {
            TableRow headerExcursionRow = new TableRow(this);

            TextView excursionTitleHeader = new TextView(this);
            excursionTitleHeader.setText("Title");


            TextView excursionStartHeader = new TextView(this);
            excursionStartHeader.setText("Start Date");

            TextView excursionVacationIdHeader = new TextView(this);
            excursionVacationIdHeader.setText("Vacation ID");

            headerExcursionRow.addView(excursionTitleHeader);
            headerExcursionRow.addView(excursionStartHeader);
            headerExcursionRow.addView(excursionVacationIdHeader);


            tableLayout.addView(headerExcursionRow);


            for (Excursion excursion : excursionResults) {
                TableRow row = new TableRow(this);

                TextView titleCell = new TextView(this);
                titleCell.setText(excursion.getTitle());

                TextView excursionStartDate = new TextView(this);
                excursionStartDate.setText(excursion.getDate());


                TextView vacationID = new TextView(this);
                vacationID.setText(String.valueOf(excursion.getVacationID()));

                row.addView(titleCell);
                row.addView(excursionStartDate);
                row.addView(vacationID);


                tableLayout.addView(row);


            }
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.setVacations(repository.getAllVacations());
    }
}