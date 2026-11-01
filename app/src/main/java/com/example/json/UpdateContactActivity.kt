package com.example.json

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UpdateContactActivity : AppCompatActivity() {
    private lateinit var etUpdateName: EditText
    private lateinit var etUpdatePhone: EditText
    private lateinit var btnUpdateContact: Button
    private var oldName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_update_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etUpdateName = findViewById(R.id.et_UpdateName)
        etUpdatePhone = findViewById(R.id.et_UpdatePhone)
        btnUpdateContact = findViewById(R.id.btn_UpdateContact)

        oldName = intent.getStringExtra("CONTACT_NAME")
        etUpdateName.setText(oldName)

        val databaseHelper = DatabaseHelper(this)

        // Eski kaydın telefon numarasını DB'den çekip EditText'e yazdırıyoruz
        if (oldName != null) {
            val phone = databaseHelper.getPhoneByName(oldName!!)
            etUpdatePhone.setText(phone)
        }

        btnUpdateContact.setOnClickListener {
            val newName = etUpdateName.text.toString()
            val newPhone = etUpdatePhone.text.toString()

            if (newName.isNotEmpty() && newPhone.isNotEmpty() && oldName != null) {
                if (databaseHelper.updateContact(oldName!!, newName, newPhone)) {
                    Toast.makeText(this, "Kişi başarıyla güncellendi", Toast.LENGTH_SHORT).show()
                    finish() // Güncelleme bitince ana sayfaya dön
                } else {
                    Toast.makeText(this, "Bir hata oluştu...", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }
}