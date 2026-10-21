package com.example.vacationscheduler.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.ExcursionDetails;
import com.example.vacationscheduler.R;
import com.example.vacationscheduler.entities.Excursion;

import java.util.List;
public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder>{

    private List <Excursion>excursions;

    public ExcursionAdapter(List<Excursion> excursions){
        this.excursions= excursions;
    }

    @Override

    public ExcursionViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.excursion_list_item,parent,false);
        return  new ExcursionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExcursionViewHolder holder, int position){
        Excursion currentExcursion = excursions.get(position);
        holder.excursionTitle.setText(currentExcursion.getTitle());
        holder.excursionDate.setText(currentExcursion.getDate());


        holder.itemView.setOnClickListener(view ->{
            Intent intent = new Intent(view.getContext(), ExcursionDetails.class);
            intent.putExtra("excursionID", currentExcursion.getExcursionID());
            intent.putExtra("title", currentExcursion.getTitle());
            intent.putExtra("date", currentExcursion.getDate());
            intent.putExtra("vacationID", currentExcursion.getVacationID());
            view.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount(){
        return  excursions.size();
    }

    public void setExcursions(List<Excursion>excursions){
        this.excursions = excursions;
        notifyDataSetChanged();
    }

    public static class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private TextView excursionTitle;
        private TextView excursionDate;


        public ExcursionViewHolder(View itemView) {
            super(itemView);
            excursionTitle = itemView.findViewById(R.id.excursionItemTitle);
            excursionDate = itemView.findViewById(R.id.excursionItemDate);
        }


    }

}