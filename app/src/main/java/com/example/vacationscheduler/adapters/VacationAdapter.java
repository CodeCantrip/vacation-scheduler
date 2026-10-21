package com.example.vacationscheduler.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.VacationDetails;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;
public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder>{

    private List <Vacation>vacations;

    public VacationAdapter(List<Vacation> vacations){
        this.vacations=vacations;
    }

    @Override

    public VacationViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vacation_list_item,parent,false);
        return  new VacationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VacationViewHolder holder, int position){
        Vacation currentVacation = vacations.get(position);
        holder.vacationTitle.setText(currentVacation.getTitle());
        holder.vacationHotel.setText(currentVacation.getHotel());

        holder.itemView.setOnClickListener(view ->{
            Intent intent = new Intent(view.getContext(), VacationDetails.class);
            intent.putExtra("vacationID", currentVacation.getVacationID());
            intent.putExtra("title", currentVacation.getTitle());
            intent.putExtra("hotel", currentVacation.getHotel());
            intent.putExtra("startDate", currentVacation.getStartDate());
            intent.putExtra("endDate", currentVacation.getEndDate());
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount(){
        return  vacations.size();
    }

    public void setVacations(List<Vacation>vacations){
        this.vacations = vacations;
        notifyDataSetChanged();
    }

    public static class VacationViewHolder extends RecyclerView.ViewHolder {

        private TextView vacationTitle;
        private TextView vacationHotel;


        public VacationViewHolder(View itemView) {
            super(itemView);
            vacationTitle = itemView.findViewById(R.id.vacationItemTitle);
            vacationHotel = itemView.findViewById(R.id.vacationItemHotel);
        }


    }

}
