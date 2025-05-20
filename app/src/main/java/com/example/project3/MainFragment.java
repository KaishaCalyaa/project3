package com.example.project3;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.project3.R;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button btnStartWorkout = view.findViewById(R.id.btn_start_workout);
        Button btnProfile = view.findViewById(R.id.btn_profile);

        btnStartWorkout.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(1);
            } else {
                Toast.makeText(getContext(), "Gagal membuka workout.", Toast.LENGTH_SHORT).show();
            }
        });


        btnProfile.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).switchToTab(2);
            } else {
                Toast.makeText(getContext(), "Gagal membuka profil.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
