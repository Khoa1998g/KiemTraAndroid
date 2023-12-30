package com.example.kiemtraandroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SanPhamAdapter sanPhamAdapter;
    private SanPhamDAO sanPhamDAO;
    TextView edtMaSP, edtTenSP, edtPrice, edtImageName;
    Button btnClear, btnSave;
    ImageButton imageDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize SanPhamDAO
        sanPhamDAO = new SanPhamDAO(this);

        // Get data from DAO and set up the adapter
        ArrayList<SanPham> listSanPham = sanPhamDAO.getListSanPham();
        sanPhamAdapter = new SanPhamAdapter(this, listSanPham, sanPhamDAO);
        recyclerView.setAdapter(sanPhamAdapter);

        View customDialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_items, null);
        btnSave = customDialogView.findViewById(R.id.btnSave);
        edtMaSP = customDialogView.findViewById(R.id.edtMaSP);
        edtTenSP = customDialogView.findViewById(R.id.edtTenSP);
        edtPrice = customDialogView.findViewById(R.id.edtPrice);
        btnClear = customDialogView.findViewById(R.id.btnClear);
        edtImageName = customDialogView.findViewById(R.id.edtImageName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ các trường nhập
                String tenSP = edtTenSP.getText().toString();
                String giaTien = edtPrice.getText().toString();
                String imageName = "https://recipes.net/wp-content/uploads/2020/04/fish-fillets-lorange-recipe-scaled.jpg";

                // Tạo một đối tượng SanPham mới từ dữ liệu nhập vào
                SanPham sanPham = new SanPham(null, tenSP, giaTien, imageName); // null cho MaSP vì đây là autoincrement

                // Thêm dữ liệu vào bảng SanPham thông qua SanPhamDAO
                boolean isInserted = sanPhamDAO.Add(sanPham);

                // Kiểm tra và thông báo kết quả
                if (isInserted) {
                    Toast.makeText(MainActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    // Đoạn này có thể thực hiện cập nhật giao diện hoặc thực hiện các hành động khác sau khi thêm sản phẩm thành công
                } else {
                    Toast.makeText(MainActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Other view initializations and button clicks can go here
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallBack(sanPhamAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        FloatingActionButton flButton = findViewById(R.id.flButton);
        flButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View customDialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_items, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(customDialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

}