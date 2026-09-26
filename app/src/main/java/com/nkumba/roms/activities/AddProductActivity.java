package com.nkumba.roms.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nkumba.roms.R;
import com.nkumba.roms.database.DatabaseHelper;

public class AddProductActivity extends AppCompatActivity {

    private EditText etName, etSku, etPrice, etStock;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int productId = -1; // -1 indicates creation mode

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etProductName);
        etSku = findViewById(R.id.etProductSku);
        etPrice = findViewById(R.id.etProductPrice);
        etStock = findViewById(R.id.etProductStock);
        btnSave = findViewById(R.id.btnSaveProduct);

        // Check if intent contains product payload (Edit Mode)
        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getIntExtra("PRODUCT_ID", -1);
            etName.setText(getIntent().getStringExtra("PRODUCT_NAME"));
            etSku.setText(getIntent().getStringExtra("PRODUCT_SKU"));
            etPrice.setText(String.valueOf(getIntent().getDoubleExtra("PRODUCT_PRICE", 0.0)));
            etStock.setText(String.valueOf(getIntent().getIntExtra("PRODUCT_STOCK", 0)));
            btnSave.setText("UPDATE PRODUCT");
        }

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String name = etName.getText().toString().trim();
        String sku = etSku.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Please complete required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        int stock = Integer.parseInt(stockStr);

        if (productId == -1) {
            // INSERT Operation
            long result = dbHelper.addProduct(name, sku, price, stock);
            if (result != -1) {
                Toast.makeText(this, "Product Added!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // UPDATE Operation
            int rows = dbHelper.updateProduct(productId, name, sku, price, stock);
            if (rows > 0) {
                Toast.makeText(this, "Product Updated!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}