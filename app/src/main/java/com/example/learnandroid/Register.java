package com.example.learnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    private EditText edtEmail, edtPassword, edtName, edtConfirmPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // 2. Ánh xạ ID từ XML sang Java (Huy nhớ đổi ID cho khớp với file XML của bạn nhé)
        edtEmail = findViewById(R.id.tieEmail);
        edtPassword = findViewById(R.id.tiePass);
        edtConfirmPassword = findViewById(R.id.tieCfPass);
        btnRegister = findViewById(R.id.btnRegister);
        edtName = findViewById(R.id.tieName);

        // =========================================================
        // 3. XỬ LÝ SỰ KIỆN KHI BẤM NÚT "ĐĂNG KÝ"
        // =========================================================
        btnRegister.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            android.content.SharedPreferences sharedPreferences = getSharedPreferences("ThongTin", MODE_PRIVATE);
            android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Email", email);
            editor.putString("Pass", password);
            editor.putString("Name", name);
            editor.apply();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!password.equals(confirmPassword)) {
                Toast.makeText(Register.this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, MainActivity.class);
            intent.putExtra("EMAIL_DANG_KY", email);

            startActivity(intent);

            finish();
        });

    }
}