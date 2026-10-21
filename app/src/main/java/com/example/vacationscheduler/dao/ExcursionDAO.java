package com.example.vacationscheduler.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionID ASC")
    List<Excursion> getAllExcursions();

    @Query("SELECT * FROM excursions WHERE vacationID= :vacationID ORDER BY excursionID ASC")
    List<Excursion> getAssociatedExcursions(int vacationID);

    @Query("SELECT * FROM excursions WHERE title LIKE '%' || :searchTerm || '%' ORDER BY excursionID ASC")
    List<Excursion> searchExcursions(String searchTerm);
}
