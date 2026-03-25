package com.example.learnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView; // Nhớ import ImageView
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Profile extends AppCompatActivity {
    private TextInputEditText tieName;
    private TextInputEditText tieEmail;
    private TextInputEditText tieAddress;
    private TextInputEditText tieAvatar;
    private TextInputEditText tieDescription;
    private MaterialButton btnSave;
    private MaterialButton btnLogout;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        tieName = findViewById(R.id.tieName);
        tieEmail = findViewById(R.id.tieEmail);
        tieAddress = findViewById(R.id.tieAddress);
        tieAvatar = findViewById(R.id.tieAvatar);
        tieDescription = findViewById(R.id.tieDiscription);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        imgAvatar = findViewById(R.id.ivProfile);
        // Gọi hàm dỡ hàng Intent
        NhanDuLieu();

        // 2. SỰ KIỆN NÚT LƯU
        btnSave.setOnClickListener(v -> {
            String tenMoi = tieName.getText().toString().trim();
            String emailMoi = tieEmail.getText().toString().trim();
            String diaChiMoi = tieAddress.getText().toString().trim();
            String urlMoi = tieAvatar.getText().toString().trim();
            String moTaMoi = tieDescription.getText().toString().trim();

            SharedPreferences sharedPreferences = getSharedPreferences("ThongTin", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Name", tenMoi);
            editor.putString("Email", emailMoi);
            editor.putString("Address", diaChiMoi);
            editor.putString("Avatar", urlMoi);
            editor.putString("Description", moTaMoi);
            editor.apply();

            if (!urlMoi.isEmpty()) {
                Glide.with(Profile.this)
                        .load(urlMoi)
                        .into(imgAvatar);
            } else {
                imgAvatar.setImageResource(android.R.color.transparent);
            }

            Toast.makeText(Profile.this, "Đã lưu thông tin và cập nhật ảnh!", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v->
        {
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);
            finish();

        });
    }

    private void NhanDuLieu() {
        SharedPreferences sharedPreferences = getSharedPreferences("ThongTin", MODE_PRIVATE);
        String savedName = sharedPreferences.getString("Name", "");
        String savedEmail = sharedPreferences.getString("Email", "");
        String savedAddress = sharedPreferences.getString("Address", "");
        String savedAvatar = sharedPreferences.getString("Avatar", "");
        String savedDesc = sharedPreferences.getString("Description", "");

        if (!savedName.isEmpty() || !savedEmail.isEmpty() || !savedAddress.isEmpty() || !savedAvatar.isEmpty() || !savedDesc.isEmpty()) {
            tieName.setText(savedName);
            tieEmail.setText(savedEmail);
            tieAddress.setText(savedAddress);
            tieAvatar.setText(savedAvatar);
            tieDescription.setText(savedDesc);

            // Bơm ảnh Avatar
            if (!savedAvatar.isEmpty()) {
                Glide.with(Profile.this).load(savedAvatar).into(imgAvatar);
            }
        } else {

            Intent intent = getIntent();
            if (intent != null) {
                String tenNhanDuoc = intent.getStringExtra("name");
                String emailNhanDuoc = intent.getStringExtra("Email");

                if (tenNhanDuoc != null) tieName.setText(tenNhanDuoc);
                if (emailNhanDuoc != null) tieEmail.setText(emailNhanDuoc);
            }
        }
    }
}