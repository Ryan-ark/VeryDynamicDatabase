package com.nasaro.verydynamicdatabase;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nasaro.verydynamicdatabase.db.DatabaseHelper;
import com.nasaro.verydynamicdatabase.model.Car;
import com.nasaro.verydynamicdatabase.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private EditText nameInput, emailInput;
    private EditText makeInput, modelInput, yearInput, userIdInput;
    private EditText updateUserIdInput, updateNameInput, updateEmailInput;
    private EditText updateCarIdInput, updateMakeInput, updateModelInput, updateYearInput, updateCarUserIdInput;
    private EditText deleteUserIdInput, deleteCarIdInput;
    private TextView outputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);

        makeInput = findViewById(R.id.make_input);
        modelInput = findViewById(R.id.model_input);
        yearInput = findViewById(R.id.year_input);
        userIdInput = findViewById(R.id.user_id_input);

        updateUserIdInput = findViewById(R.id.update_user_id_input);
        updateNameInput = findViewById(R.id.update_name_input);
        updateEmailInput = findViewById(R.id.update_email_input);

        updateCarIdInput = findViewById(R.id.update_car_id_input);
        updateMakeInput = findViewById(R.id.update_make_input);
        updateModelInput = findViewById(R.id.update_model_input);
        updateYearInput = findViewById(R.id.update_year_input);
        updateCarUserIdInput = findViewById(R.id.update_car_user_id_input);

        deleteUserIdInput = findViewById(R.id.delete_user_id_input);
        deleteCarIdInput = findViewById(R.id.delete_car_id_input);

        outputText = findViewById(R.id.output_text);

        Button addUserBtn = findViewById(R.id.add_user_btn);
        Button addCarBtn = findViewById(R.id.add_car_btn);
        Button updateUserBtn = findViewById(R.id.update_user_btn);
        Button updateCarBtn = findViewById(R.id.update_car_btn);
        Button deleteUserBtn = findViewById(R.id.delete_user_btn);
        Button deleteCarBtn = findViewById(R.id.delete_car_btn);
        Button showAllBtn = findViewById(R.id.show_all_btn);

        addUserBtn.setOnClickListener(v -> addUser());
        addCarBtn.setOnClickListener(v -> addCar());
        updateUserBtn.setOnClickListener(v -> updateUser());
        updateCarBtn.setOnClickListener(v -> updateCar());
        deleteUserBtn.setOnClickListener(v -> deleteUser());
        deleteCarBtn.setOnClickListener(v -> deleteCar());
        showAllBtn.setOnClickListener(v -> showAll());
    }

    private void addUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter name and email", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User(name, email);

        long id = db.insert(User.tableName, user.toValues());

        if (id > 0) {
            Toast.makeText(this, "User added with ID: " + id, Toast.LENGTH_SHORT).show();
            nameInput.setText("");
            emailInput.setText("");
        } else {
            Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCar() {
        String make = makeInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        String userIdStr = userIdInput.getText().toString().trim();

        if (make.isEmpty() || model.isEmpty() || yearStr.isEmpty() || userIdStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int year = Integer.parseInt(yearStr);
            long userId = Long.parseLong(userIdStr);

            Car car = new Car(make, model, year, userId);

            long id = db.insert(Car.tableName, car.toValues());

            if (id > 0) {
                Toast.makeText(this, "Car added with ID: " + id, Toast.LENGTH_SHORT).show();
                makeInput.setText("");
                modelInput.setText("");
                yearInput.setText("");
                userIdInput.setText("");
            } else {
                Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid year or user ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser() {
        String idStr = updateUserIdInput.getText().toString().trim();
        String name = updateNameInput.getText().toString().trim();
        String email = updateEmailInput.getText().toString().trim();

        if (idStr.isEmpty() || (name.isEmpty() && email.isEmpty())) {
            Toast.makeText(this, "Please enter ID and at least one field to update", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userId = Integer.parseInt(idStr);

            User user = new User();
            if (!name.isEmpty()) {
                user.name = name;
            }
            if (!email.isEmpty()) {
                user.email = email;
            }

            int rowsAffected = db.update(User.tableName, user.toValues(),
                    "id = ?", new String[]{String.valueOf(userId)});

            if (rowsAffected > 0) {
                Toast.makeText(this, "User updated successfully", Toast.LENGTH_SHORT).show();
                updateUserIdInput.setText("");
                updateNameInput.setText("");
                updateEmailInput.setText("");
                showAll();
            } else {
                Toast.makeText(this, "No user found with ID: " + userId, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCar() {
        String idStr = updateCarIdInput.getText().toString().trim();
        String make = updateMakeInput.getText().toString().trim();
        String model = updateModelInput.getText().toString().trim();
        String yearStr = updateYearInput.getText().toString().trim();
        String userIdStr = updateCarUserIdInput.getText().toString().trim();

        if (idStr.isEmpty() || (make.isEmpty() && model.isEmpty() && yearStr.isEmpty() && userIdStr.isEmpty())) {
            Toast.makeText(this, "Please enter ID and at least one field to update", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int carId = Integer.parseInt(idStr);

            Car car = new Car();
            if (!make.isEmpty()) {
                car.make = make;
            }
            if (!model.isEmpty()) {
                car.model = model;
            }
            if (!yearStr.isEmpty()) {
                car.year = Integer.parseInt(yearStr);
            }
            if (!userIdStr.isEmpty()) {
                car.userId = Long.parseLong(userIdStr);
            }

            int rowsAffected = db.update(Car.tableName, car.toValues(),
                    "id = ?", new String[]{String.valueOf(carId)});

            if (rowsAffected > 0) {
                Toast.makeText(this, "Car updated successfully", Toast.LENGTH_SHORT).show();
                updateCarIdInput.setText("");
                updateMakeInput.setText("");
                updateModelInput.setText("");
                updateYearInput.setText("");
                updateCarUserIdInput.setText("");
                showAll();
            } else {
                Toast.makeText(this, "No car found with ID: " + carId, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid ID or numeric value", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUser() {
        String idStr = deleteUserIdInput.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Please enter user ID to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int userId = Integer.parseInt(idStr);

            int carsDeleted = db.delete(Car.tableName, "user_id = ?",
                    new String[]{String.valueOf(userId)});

            int rowsAffected = db.delete(User.tableName, "id = ?",
                    new String[]{String.valueOf(userId)});

            if (rowsAffected > 0) {
                Toast.makeText(this, "User deleted. Also removed " + carsDeleted + " associated cars.",
                        Toast.LENGTH_SHORT).show();
                deleteUserIdInput.setText("");
                showAll();
            } else {
                Toast.makeText(this, "No user found with ID: " + userId, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCar() {
        String idStr = deleteCarIdInput.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Please enter car ID to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int carId = Integer.parseInt(idStr);

            int rowsAffected = db.delete(Car.tableName, "id = ?",
                    new String[]{String.valueOf(carId)});

            if (rowsAffected > 0) {
                Toast.makeText(this, "Car deleted successfully", Toast.LENGTH_SHORT).show();
                deleteCarIdInput.setText("");
                showAll();
            } else {
                Toast.makeText(this, "No car found with ID: " + carId, Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid car ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAll() {
        StringBuilder output = new StringBuilder();

        output.append("=== USERS ===\n");
        List<User> users = getAllUsers();

        if (!users.isEmpty()) {
            for (User user : users) {
                output.append("ID: ").append(user.id)
                        .append(", Name: ").append(user.name)
                        .append(", Email: ").append(user.email)
                        .append("\n");

                List<Car> cars = getCarsByUserId(user.id);
                if (!cars.isEmpty()) {
                    output.append("  Cars:\n");
                    for (Car car : cars) {
                        output.append("  - ID: ").append(car.id)
                                .append(", ").append(car.year)
                                .append(" ").append(car.make)
                                .append(" ").append(car.model)
                                .append("\n");
                    }
                } else {
                    output.append("  No cars\n");
                }

                output.append("\n");
            }
        } else {
            output.append("No users found\n");
        }

        outputText.setText(output.toString());
    }

    private List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Cursor cursor = db.getAll(User.tableName);

        if (cursor.moveToFirst()) {
            do {
                users.add(User.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return users;
    }

    private List<Car> getCarsByUserId(long userId) {
        List<Car> cars = new ArrayList<>();
        Cursor cursor = db.query(Car.tableName, "user_id = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                cars.add(Car.fromCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return cars;
    }
}