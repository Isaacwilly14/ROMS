package com.nkumba.roms.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nkumba.roms.R;
import com.nkumba.roms.database.DatabaseHelper;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etUsername, etNewPassword, etConfirmPassword;
    private Button btnReset;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        dbHelper = new DatabaseHelper(this);

        etUsername = findViewById(R.id.etResetUsername);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnReset = findViewById(R.id.btnResetPassword);

        btnReset.setOnClickListener(v -> handleReset());
    }

    private void handleReset() {
        String username = etUsername.getText().toString().trim();
        String newPass = etNewPassword.getText().toString().trim();
        String confirmPass = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!dbHelper.checkUsernameExists(username)) {
            Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.updatePassword(username, newPass);
        if (success) {
            Toast.makeText(this, "Password reset successfully!", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
        }
    }
}