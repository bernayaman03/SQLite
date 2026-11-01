package com.example.json

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    companion object {
        const val DATABASE_NAME = "CONTACTS.DB"
        const val TABLE_NAME = "CONTACTS"
        const val COL_ID = "ID"
        const val COL_NAME = "NAME"
        const val COL_PHONE = "PHONE"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY,
                $COL_NAME TEXT NOT NULL, $COL_PHONE TEXT NOT NULL)""".trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertContact(name: String, phone: String): Boolean {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val id = cursor.count
        cursor.close()

        val record = ContentValues().apply {
            put(COL_ID, id + 1)
            put(COL_NAME, name)
            put(COL_PHONE, phone)
        }

        val result = db.insert(TABLE_NAME, null, record)
        db.close()
        return result != -1L
    }

    fun getAllContacts(): ArrayList<String> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        val contactNames = ArrayList<String>()

        if (cursor.moveToFirst()) {
            do {
                // Name kolonu 1. indekste (0: ID, 1: NAME, 2: PHONE)
                contactNames.add(cursor.getString(1))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return contactNames
    }

    fun getPhoneByName(name: String): String {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COL_PHONE FROM $TABLE_NAME WHERE $COL_NAME = ?", arrayOf(name))
        var phone = ""
        if (cursor.moveToFirst()) {
            phone = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return phone
    }

    fun deleteContact(name: String): Boolean {
        val db = writableDatabase
        val result = db.delete(TABLE_NAME, "$COL_NAME = ?", arrayOf(name))
        db.close()
        return result > 0
    }

    fun updateContact(oldName: String, newName: String, newPhone: String): Boolean {
        val db = writableDatabase
        val record = ContentValues().apply {
            put(COL_NAME, newName)
            put(COL_PHONE, newPhone)
        }
        val result = db.update(TABLE_NAME, record, "$COL_NAME = ?", arrayOf(oldName))
        db.close()
        return result > 0
    }
}