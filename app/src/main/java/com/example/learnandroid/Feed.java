package com.example.learnandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // ĐÃ THÊM IMPORT TOOLBAR

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Feed extends AppCompatActivity {

    private EditText edtIdea;
    private Button btnPost;
    private ListView listViewIdeas;

    private ArrayList<Idea> ideaList;
    private ArrayAdapter<Idea> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // ==========================================
        // KÍCH HOẠT TOOLBAR ĐỂ HIỆN MENU GÓC PHẢI
        // ==========================================
        Toolbar toolbar = findViewById(R.id.toolbarFeed);
        setSupportActionBar(toolbar);

        // 1. Ánh xạ View
        edtIdea = findViewById(R.id.edtIdea);
        btnPost = findViewById(R.id.btnPost);
        listViewIdeas = findViewById(R.id.listViewIdeas);

        // 2. Tải dữ liệu cũ từ bộ nhớ lên
        loadData();

        // 3. Cài đặt Adapter để vẽ danh sách
        adapter = new ArrayAdapter<Idea>(this, R.layout.item_idea, ideaList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_idea, parent, false);
                }

                Idea currentIdea = ideaList.get(position);

                TextView tvAuthor = convertView.findViewById(R.id.tvAuthor);
                TextView tvDate = convertView.findViewById(R.id.tvDate);
                TextView tvContent = convertView.findViewById(R.id.tvContent);

                tvAuthor.setText(currentIdea.getAuthor());
                tvDate.setText(currentIdea.getDate());
                tvContent.setText(currentIdea.getContent());

                return convertView;
            }
        };
        listViewIdeas.setAdapter(adapter);

        // 4. Xử lý nút Đăng bài (Post)
        btnPost.setOnClickListener(v -> {
            String content = edtIdea.getText().toString().trim();
            if (!content.isEmpty()) {
                String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                // Lấy email người dùng đang đăng nhập để làm tên Author (hoặc để "My Account")
                SharedPreferences prefs = getSharedPreferences("ThongTin", MODE_PRIVATE);
                String currentAuthor = prefs.getString("Email", "My Account");

                ideaList.add(0, new Idea(currentAuthor, currentDate, content));
                adapter.notifyDataSetChanged();
                edtIdea.setText("");

                saveData();
                Toast.makeText(Feed.this, "Đã đăng bài!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Feed.this, "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show();
            }
        });

        // 5. Đăng ký Context Menu cho ListView
        registerForContextMenu(listViewIdeas);
    }

    // ==========================================
    // XỬ LÝ OPTION MENU (Góc phải trên)
    // ==========================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opt_profile) {
            // ĐÃ CẬP NHẬT: Bấm vào Profile trên Menu -> Mở màn hình Profile
            Intent profileIntent = new Intent(Feed.this, Profile.class);
            startActivity(profileIntent);
            return true;

        } else if (id == R.id.opt_sort_date) {
            Toast.makeText(this, "Sắp xếp theo ngày", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.opt_sort_author) {
            Toast.makeText(this, "Sắp xếp theo tác giả", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ==========================================
    // XỬ LÝ CONTEXT MENU
    // ==========================================
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Tùy chọn bài viết");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        int id = item.getItemId();
        if (id == R.id.ctx_detail) {
            Toast.makeText(this, "Chi tiết bài của: " + ideaList.get(position).getAuthor(), Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.ctx_hide) {
            ideaList.remove(position);
            adapter.notifyDataSetChanged();
            saveData();
            Toast.makeText(this, "Đã ẩn bài viết", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // ==========================================
    // HÀM LƯU VÀ TẢI DỮ LIỆU (Dùng Gson)
    // ==========================================
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("FeedData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ideaList);
        editor.putString("idea_list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("FeedData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("idea_list", null);
        Type type = new TypeToken<ArrayList<Idea>>() {}.getType();
        ideaList = gson.fromJson(json, type);

        if (ideaList == null) {
            ideaList = new ArrayList<>();
        }
    }
}