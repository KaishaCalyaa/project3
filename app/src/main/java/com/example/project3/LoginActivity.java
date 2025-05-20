package com.example.project3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project3.MainActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etName, etAge, etHeight, etWeight;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek apakah data user sudah tersimpan di SharedPreferences "user_profile"
        SharedPreferences prefs = getSharedPreferences("user_profile", Context.MODE_PRIVATE);
        if (prefs.contains("name")) {
            // Jika sudah login (data sudah ada), langsung ke MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.et_name);
        etAge = findViewById(R.id.et_age);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String age = etAge.getText().toString().trim();
            String height = etHeight.getText().toString().trim();
            String weight = etWeight.getText().toString().trim();

            if (name.isEmpty() || age.isEmpty() || height.isEmpty() || weight.isEmpty()) {
                Toast.makeText(this, "Mohon isi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data user ke SharedPreferences "user_profile"
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", name);
            editor.putInt("age", Integer.parseInt(age));
            editor.putInt("height", Integer.parseInt(height));   // ubah ke int supaya konsisten dengan ProfileFragment
            editor.putInt("weight", Integer.parseInt(weight));   // ubah ke int supaya konsisten dengan ProfileFragment
            editor.apply();

            // Pindah ke MainActivity setelah data tersimpan
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}
