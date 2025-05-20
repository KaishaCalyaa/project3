package com.example.project3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private TextView tvNama, tvUmur, tvTinggi, tvBerat, tvBMI, tvTargetBerat, tvProgress;
    private TextView tvKalori, tvLatihan;
    private Button btnEditProfile, btnLogout;

    private String nama = "Calya";
    private int umur = 17;
    private int tinggi = 165;
    private int berat = 55;

    private SharedPreferences prefs;

    public ProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNama = view.findViewById(R.id.tv_nama);
        tvUmur = view.findViewById(R.id.tv_umur);
        tvTinggi = view.findViewById(R.id.tv_tinggi);
        tvBerat = view.findViewById(R.id.tv_berat);
        tvBMI = view.findViewById(R.id.tv_bmi);
        tvTargetBerat = view.findViewById(R.id.tv_target_berat);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvKalori = view.findViewById(R.id.tvKalori);
        tvLatihan = view.findViewById(R.id.tvLatihan);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);
        btnLogout = view.findViewById(R.id.btn_logout); // Tambahkan ini

        prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE);

        loadProfileFromPrefs();
        updateProfileUI();
        tampilkanStatistikMingguan();

        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        // Tambahkan logika tombol Logout
        btnLogout.setOnClickListener(v -> {
            // Hapus semua data profil
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Kembali ke LoginActivity
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // supaya tidak bisa kembali ke MainActivity
            startActivity(intent);

            requireActivity().finish(); // Tutup activity sekarang
        });
    }

    private void loadProfileFromPrefs() {
        nama = prefs.getString("name", nama);
        umur = prefs.getInt("age", umur);
        tinggi = prefs.getInt("height", tinggi);
        berat = prefs.getInt("weight", berat);
    }

    private void saveProfileToPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("name", nama);
        editor.putInt("age", umur);
        editor.putInt("height", tinggi);
        editor.putInt("weight", berat);
        editor.apply();
    }

    private void updateProfileUI() {
        tvNama.setText("Nama: " + nama);
        tvUmur.setText("Umur: " + umur);
        tvTinggi.setText("Tinggi: " + tinggi + " cm");
        tvBerat.setText("Berat: " + berat + " kg");

        double tinggiM = tinggi / 100.0;
        double bmi = berat / (tinggiM * tinggiM);
        tvBMI.setText(String.format("BMI: %.1f", bmi));

        tvTargetBerat.setText("Target Berat: 50 kg");
        tvProgress.setText("Progress Mingguan: 3 sesi selesai");
    }

    private void tampilkanStatistikMingguan() {
        SharedPreferences prefsProgress = requireContext().getSharedPreferences("progress", Context.MODE_PRIVATE);
        int totalKaloriMingguan = 0;
        int totalLatihanMingguan = 0;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i < 7; i++) {
            String tanggal = sdf.format(calendar.getTime());
            totalKaloriMingguan += prefsProgress.getInt("kalori_" + tanggal, 0);
            totalLatihanMingguan += prefsProgress.getInt("latihan_" + tanggal, 0);
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        tvKalori.setText("Total Kalori\n" + totalKaloriMingguan + " kcal");
        tvLatihan.setText("Jumlah Latihan\n" + totalLatihanMingguan + " sesi");
    }

    private void showEditProfileDialog() {
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputNama = new EditText(getContext());
        inputNama.setHint("Nama");
        inputNama.setText(nama);
        layout.addView(inputNama);

        final EditText inputUmur = new EditText(getContext());
        inputUmur.setHint("Umur");
        inputUmur.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputUmur.setText(String.valueOf(umur));
        layout.addView(inputUmur);

        final EditText inputTinggi = new EditText(getContext());
        inputTinggi.setHint("Tinggi (cm)");
        inputTinggi.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputTinggi.setText(String.valueOf(tinggi));
        layout.addView(inputTinggi);

        final EditText inputBerat = new EditText(getContext());
        inputBerat.setHint("Berat (kg)");
        inputBerat.setInputType(InputType.TYPE_CLASS_NUMBER);
        inputBerat.setText(String.valueOf(berat));
        layout.addView(inputBerat);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Profil");
        builder.setView(layout);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String newNama = inputNama.getText().toString().trim();
            String umurStr = inputUmur.getText().toString().trim();
            String tinggiStr = inputTinggi.getText().toString().trim();
            String beratStr = inputBerat.getText().toString().trim();

            if (newNama.isEmpty() || umurStr.isEmpty() || tinggiStr.isEmpty() || beratStr.isEmpty()) {
                Toast.makeText(getContext(), "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                umur = Integer.parseInt(umurStr);
                tinggi = Integer.parseInt(tinggiStr);
                berat = Integer.parseInt(beratStr);
                nama = newNama;

                saveProfileToPrefs();
                updateProfileUI();
                Toast.makeText(getContext(), "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Masukkan angka yang valid", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
