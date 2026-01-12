package com.metrox.demo61;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class RegionDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "region.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_REGION = "region";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PARENT_ID = "parent_id";
    private static final String COLUMN_LEVEL = "level";

    private static RegionDbHelper instance;

    public static synchronized RegionDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new RegionDbHelper(context.getApplicationContext());
        }
        return instance;
    }

    private RegionDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_REGION_TABLE = "CREATE TABLE " + TABLE_REGION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_PARENT_ID + " INTEGER,"
                + COLUMN_LEVEL + " INTEGER"
                + ")";
        db.execSQL(CREATE_REGION_TABLE);
        Log.d("RegionDbHelper", "Database table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGION);
        onCreate(db);
    }

    public void addRegion(Region region) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, region.getId());
        values.put(COLUMN_NAME, region.getName());
        values.put(COLUMN_PARENT_ID, region.getParentId());
        values.put(COLUMN_LEVEL, region.getLevel());
        db.insert(TABLE_REGION, null, values);
        db.close();
    }

    public List<Region> getRegionsByParent(int parentId, int level) {
        List<Region> regionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_REGION,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PARENT_ID, COLUMN_LEVEL},
                COLUMN_PARENT_ID + "=? AND " + COLUMN_LEVEL + "=?",
                new String[]{String.valueOf(parentId), String.valueOf(level)},
                null, null, COLUMN_NAME);

        if (cursor.moveToFirst()) {
            do {
                Region region = new Region();
                region.setId(cursor.getInt(0));
                region.setName(cursor.getString(1));
                region.setParentId(cursor.getInt(2));
                region.setLevel(cursor.getInt(3));
                regionList.add(region);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return regionList;
    }

    public Region getRegionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REGION,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_PARENT_ID, COLUMN_LEVEL},
                COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Region region = null;
        if (cursor.moveToFirst()) {
            region = new Region();
            region.setId(cursor.getInt(0));
            region.setName(cursor.getString(1));
            region.setParentId(cursor.getInt(2));
            region.setLevel(cursor.getInt(3));
        }
        cursor.close();
        db.close();
        return region;
    }

    public void clearAllRegions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REGION, null, null);
        db.close();
        Log.d("RegionDbHelper", "All regions cleared");
    }

    public void batchInsertRegions(List<Region> regions) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.beginTransaction();
            for (Region region : regions) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_ID, region.getId());
                values.put(COLUMN_NAME, region.getName());
                values.put(COLUMN_PARENT_ID, region.getParentId());
                values.put(COLUMN_LEVEL, region.getLevel());
                db.insert(TABLE_REGION, null, values);
            }
            db.setTransactionSuccessful();
            Log.d("RegionDbHelper", "Batch inserted " + regions.size() + " regions");
        } catch (Exception e) {
            Log.e("RegionDbHelper", "Batch insert failed", e);
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean hasData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REGION, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count > 0;
    }
}
