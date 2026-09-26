package com.nkumba.roms.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nkumba.roms.R;
import com.nkumba.roms.adapters.ProductAdapter;
import com.nkumba.roms.database.DatabaseHelper;
import com.nkumba.roms.models.Product;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private DatabaseHelper dbHelper;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewProducts);
        fabAdd = findViewById(R.id.fabAddProduct);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // FAB Click -> Add Product
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(ProductListActivity.this, AddProductActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh product list from SQLite DB
        List<Product> products = dbHelper.getAllProducts();
        adapter = new ProductAdapter(this, products);
        recyclerView.setAdapter(adapter);
    }
}