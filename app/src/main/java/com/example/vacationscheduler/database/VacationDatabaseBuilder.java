package com.example.vacationscheduler.database;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

import com.example.vacationscheduler.dao.ExcursionDAO;
import com.example.vacationscheduler.dao.VacationDAO;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.entities.Excursion;

@Database(
        entities = {Vacation.class, Excursion.class},
        version = 1,
        exportSchema = false

)

public abstract class VacationDatabaseBuilder extends RoomDatabase{

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;

    public static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            VacationDatabaseBuilder.class,
                            "VacationDatabase.db"
                    ).allowMainThreadQueries().build();


                }
            }
        }
        return INSTANCE;
    }

}
