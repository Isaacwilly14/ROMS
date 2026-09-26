package com.nkumba.roms.activities;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.nkumba.roms.R;
import com.nkumba.roms.adapters.OrderAdapter;
import com.nkumba.roms.database.DatabaseHelper;
import com.nkumba.roms.models.Order;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView listViewOrders;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        listViewOrders = findViewById(R.id.listViewOrders);
        dbHelper = new DatabaseHelper(this);

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        List<Order> orders = dbHelper.getAllOrders();
        OrderAdapter adapter = new OrderAdapter(this, orders);
        listViewOrders.setAdapter(adapter);
    }
}