package com.example.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {
    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    @Query("SELECT * FROM vacations WHERE vacationID = :vacationID")
    Vacation getVacationByID(int vacationID);

    @Query("SELECT * FROM vacations WHERE title LIKE '%' || :searchTerm || '%' ORDER BY vacationID ASC")
    List<Vacation>searchVacations(String searchTerm);
}
