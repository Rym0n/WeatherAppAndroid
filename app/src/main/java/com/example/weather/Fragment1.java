package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentOnAttachListener;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Fragment1 extends Fragment {


    private Button buttonSzukaj,buttonOdswiez,buttonZmienTemp;
    private TextView textViewMiasto, textViewTemperature, textViewWarunkiPogodowe;
    private EditText editTextData;
    private RecyclerView recyclerViewWeather;
    private ImageView icon;
    private ArrayList<RecyclerViewInfo> recyclerViewInfoTab;
    private RecyclerViewAdapter recyclerViewAdapter;
    private String nazwa, dane, daneWartosci[];
    private ViewModel viewModel;
    private TextView data;

    //DODANIE SHARED PREFERENCES
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor sPEdit;


//    private FragmentBListener listener;
//    public interface FragmentBListener{
//        void onInputBSent(CharSequence input);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(getActivity()).get(ViewModel.class);
        buttonSzukaj = view.findViewById(R.id.button2);
        buttonOdswiez = view.findViewById(R.id.refresh);
        buttonZmienTemp = view.findViewById(R.id.stopnie);
        textViewMiasto = view.findViewById(R.id.miasto);
        textViewTemperature = view.findViewById(R.id.temperatureRecyclerAdapter);
        textViewWarunkiPogodowe = view.findViewById(R.id.warunkiPogodwe);
        editTextData = view.findViewById(R.id.editTextWeather);
        icon = view.findViewById(R.id.Icon);
        recyclerViewWeather = view.findViewById(R.id.RecyclerViewWeather);
        recyclerViewInfoTab = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(recyclerViewInfoTab);
        recyclerViewWeather.setAdapter(recyclerViewAdapter);
        data = view.findViewById(R.id.time);

        sharedPreferences = this.getActivity().getSharedPreferences("Units",Context.MODE_PRIVATE);
        sPEdit = sharedPreferences.edit();


        final int[] zmienTemp = {1};
        ustawNazwa("Lodz");
        getApiInfo(nazwa,zmienTemp);

        getParentFragmentManager().setFragmentResultListener("CheckedChanged", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                int[] x={1};
                String miastoUlubione = result.getString("name");
                getApiInfo(miastoUlubione,x);

            }
        });


        String data_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        data.setText(data_n);
        ustawDane(readFromFile(getActivity()));
        splitDane(pobierzDane());

        buttonSzukaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String miasto = editTextData.getText().toString();
                textViewMiasto.setText(nazwa);
                getApiInfo(miasto,zmienTemp);
                Toast.makeText(getActivity(), "Znaleziono", Toast.LENGTH_SHORT).show();
            }
        });
        buttonOdswiez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String miasto = editTextData.getText().toString();
                textViewMiasto.setText(nazwa);
                getApiInfo(miasto,zmienTemp);
                Toast.makeText(getActivity(), "Znaleziono", Toast.LENGTH_SHORT).show();
            }
        });
        buttonZmienTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(zmienTemp[0] == 1){
                  zmienTemp[0] = 2;
                  Toast.makeText(getActivity(), "Zmieniono na fahrenheit ", Toast.LENGTH_SHORT).show();
                  sPEdit.putString("jednostka","F");
                  sPEdit.commit();
              }
              else if(zmienTemp[0] == 2){
                  zmienTemp[0] = 1;
                  Toast.makeText(getActivity(), "Zmieniono na Celcjusz ", Toast.LENGTH_SHORT).show();
                  sPEdit.putString("jednostka","C");
                  sPEdit.commit();
              }
              buttonOdswiez.callOnClick();

            }
        });
    }



    //WSZYSTKIE METODY
    private void getApiInfo(String miasto , final int[] zmienTemp){
        textViewMiasto.setText(miasto);
        String myUrl = "https://api.openweathermap.org/data/2.5/forecast?q="+miasto+"&appid=61f0ef6fe6c9e68e01580b13f9e8c5cd";
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String temp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp");
                    float floatTemp = (float) (Float.parseFloat(temp) - 273.15);
                    float floatTempFar = (float) (((Float.parseFloat(temp)- 273.15)*9/5)+32);
                    DecimalFormat df = new DecimalFormat("##.#");

//                    if(zmienTemp[0] == 1) {
//                        textViewTemperature.setText(String.valueOf(df.format(floatTemp)) + "°C");
//                    }
//                    if(zmienTemp[0] == 2){
//                        textViewTemperature.setText(String.valueOf(df.format(floatTemp)) + "°F");
//                    }

                    String condition = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("main");
                    String conditionIcon = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");

                    Picasso.get().load("https://openweathermap.org/img/wn/".concat(conditionIcon).concat("@2x.png")).into(icon);


                    textViewWarunkiPogodowe.setText(condition);

                    //zapis dla wiecej danych
                    String tempOdczowalna = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("feels_like");
                    float temperatureFeelFloat = (float) (Float.parseFloat(tempOdczowalna) - 273.15);
                    float temperatureFeelFloatFar = (float) (((Float.parseFloat(tempOdczowalna)- 273.15)*9/5)+32);

                    String tempMin = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_min");

                    float floatTempMin = (float) (Float.parseFloat(tempMin) - 273.15);
                    float floatTempMinFar = (float) (((Float.parseFloat(tempMin)- 273.15)*9/5)+32);

                    String tempMax = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp_max");

                    float floatTempMax = (float) (Float.parseFloat(tempMax) - 273.15);
                    float floatTempMaxFar = (float) (((Float.parseFloat(tempMax)- 273.15)*9/5)+32);

                    String pressure = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("pressure");
                    String humidity = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("humidity");
                    String description = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("description");

                    //zapis do viewModel
                    if(zmienTemp[0] == 1) {
                        viewModel.setWeatherData(new TempInfo(String.valueOf(df.format(temperatureFeelFloat)) + "°C", String.valueOf(df.format(floatTempMin)) + "°C", String.valueOf(df.format(floatTempMax)) + "°C", pressure + "hPa", humidity + "%", description));
                    }
                    else if(zmienTemp[0] == 2){
                        viewModel.setWeatherData(new TempInfo(String.valueOf(df.format(temperatureFeelFloatFar)) + "°F", String.valueOf(df.format(floatTempMinFar)) + "°F", String.valueOf(df.format(floatTempMaxFar)) + "°F", pressure + "hPa", humidity + "%", description));
                    }
                    JSONArray forecastNext = response.getJSONArray("list");

                    for (int i=1;i<9;i++) {
                        JSONObject hourObj = forecastNext.getJSONObject(i);

                        String time = hourObj.getString("dt_txt");

                        String temper = hourObj.getJSONObject("main").getString("temp");
                        floatTemp = (float) (Float.parseFloat(temper) - 273.15);
                        floatTempFar = (float) (((Float.parseFloat(temper)- 273.15)*9/5)+32);

                        if(zmienTemp[0]==1){
                            temper = String.valueOf(df.format(floatTemp));
                        }
                        else if (zmienTemp[0]==2) {
                            temper = String.valueOf(df.format(floatTempFar));
                        }
                        String img = hourObj.getJSONArray("weather").getJSONObject(0).getString("icon");

                        String wind = hourObj.getJSONObject("wind").getString("speed");
                        RecyclerViewInfo recyclerViewInfo = new RecyclerViewInfo(time, temper, img, wind);
                        recyclerViewInfoTab.remove(recyclerViewInfo);
                        recyclerViewInfoTab.add(recyclerViewInfo);


                    }
                    recyclerViewAdapter.notifyDataSetChanged();

                    //zapis do stringa
                    String dt = textViewMiasto.getText().toString() + '\n'
                            + String.valueOf(df.format(floatTemp)) + '\n'
                            + condition + '\n'
                            + conditionIcon + '\n'
                            + String.valueOf(df.format(temperatureFeelFloat)) + '\n'
                            + String.valueOf(df.format(floatTempMin)) + '\n'
                            + String.valueOf(df.format(floatTempMax)) + '\n'
                            + pressure + '\n'
                            + humidity + '\n'
                            + description + '\n';
                    String unit = sharedPreferences.getString("jednostka","");
                    if(unit.equals("C")) {
                        textViewTemperature.setText(String.valueOf(df.format(floatTemp)) + "°C");
                    }
                    if(unit.equals("F")){
                        textViewTemperature.setText(String.valueOf((df.format(floatTempFar))) + "°F");
                    }


                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getActivity().openFileOutput("config.txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(dt);
                        outputStreamWriter.close();
                    } catch (IOException e) {
                        Log.e("Exception", "Błąd w zapisie do pliku: " + e.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                if(error instanceof NoConnectionError)
                {
                    File file = new File(getActivity().getFilesDir(),"config.txt");
                    if(file.exists()) {
                        putDataValues();
                        viewModel.setWeatherData(new TempInfo(daneWartosci[5] + "°C", daneWartosci[6] + "°C", daneWartosci[7] + "°C", daneWartosci[8] + "hPa", daneWartosci[9] + "hPa", daneWartosci[10]));
                    }
                    Toast.makeText(getActivity(), "No conneection",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), "Please enter valid city name...",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private String fileRead(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "NIE ZNALEZIONO PLIKU " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "NIE MOZNA ODCZYTAC PLIKU " + e.toString());
        }

        return ret;
    }

    void ustawNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
    public static String ulubMiasto (String nazwa) {
        return nazwa;
    }
    void ustawDane(String dane) { this.dane = dane; }
    String pobierzDane() { return dane; }

    void splitDane(String str) {
        daneWartosci = str.split(System.lineSeparator());
    }

    public void onResume() {
        super.onResume();
        if(viewModel.getFavoriteCity() != null)
            textViewMiasto.setText(viewModel.getFavoriteCity());
    }
    @SuppressLint("SetTextI18n")
    void putDataValues() {
        textViewMiasto.setText(daneWartosci[1]);
        textViewTemperature.setText(daneWartosci[2] + "°C");
        textViewWarunkiPogodowe.setText(daneWartosci[3]);
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(context instanceof FragmentBListener){
//            listener = (FragmentBListener) context;
//        } else{
//            throw new RuntimeException(context.toString()+"blad");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }


}
