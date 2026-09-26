package com.nkumba.roms.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.nkumba.roms.R;

public class MainActivity extends AppCompatActivity {

    private TextView tvRoleHeader;
    private Button btnManageProducts, btnCreateOrder, btnOrderHistory, btnCreateUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvRoleHeader = findViewById(R.id.tvRoleHeader);
        btnManageProducts = findViewById(R.id.btnManageProducts);
        btnCreateOrder = findViewById(R.id.btnCreateOrder);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnCreateUser = findViewById(R.id.btnCreateUser);

        String role = getIntent().getStringExtra("USER_ROLE");
        if (role == null) role = "STAFF";

        tvRoleHeader.setText("Dashboard (" + role + ")");

        // Role-Based Control: Only ADMIN sees user creation button
        if ("ADMIN".equalsIgnoreCase(role)) {
            btnCreateUser.setVisibility(View.VISIBLE);
        } else {
            btnCreateUser.setVisibility(View.GONE);
        }

        btnManageProducts.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ProductListActivity.class)));
        btnCreateOrder.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CreateOrderActivity.class)));
        btnOrderHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class)));
        btnCreateUser.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddUserActivity.class)));
    }
}