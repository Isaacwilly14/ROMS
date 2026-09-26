package com.nkumba.roms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nkumba.roms.models.Order;
import com.nkumba.roms.models.Product;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "roms_db";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_ORDERS = "orders";

    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ROLE = "role";

    public static final String KEY_PRODUCT_NAME = "name";
    public static final String KEY_PRODUCT_SKU = "sku";
    public static final String KEY_PRODUCT_PRICE = "price";
    public static final String KEY_PRODUCT_STOCK = "stock";

    public static final String KEY_ORDER_CUSTOMER = "customer_name";
    public static final String KEY_ORDER_LOCATION = "customer_location";
    public static final String KEY_ORDER_CONTACT = "customer_contact";
    public static final String KEY_ORDER_EMAIL = "customer_email";
    public static final String KEY_ORDER_TOTAL = "total_amount";
    public static final String KEY_ORDER_DATE = "order_date";
    public static final String KEY_ORDER_STATUS = "status";
    public static final String KEY_ORDER_PRODUCT_ID = "product_id";
    public static final String KEY_ORDER_QTY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_USERNAME + " TEXT UNIQUE,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_ROLE + " TEXT DEFAULT 'STAFF'" + ")";

        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRODUCT_SKU + " TEXT,"
                + KEY_PRODUCT_PRICE + " REAL,"
                + KEY_PRODUCT_STOCK + " INTEGER" + ")";

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ORDER_CUSTOMER + " TEXT,"
                + KEY_ORDER_LOCATION + " TEXT,"
                + KEY_ORDER_CONTACT + " TEXT,"
                + KEY_ORDER_EMAIL + " TEXT,"
                + KEY_ORDER_TOTAL + " REAL,"
                + KEY_ORDER_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + KEY_ORDER_STATUS + " TEXT DEFAULT 'CONFIRMED',"
                + KEY_ORDER_PRODUCT_ID + " INTEGER,"
                + KEY_ORDER_QTY + " INTEGER" + ")";

        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }

    public String getUserRole(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ROLE},
                KEY_USERNAME + "=? AND " + KEY_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        String role = null;
        if (cursor.moveToFirst()) {
            role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));
        }
        cursor.close();
        return role;
    }

    public boolean registerUser(String username, String password, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_ROLE, role);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID},
                KEY_USERNAME + "=?",
                new String[]{username}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASSWORD, newPassword);
        int rows = db.update(TABLE_USERS, values, KEY_USERNAME + " = ?", new String[]{username});
        return rows > 0;
    }

    public long addProduct(String name, String sku, double price, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, name);
        values.put(KEY_PRODUCT_SKU, sku);
        values.put(KEY_PRODUCT_PRICE, price);
        values.put(KEY_PRODUCT_STOCK, stock);
        return db.insert(TABLE_PRODUCTS, null, values);
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Product product = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_PRODUCT_SKU)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRODUCT_PRICE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRODUCT_STOCK))
                );
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    public int updateProduct(int id, String name, String sku, double price, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, name);
        values.put(KEY_PRODUCT_SKU, sku);
        values.put(KEY_PRODUCT_PRICE, price);
        values.put(KEY_PRODUCT_STOCK, stock);
        return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean createOrder(String customerName, String location, String contact, String email, double totalAmount, int productId, int orderQty) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ORDER_CUSTOMER, customerName);
            values.put(KEY_ORDER_LOCATION, location);
            values.put(KEY_ORDER_CONTACT, contact);
            values.put(KEY_ORDER_EMAIL, email);
            values.put(KEY_ORDER_TOTAL, totalAmount);
            values.put(KEY_ORDER_STATUS, "CONFIRMED");
            values.put(KEY_ORDER_PRODUCT_ID, productId);
            values.put(KEY_ORDER_QTY, orderQty);

            db.insert(TABLE_ORDERS, null, values);
            db.execSQL("UPDATE " + TABLE_PRODUCTS + " SET " + KEY_PRODUCT_STOCK + " = " + KEY_PRODUCT_STOCK + " - " + orderQty + " WHERE " + KEY_ID + " = " + productId);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " ORDER BY " + KEY_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_CUSTOMER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_LOCATION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_CONTACT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_EMAIL)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_ORDER_TOTAL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ORDER_STATUS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ORDER_PRODUCT_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ORDER_QTY))
                );
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public boolean cancelOrder(int orderId, int productId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ORDER_STATUS, "CANCELLED");
            db.update(TABLE_ORDERS, values, KEY_ID + " = ?", new String[]{String.valueOf(orderId)});

            db.execSQL("UPDATE " + TABLE_PRODUCTS + " SET " + KEY_PRODUCT_STOCK + " = " + KEY_PRODUCT_STOCK + " + " + quantity + " WHERE " + KEY_ID + " = " + productId);

            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public boolean deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_ORDERS, KEY_ID + " = ?", new String[]{String.valueOf(orderId)});
        return rows > 0;
    }
}