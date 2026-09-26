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

public class AddUserActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Spinner spinnerRole;
    private Button btnCreate;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etNewUsername);
        etPassword = findViewById(R.id.etNewUserPassword);
        spinnerRole = findViewById(R.id.spinnerUserRole);
        btnCreate = findViewById(R.id.btnCreateUser);

        String[] roles = {"STAFF", "ADMIN"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        spinnerRole.setAdapter(adapter);

        btnCreate.setOnClickListener(v -> createAccount());
    }

    private void createAccount() {
        String user = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUsernameExists(user)) {
            Toast.makeText(this, "Username already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.addUser(user, pass, role);
        if (success) {
            Toast.makeText(this, "User Created Successfully (" + role + ")", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
        }
    }
}