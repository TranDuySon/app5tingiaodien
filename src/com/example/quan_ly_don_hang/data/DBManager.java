package com.example.quan_ly_don_hang.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.quan_ly_don_hang.model.DanhMuc;
import com.example.quan_ly_don_hang.model.Users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class DBManager extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "QUANLYDONHANG";
	private static final String TABLE_USER = "user";
	private static final String TABLE_SANPHAM = "SanPham";
	private static final String TABLE_DANHMUC = "DanhMuc";
	private static final String TABLE_GIAMGIA = "GiamGia";
	private static final String TABLE_HOADON = "HoaDon";
	// Các cột chung
	public static final String ID = "_id";
	// Các cột bảng user
	private static final String USER_NAME = "username";
	private static final String PASSWORD = "password";
	private static final String PERMISSION = "permission";
	// Các cột bảng danh mục
	public static final String TEN_DANHMUC = "Tendanhmuc";

	// Các cột bảng Giảm giá
	public static final String MA_GIAM_GIA = "Magiamgia";
	public static final String GIA_TRI = "Giatri";
	// Các cột bảng sản phẩm
	public static final String TEN_SANPHAM = "Tensanpham";
	public static final String DANH_MUC = "Danhmuc";
	public static final String SO_LUONG = "Soluong";
	public static final String GIA_BAN = "Giaban";
	public static final String ANH = "Anh";
	
	//Cac cot hoa don
	public static final String NGAY = "Ngay";
	public static final String TONGTIEN = "Tongtien";
	
	
	// Một số thứ linh tinh
	private static int VERSION = 1;
	private Context context;
	// Các câu lệnh tạo bảng
	private String SQLQuery_CREATE_TBLUSER = "CREATE TABLE " + TABLE_USER
			+ " (" + ID + " integer primary key, " + USER_NAME
			+ " TEXT UNIQUE, " + PASSWORD + " TEXT, " + PERMISSION
			+ " integer)";
	private String SQLQuery_CREATE_TBLSANPHAM = "CREATE TABLE " + TABLE_SANPHAM
			+ " (" + ID + " integer primary key, " + TEN_SANPHAM
			+ " TEXT UNIQUE, " + DANH_MUC + " TEXT, " + SO_LUONG + " integer, "
			+ GIA_BAN + " integer, " + ANH + " BLOB)";
	private String SQLQuery_CREATE_GIAMGIA = "CREATE TABLE " + TABLE_GIAMGIA
			+ " (" + ID + " integer primary key, " + MA_GIAM_GIA
			+ " TEXT UNIQUE, " + GIA_TRI + " integer)";
	private String SQLQuery_CREATE_DANHMUC = "CREATE TABLE " + TABLE_DANHMUC
			+ " (" + ID + " integer primary key, " + TEN_DANHMUC
			+ " TEXT UNIQUE)";
	private String SQLQuery_CREATE_HOADON = "CREATE TABLE " + TABLE_HOADON
			+ " (" + ID + "  integer primary key," + NGAY
			+ " TEXT , " + TONGTIEN + " integer)";

	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	SQLiteDatabase db;
	DBManager dbmanager;

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQLQuery_CREATE_TBLUSER);
		db.execSQL(SQLQuery_CREATE_TBLSANPHAM);
		db.execSQL(SQLQuery_CREATE_GIAMGIA);
		db.execSQL(SQLQuery_CREATE_DANHMUC);
		db.execSQL(SQLQuery_CREATE_HOADON);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DANHMUC);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GIAMGIA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SANPHAM);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOADON);

		this.onCreate(db);
	}

	// hàm tạo tài khoản
	public void adduser(Users users) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(USER_NAME, users.getmUsername());
		values.put(PASSWORD, users.getmPassword());
		db.insert(TABLE_USER, null, values);
		db.close();
	}

	// hàm kiểm tra đăng nhập
	public boolean checklogin(String user, String pass) {
		db = this.getReadableDatabase();
		String query = "Select * from " + TABLE_USER + " where username ='"
				+ user + "'and password='" + pass + "'";
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.getCount() >= 1) {
			db.close();
			return true;
		}

		db.close();
		return false;

	}

	// hàm tạo danh mục
	public void addDanhmuc(DanhMuc dm) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TEN_DANHMUC, dm.getmTenDM());
		db.insert(TABLE_DANHMUC, null, values);
		db.close();
	}

	public List<String> getAllDanhMuc() {
		List<String> listDanhmuc = new ArrayList<String>();
		String selectQuery = "SELECT * FROM DanhMuc;";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				listDanhmuc.add(cursor.getString(1));

			} while (cursor.moveToNext());

		}
		cursor.close();
		db.close();
		return listDanhmuc;
	}

	public DBManager open() throws SQLException {
		dbmanager = new DBManager(context);
		db = dbmanager.getWritableDatabase();
		return this;
	}

	public void CloseDB() {
		if (db != null && db.isOpen())
			db.close();
	}

	// hàm lấy toàn bộ danh mục
	public Cursor SELECT_ALL_DANHMUC() {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM DanhMuc;", null);
		return cursor;
	}

	public Cursor getAllSanPham() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM SanPham;", null);
		return cursor;
	}
	public Cursor getAllHoaDon() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM HoaDon;", null);
		return cursor;
	}
	public void ThemSanPham(String Ten, String Danhmuc, int Soluong,
			int Giaban, byte[] Hinh) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "Insert into SanPham values (null, ?, ?, ?, ?, ?)";
		SQLiteStatement statement = db.compileStatement(sql);
		statement.clearBindings();
		statement.bindString(1, Ten);
		statement.bindString(2, Danhmuc);
		statement.bindLong(3, Soluong);
		statement.bindLong(4, Giaban);
		statement.bindBlob(5, Hinh);
		statement.executeInsert();
db.close();
	}
	public void ThemHoaDon (String ngaythang, int tongtien){
		SQLiteDatabase db = getWritableDatabase();
		String sql = "Insert into HoaDon values (null, ?, ?)";
		SQLiteStatement statement = db.compileStatement(sql);
		statement.clearBindings();
		statement.bindString(1, ngaythang);
		statement.bindLong(2, tongtien);
		statement.executeInsert();
		db.close();
	}
	public void UpdateSanPham(int Soluong, String Tensp){
		SQLiteDatabase db = getWritableDatabase();
		String sql  = "UPDATE SanPham SET Soluong= Soluong - ? Where Tensanpham =?";
		SQLiteStatement statement = db.compileStatement(sql);
		statement.clearBindings();
		statement.bindLong(1, Soluong);
		statement.bindString(2, Tensp);
		statement.execute();
		db.close();
	}
	public String ngaythang(){
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String date = df.format(cal.getTime());
		return date;
	}
}
