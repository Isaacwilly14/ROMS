package com.nkumba.roms.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.nkumba.roms.R;
import com.nkumba.roms.database.DatabaseHelper;
import com.nkumba.roms.models.Order;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends ArrayAdapter<Order> {

    private Context context;
    private List<Order> orderList;
    private DatabaseHelper dbHelper;

    public OrderAdapter(@NonNull Context context, @NonNull List<Order> orderList) {
        super(context, 0, orderList);
        this.context = context;
        this.orderList = orderList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        Order order = orderList.get(position);

        TextView tvId = convertView.findViewById(R.id.tvOrderId);
        TextView tvStatus = convertView.findViewById(R.id.tvOrderStatus);
        TextView tvDate = convertView.findViewById(R.id.tvOrderDate);
        TextView tvCustomer = convertView.findViewById(R.id.tvOrderCustomer);
        TextView tvLocation = convertView.findViewById(R.id.tvOrderLocation);
        TextView tvContact = convertView.findViewById(R.id.tvOrderContact);
        TextView tvTotal = convertView.findViewById(R.id.tvOrderTotal);
        Button btnCancel = convertView.findViewById(R.id.btnCancelOrder);
        ImageButton btnDelete = convertView.findViewById(R.id.btnDeleteOrder);

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);

        tvId.setText("Order #" + order.getId());
        tvStatus.setText(order.getStatus());
        tvDate.setText(order.getOrderDate());
        tvCustomer.setText("Customer: " + order.getCustomerName());
        tvLocation.setText("Location: " + order.getCustomerLocation());
        tvContact.setText("Contact: " + order.getCustomerContact() + " | " + order.getCustomerEmail());
        tvTotal.setText("Total Amount: UGX " + formatter.format(order.getTotalAmount()));

        // Style status badge
        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            tvStatus.setTextColor(0xFFDC2626);
            tvStatus.setBackgroundColor(0xFFFEE2E2);
            btnCancel.setVisibility(View.GONE);
        } else {
            tvStatus.setTextColor(0xFF059669);
            tvStatus.setBackgroundColor(0xFFD1FAE5);
            btnCancel.setVisibility(View.VISIBLE);
        }

        // Cancel Order Click
        btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure you want to cancel Order #" + order.getId() + "? This will restore the product stock.")
                    .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                        boolean cancelled = dbHelper.cancelOrder(order.getId(), order.getProductId(), order.getQuantity());
                        if (cancelled) {
                            Toast.makeText(context, "Order Cancelled and Stock Restored", Toast.LENGTH_SHORT).show();
                            orderList.set(position, new Order(order.getId(), order.getCustomerName(), order.getCustomerLocation(), order.getCustomerContact(), order.getCustomerEmail(), order.getTotalAmount(), order.getOrderDate(), "CANCELLED", order.getProductId(), order.getQuantity()));
                            notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Delete Order Click with Tooltip & Confirmation Dialog
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Order")
                    .setMessage("Are you sure you want to permanently delete Order #" + order.getId() + "? This action cannot be undone.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        boolean deleted = dbHelper.deleteOrder(order.getId());
                        if (deleted) {
                            orderList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Order Deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        return convertView;
    }
}