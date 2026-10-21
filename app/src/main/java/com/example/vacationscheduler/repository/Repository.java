package com.example.vacationscheduler.repository;
import android.app.Application;

import com.example.vacationscheduler.database.VacationDatabaseBuilder;
import com.example.vacationscheduler.dao.ExcursionDAO;
import com.example.vacationscheduler.dao.VacationDAO;
import com.example.vacationscheduler.entities.Vacation;
import com.example.vacationscheduler.entities.Excursion;

import java.util.List;

public class Repository {

    private VacationDAO vacationDAO;
    private ExcursionDAO excursionDAO;

    private List<Vacation> allVacations;
    private List<Excursion> allExcursions;

    public Repository(Application application){
        VacationDatabaseBuilder db =VacationDatabaseBuilder.getDatabase(application);
        vacationDAO = db.vacationDAO();
        excursionDAO = db.excursionDAO();

    }

    public List<Vacation> getAllVacations() {
        allVacations =vacationDAO.getAllVacations();
        return allVacations;
    }

    public List<Vacation> searchVacations( String searchTerm){
        return vacationDAO.searchVacations(searchTerm);
    }

    public Vacation getVacationByID(int vacationID){
        return vacationDAO.getVacationByID(vacationID);
    }
    public List<Excursion> getAllExcursions() {
        allExcursions = excursionDAO.getAllExcursions();
        return allExcursions;
    }

    public List<Excursion> getAssociatedExcursions(int vacationID){
        return excursionDAO.getAssociatedExcursions(vacationID);
    }

    public List<Excursion> searchExcursions(String searchTerm) {
        return excursionDAO.searchExcursions(searchTerm);
    }

    public void insert(Vacation vacation){
        vacationDAO.insert(vacation);
    }

    public void update(Vacation vacation) {
        vacationDAO.update(vacation);
    }

    public void delete(Vacation vacation) {
        vacationDAO.delete(vacation);
    }

    public void insert(Excursion excursion) {
        excursionDAO.insert(excursion);
    }

    public void update(Excursion excursion) {
        excursionDAO.update(excursion);
    }
    public void delete(Excursion excursion) {
        excursionDAO.delete(excursion);
    }
}
