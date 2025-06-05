package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Spinner spinnerRole;
    Button btnLogin;
    TextView tvCreateAccount;

    UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        userDatabaseHelper = new UserDatabaseHelper(this);

        // Setup spinner with roles: Admin, Chef, User
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String selectedRole = spinnerRole.getSelectedItem().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = userDatabaseHelper.getUserByEmail(email);

            if (user == null) {
                Toast.makeText(MainActivity.this, "User not found. Please register first.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verify password (hashed)
            String hashedInputPassword = PasswordUtils.hashPassword(password);
            if (hashedInputPassword == null || !hashedInputPassword.equals(user.getHashedPassword())) {
                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if role matches
            if (!selectedRole.equalsIgnoreCase(user.getRole())) {
                Toast.makeText(MainActivity.this, "Role does not match. Please select the correct role.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Role-based redirection
            if (selectedRole.equalsIgnoreCase("Admin") || selectedRole.equalsIgnoreCase("Chef")) {
                Intent intent = new Intent(MainActivity.this, AdminChefDashboardActivity.class);
                startActivity(intent);
            } else if (selectedRole.equalsIgnoreCase("User")) {
                Intent intent = new Intent(MainActivity.this, UserDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Unknown role", Toast.LENGTH_SHORT).show();
                return;
            }

            // Optionally finish login activity so user cannot come back by back button
            finish();
        });

        // Redirect to RegistrationActivity when clicking "Create new account"
        tvCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }
}
