package com.example.weather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class Fragment2 extends Fragment {

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        final Observer<TempInfo> weatherDataObserver = this::setValues;

        ViewModel model = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        model.getWeatherData().observe(getViewLifecycleOwner(),weatherDataObserver);

    }

    @SuppressLint("SetTextI18n")
    private void setValues(TempInfo info) {
        TextView odczuwalna = view.findViewById(R.id.textViewOdczuwalna);
        odczuwalna.setText(info.getFeelTemp());

        TextView minTemp = view.findViewById(R.id.textViewMin);
        minTemp.setText(info.getMinTemp());

        TextView maxTemp = view.findViewById(R.id.textViewMax);
        maxTemp.setText(info.getMaxTemp());

        TextView cisnienie = view.findViewById(R.id.textViewCisnienie);
        cisnienie.setText(info.getPressureTemp());

        TextView wilgotnosc = view.findViewById(R.id.textViewWilgotnosc);
        wilgotnosc.setText(info.getHumidityTemp());

        TextView opis = view.findViewById(R.id.textViewOpis);
        opis.setText(info.getDescriptionTemp());
    }
}