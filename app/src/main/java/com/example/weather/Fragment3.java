package com.example.weather;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class Fragment3 extends Fragment {

    static ListView listView;
    static ArrayList<String> miasta;
    static ListViewAdapter adapter;
    Toast t;
    EditText input;
    ImageView enter;

//    private FragmentAListener listener;
//    public interface FragmentAListener{
//        void onInputASent(CharSequence input);
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);

        listView = view.findViewById(R.id.listViewUlubione);
        input = view.findViewById(R.id.editTextUlubione);
        enter = view.findViewById(R.id.add);
        miasta = new ArrayList<>();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = miasta.get(position);
                makeToast(name);
//                listener.onInputASent(name);
                Bundle result = new Bundle();
                result.putString("name", name);
                getParentFragmentManager().setFragmentResult("CheckedChanged",result);


            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if(text == null || text.length() == 0){
                    makeToast("Wpisz ponownie");
                }   else {
                        addMiasto(text);
                        input.setText("");


                    }
                }

        });

        adapter = new ListViewAdapter(getContext(),miasta);
        listView.setAdapter(adapter);
        return view;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(context instanceof FragmentAListener){
//            listener = (FragmentAListener) context;
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

    public void addMiasto(String miasto){
        miasta.add(miasto);
        listView.setAdapter(adapter);

    }
    public static void removeMiasto(int remove){
        miasta.remove(remove);
        listView.setAdapter(adapter);
    }
    private void makeToast(String s){
        if(t!=null) t.cancel();
        t = Toast.makeText(getContext(),s,Toast.LENGTH_SHORT);
        t.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}