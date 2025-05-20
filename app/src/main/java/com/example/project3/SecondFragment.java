package com.example.project3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SecondFragment extends Fragment {

    RecyclerView recyclerView;
    Button btnToggleFavorite;
    List<WorkoutModel> workoutList;
    WorkoutAdapter adapter;
    boolean showingFavorites = false;

    private static final String PREFS_NAME = "workout_prefs";
    private static final String KEY_FAVORITES = "favorite_workouts";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        recyclerView = view.findViewById(R.id.recycler_workout);
        btnToggleFavorite = view.findViewById(R.id.btn_toggle_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        workoutList = getAllWorkouts();
        adapter = new WorkoutAdapter(getContext(), workoutList);
        recyclerView.setAdapter(adapter);

        btnToggleFavorite.setOnClickListener(v -> {
            showingFavorites = !showingFavorites;
            if (showingFavorites) {
                btnToggleFavorite.setText("Lihat Semua");
                adapter.updateList(getFavoriteWorkouts());
            } else {
                btnToggleFavorite.setText("Lihat Favorite");
                adapter.updateList(getAllWorkouts());
            }
        });

        return view;
    }

    private List<WorkoutModel> getAllWorkouts() {
        List<WorkoutModel> list = new ArrayList<>();
        list.add(new WorkoutModel("pushup",
                "Push Up",
                "Latihan dasar kekuatan tubuh bagian atas.",
                "Melatih otot dada, bahu, dan trisep.",
                "Posisi plank, turunkan badan, dorong naik.",
                "3 Set x 15 Reps",
                R.drawable.fit_pushup, "1 Menit"));

        list.add(new WorkoutModel("plank",
                "Plank",
                "Menahan tubuh dalam posisi lurus.",
                "Menguatkan otot core dan punggung.",
                "Posisikan lengan di bawah bahu, tahan posisi.",
                "3 Set x 1 Menit",
                R.drawable.fit_plank, "1 Menit"));

        list.add(new WorkoutModel("squats",
                "Squats",
                "Latihan untuk kaki dan otot paha.",
                "Menguatkan otot paha, gluteus, dan pinggul.",
                "Posisi berdiri, turunkan badan seperti duduk, dan angkat kembali.",
                "4 Set x 20 Reps",
                R.drawable.fit_squats, "1 Menit"));

        list.add(new WorkoutModel("lunges",
                "Lunges",
                "Melangkah ke depan dan menurunkan tubuh.",
                "Meningkatkan kekuatan paha dan gluteus.",
                "Langkahkan kaki kanan ke depan, tekuk kedua lutut, dan angkat kembali.",
                "3 Set x 12 Reps per Kaki",
                R.drawable.fit_lunges, "1 Menit"));

        list.add(new WorkoutModel("burpees",
                "Burpees",
                "Latihan full-body yang melibatkan lompat dan push up.",
                "Meningkatkan kekuatan seluruh tubuh dan daya tahan.",
                "Mulai berdiri, turun ke squat, lakukan push-up, lalu lompat.",
                "3 Set x 10 Reps",
                R.drawable.fit_burpees, "1 Menit"));

        list.add(new WorkoutModel("mountain_climbers",
                "Mountain Climbers",
                "Latihan kardio untuk tubuh bagian atas dan core.",
                "Mengencangkan otot perut, lengan, dan bahu.",
                "Posisi push-up, tarik kaki kanan ke depan, ganti kaki.",
                "3 Set x 1 Menit",
                R.drawable.fit_mountain_climbers, "1 Menit"));

        list.add(new WorkoutModel("bicycle_crunches",
                "Bicycle Crunches",
                "Latihan abs yang melibatkan putaran tubuh.",
                "Mengencangkan otot perut dan meningkatkan keseimbangan.",
                "Posisi telentang, tekuk lutut, lakukan gerakan seperti mengayuh sepeda.",
                "4 Set x 20 Reps",
                R.drawable.fit_bicycle_crunches, "1 Menit"));
        return list;
    }

    // Ambil data workout yang difavoritkan
    private List<WorkoutModel> getFavoriteWorkouts() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favoriteIds = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
        List<WorkoutModel> all = getAllWorkouts();
        List<WorkoutModel> favorites = new ArrayList<>();
        for (WorkoutModel model : all) {
            if (favoriteIds.contains(model.getId())) {
                favorites.add(model);
            }
        }
        return favorites;
    }
}
