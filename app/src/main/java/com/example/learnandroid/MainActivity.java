package com.example.learnandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText tiePass;
    private MaterialButton btnLogin;
    private TextInputEditText tieEmail;
    private LottieAnimationView lottieLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tiePass = findViewById(R.id.tiePass);
        btnLogin = findViewById(R.id.btnLogin);
        tieEmail = findViewById(R.id.tieEmail);

        btnLogin.setOnClickListener(v -> {
            String Pass = tiePass.getText().toString().trim();
            String Email = tieEmail.getText().toString().trim();

            SharedPreferences sharedPreferences = getSharedPreferences("ThongTin", MODE_PRIVATE);
            String EmailDangKy = sharedPreferences.getString("Email", "");
            String PassDangKy = sharedPreferences.getString("Pass", "");

            if (Email.isEmpty() || Pass.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }

            if (Email.equals(EmailDangKy) && Pass.equals(PassDangKy)) {
                Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                Intent profileIntent = new Intent(MainActivity.this, Profile.class);
                startActivity(profileIntent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}