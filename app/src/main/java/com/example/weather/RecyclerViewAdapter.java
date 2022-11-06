package com.example.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<RecyclerViewInfo> weatherRVModalArrayList;

    public RecyclerViewAdapter(ArrayList<RecyclerViewInfo> weatherRVModalArrayList) {
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewwidget,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        RecyclerViewInfo modal = weatherRVModalArrayList.get(position);
        holder.tempRA.setText(modal.getTemperature() + "°C/°F");
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.iconRA);
        holder.wiatrRA.setText(modal.getWindSpeed() + "m/s");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");
        try {
            Date t = input.parse(modal.getTime());
            holder.czasRA.setText(output.format(t));
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView wiatrRA, tempRA, czasRA;
        private ImageView iconRA;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            wiatrRA = itemView.findViewById(R.id.wiatr);
            tempRA = itemView.findViewById(R.id.temperatureRecyclerAdapter);
            czasRA = itemView.findViewById(R.id.czas);
            iconRA = itemView.findViewById(R.id.iconRecyclerAdapter);
        }
    }
}
