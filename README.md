# Very Dynamic Database for Android

A flexible and simple SQLite implementation for Android with model classes and CRUD operations.

## The Project Structure

The project consists of a few key files:

1. **DatabaseHelper.java** - The database helper with CRUD operations
2. **User.java** - Model for User table
3. **Car.java** - Model for Car table
4. **MainActivity.java** - Main UI with complete CRUD operations

## Database Helper

```java
public class DatabaseHelper extends SQLiteOpenHelper {
    
    // Singleton instance
    private static DatabaseHelper instance;
    
    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_CARS = "cars";
    
    // Get singleton instance
    public static DatabaseHelper get(Context ctx) {
        if (instance == null) instance = new DatabaseHelper(ctx.getApplicationContext());
        return instance;
    }
    
    // Constructor
    private DatabaseHelper(Context ctx) {
        super(ctx, "vdd_database.db", null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "name TEXT, email TEXT)");
        
        db.execSQL("CREATE TABLE " + TABLE_CARS + " (" +
                   "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                   "make TEXT, model TEXT, year INTEGER, " +
                   "user_id INTEGER, " +
                   "FOREIGN KEY (user_id) REFERENCES " + TABLE_USERS + "(id))");
    }
    
    // Basic database operations
    public long insert(String table, ContentValues values) {
        return getWritableDatabase().insert(table, null, values);
    }
    
    public Cursor query(String table, String selection, String[] args) {
        return getReadableDatabase().query(table, null, selection, args, null, null, null);
    }
    
    public Cursor getAll(String table) {
        return getReadableDatabase().query(table, null, null, null, null, null, null);
    }
    
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return getWritableDatabase().update(table, values, whereClause, whereArgs);
    }
    
    public int delete(String table, String whereClause, String[] whereArgs) {
        return getWritableDatabase().delete(table, whereClause, whereArgs);
    }
}
```

## Model Classes

### User.java

```java
public class User {
    public int id;
    public String name;
    public String email;
    
    public User() {
    }
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        return values;
    }
    
    public static User fromCursor(Cursor cursor) {
        User user = new User();
        user.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        user.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        user.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        return user;
    }
}
```

### Car.java

```java
public class Car {
    public int id;
    public String make;
    public String model;
    public int year;
    public long userId;
    
    public Car() {
    }
    
    public Car(String make, String model, int year, long userId) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.userId = userId;
    }
    
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put("make", make);
        values.put("model", model);
        values.put("year", year);
        values.put("user_id", userId);
        return values;
    }
    
    public static Car fromCursor(Cursor cursor) {
        Car car = new Car();
        car.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        car.make = cursor.getString(cursor.getColumnIndexOrThrow("make"));
        car.model = cursor.getString(cursor.getColumnIndexOrThrow("model"));
        car.year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        car.userId = cursor.getInt(cursor.getColumnIndexOrThrow("user_id"));
        return car;
    }
}
```

## Features

1. **Create (Insert)**
   - Add new users and cars to the database
   
2. **Read (Query)**
   - View all users and their associated cars
   - Query cars by user ID
   
3. **Update**
   - Update user information (name, email)
   - Update car details (make, model, year, owner)
   
4. **Delete**
   - Delete users (including all associated cars)
   - Delete individual cars

## How to Use

### 1. Setup

```java
// Get database instance
DatabaseHelper db = DatabaseHelper.get(context);
```

### 2. Adding Data with Models

```java
// Create and add a user
User user = new User("John", "john@example.com");
long userId = db.insert(DatabaseHelper.TABLE_USERS, user.toValues());

// Create and add a car
Car car = new Car("Toyota", "Corolla", 2020, userId);
db.insert(DatabaseHelper.TABLE_CARS, car.toValues());
```

### 3. Querying Data with Models

```java
// Get all users as objects
List<User> userList = new ArrayList<>();
Cursor cursor = db.getAll(DatabaseHelper.TABLE_USERS);

if (cursor.moveToFirst()) {
    do {
        userList.add(User.fromCursor(cursor));
    } while (cursor.moveToNext());
}
cursor.close();

// Query cars by user ID
List<Car> carList = new ArrayList<>();
Cursor carCursor = db.query(DatabaseHelper.TABLE_CARS, "user_id = ?", 
                            new String[]{String.valueOf(userId)});

if (carCursor.moveToFirst()) {
    do {
        carList.add(Car.fromCursor(carCursor));
    } while (carCursor.moveToNext());
}
carCursor.close();
```

### 4. Updating Data

```java
// Update a user
User updatedUser = new User();
updatedUser.name = "John Updated";
updatedUser.email = "john.updated@example.com";

int rowsAffected = db.update(
    DatabaseHelper.TABLE_USERS, 
    updatedUser.toValues(), 
    "id = ?", 
    new String[]{String.valueOf(userId)}
);
```

### 5. Deleting Data

```java
// Delete a car
int deleted = db.delete(
    DatabaseHelper.TABLE_CARS, 
    "id = ?", 
    new String[]{String.valueOf(carId)}
);

// Delete a user and all associated cars
// First delete associated cars
db.delete(
    DatabaseHelper.TABLE_CARS, 
    "user_id = ?", 
    new String[]{String.valueOf(userId)}
);

// Then delete the user
db.delete(
    DatabaseHelper.TABLE_USERS, 
    "id = ?", 
    new String[]{String.valueOf(userId)}
);
```

## Adding a New Model

To add a new Product model:

1. Create a new Product.java file:
```java
public class Product {
    public int id;
    public String name;
    public double price;
    
    public Product() {
    }
    
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        return values;
    }
    
    public static Product fromCursor(Cursor cursor) {
        Product product = new Product();
        product.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        product.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        product.price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        return product;
    }
}
```

2. Add table name and creation in DatabaseHelper:
```java
public static final String TABLE_PRODUCTS = "products";

// In onCreate():
db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" +
           "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
           "name TEXT, price REAL)");
```

3. Use it just like other models:
```java
Product product = new Product("Laptop", 999.99);
db.insert(DatabaseHelper.TABLE_PRODUCTS, product.toValues());
```

That's it! A clean architecture with separate model files and full CRUD operations. 