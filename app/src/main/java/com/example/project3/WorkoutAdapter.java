package com.example.project3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private Context context;
    private List<WorkoutModel> workoutList;
    private Set<String> favoriteIds = new HashSet<>();

    private static final String PREFS_NAME = "workout_prefs";
    private static final String KEY_FAVORITES = "favorite_workouts";

    public WorkoutAdapter(Context context, List<WorkoutModel> workoutList) {
        this.context = context;
        this.workoutList = workoutList;

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        favoriteIds = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
    }

    public void setFavoriteIds(Set<String> newFavoriteIds) {
        favoriteIds = new HashSet<>(newFavoriteIds);
        notifyDataSetChanged();
    }

    public void updateList(List<WorkoutModel> newList) {
        workoutList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutModel model = workoutList.get(position);

        holder.title.setText(model.getTitle());
        holder.description.setText("Pengertian: " + model.getDescription());
        holder.benefits.setText("Manfaat: " + model.getBenefits());
        holder.steps.setText("Cara: " + model.getSteps());
        holder.setsReps.setText(model.getSetsReps());
        holder.timer.setText("Durasi: " + model.getTimer());
        holder.image.setImageResource(model.getImageResId());

        // Tombol timer
        holder.startButton.setOnClickListener(v -> {
            if (holder.countDownTimer != null) {
                holder.countDownTimer.cancel();
            }

            long durationMillis = 60 * 1000;
            try {
                String[] split = model.getTimer().split(" ");
                int minute = Integer.parseInt(split[0]);
                durationMillis = minute * 60 * 1000;
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.startButton.setEnabled(false);

            holder.countDownTimer = new CountDownTimer(durationMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    String timeFormatted = String.format("%02d:%02d",
                            seconds / 60, seconds % 60);
                    holder.timer.setText("Sisa Waktu: " + timeFormatted);
                }

                @Override
                public void onFinish() {
                    holder.timer.setText("Selesai!");
                    holder.startButton.setEnabled(true);
                }
            }.start();
        });

        // Logika favorite
        boolean isFavorite = favoriteIds.contains(model.getId());
        holder.favoriteButton.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        holder.favoriteButton.setOnClickListener(v -> {
            SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            Set<String> currentFavorites = prefs.getStringSet(KEY_FAVORITES, new HashSet<>());
            Set<String> newFavorites = new HashSet<>(currentFavorites);

            if (isFavorite) {
                newFavorites.remove(model.getId());
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite_border);
            } else {
                newFavorites.add(model.getId());
                holder.favoriteButton.setImageResource(R.drawable.ic_favorite);
            }

            prefs.edit().putStringSet(KEY_FAVORITES, newFavorites).apply();
            favoriteIds = newFavorites;

        });
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, benefits, steps, setsReps, timer;
        ImageView image;
        Button startButton;
        CountDownTimer countDownTimer;
        ImageView favoriteButton;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txt_title);
            description = itemView.findViewById(R.id.txt_description);
            benefits = itemView.findViewById(R.id.txt_benefits);
            steps = itemView.findViewById(R.id.txt_steps);
            setsReps = itemView.findViewById(R.id.txt_sets_reps);
            timer = itemView.findViewById(R.id.txt_timer);
            image = itemView.findViewById(R.id.img_workout);
            startButton = itemView.findViewById(R.id.btn_start_timer);
            favoriteButton = itemView.findViewById(R.id.btn_favorite);
        }
    }
}
