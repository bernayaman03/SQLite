package com.example.json

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddContactActivity : AppCompatActivity() {
    private lateinit var etContactName: EditText
    private lateinit var etContactPhone: EditText
    private lateinit var btnSaveContact: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etContactName = findViewById(R.id.et_ContactName)
        etContactPhone = findViewById(R.id.et_ContactPhone)
        btnSaveContact = findViewById(R.id.btn_SaveContact)

        btnSaveContact.setOnClickListener {
            val name = etContactName.text.toString()
            val phone = etContactPhone.text.toString()

            if (name.isNotEmpty() && phone.isNotEmpty()) {
                val databaseHelper = DatabaseHelper(this)
                if (databaseHelper.insertContact(name, phone)) {
                    Toast.makeText(this, "Kişi eklendi", Toast.LENGTH_SHORT).show()
                    finish() // Ekleme bitince ana ekrana döner
                } else {
                    Toast.makeText(this, "Hata oluştu...", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}