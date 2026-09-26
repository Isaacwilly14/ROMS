package com.nkumba.roms.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nkumba.roms.R;
import com.nkumba.roms.database.DatabaseHelper;
import com.nkumba.roms.models.Product;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CreateOrderActivity extends AppCompatActivity {

    private EditText etCustomerName, etLocation, etContact, etEmail, etQuantity;
    private Spinner spinnerProducts;
    private Button btnSubmitOrder;
    private DatabaseHelper dbHelper;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        dbHelper = new DatabaseHelper(this);

        etCustomerName = findViewById(R.id.etCustomerName);
        etLocation = findViewById(R.id.etCustomerLocation);
        etContact = findViewById(R.id.etCustomerContact);
        etEmail = findViewById(R.id.etCustomerEmail);
        etQuantity = findViewById(R.id.etOrderQuantity);
        spinnerProducts = findViewById(R.id.spinnerProducts);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        loadProductSpinner();

        btnSubmitOrder.setOnClickListener(v -> processOrder());
    }

    private void loadProductSpinner() {
        productList = dbHelper.getAllProducts();
        String[] productNames = new String[productList.size()];
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);

        for (int i = 0; i < productList.size(); i++) {
            String formattedPrice = formatter.format(productList.get(i).getPrice());
            productNames[i] = productList.get(i).getName() + " - UGX " + formattedPrice;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productNames);
        spinnerProducts.setAdapter(adapter);
    }

    private void processOrder() {
        String customer = etCustomerName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();

        if (customer.isEmpty() || location.isEmpty() || contact.isEmpty() || email.isEmpty() || qtyStr.isEmpty() || productList.isEmpty()) {
            Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(qtyStr);
        int selectedIndex = spinnerProducts.getSelectedItemPosition();
        Product selectedProduct = productList.get(selectedIndex);

        if (quantity > selectedProduct.getStock()) {
            Toast.makeText(this, "Insufficient stock available!", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalAmount = selectedProduct.getPrice() * quantity;

        boolean success = dbHelper.createOrder(customer, location, contact, email, totalAmount, selectedProduct.getId(), quantity);

        if (success) {
            Toast.makeText(this, "Order Created Successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create order", Toast.LENGTH_SHORT).show();
        }
    }
}