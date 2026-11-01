package com.example.json

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var lvContactList: ListView
    private lateinit var databaseHelper: DatabaseHelper
    private var selectedContactName: String? = null
    private lateinit var adapter: ArrayAdapter<String>
    private var contactList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lvContactList = findViewById(R.id.lv_ContactList)
        databaseHelper = DatabaseHelper(this)

        loadContacts()

        // ListView'den kişi seçimi
        lvContactList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            selectedContactName = contactList[position]
            Toast.makeText(this, "Seçilen Kişi: $selectedContactName", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadContacts() // Yeni kişi eklenip veya güncellenip dönüldüğünde listeyi yeniler
    }

    private fun loadContacts() {
        contactList = databaseHelper.getAllContacts()
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            contactList
        )
        lvContactList.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new_contact -> {
                val intent = Intent(this, AddContactActivity::class.java)
                startActivity(intent)
            }
            R.id.action_update_contact -> {
                if (selectedContactName != null) {
                    val intent = Intent(this, UpdateContactActivity::class.java)
                    intent.putExtra("CONTACT_NAME", selectedContactName)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Lütfen listeden bir kişi seçin", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.action_delete_contact -> {
                if (selectedContactName != null) {
                    if (databaseHelper.deleteContact(selectedContactName!!)) {
                        Toast.makeText(this, "Kişi başarıyla silindi", Toast.LENGTH_SHORT).show()
                        selectedContactName = null
                        loadContacts()
                    } else {
                        Toast.makeText(this, "Silme işlemi başarısız", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Lütfen listeden bir kişi seçin", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.action_call -> {
                if (selectedContactName != null) {
                    val phone = databaseHelper.getPhoneByName(selectedContactName!!)
                    if (phone.isNotEmpty()) {
                        makePhoneCall(phone)
                    }
                } else {
                    Toast.makeText(this, "Lütfen listeden bir kişi seçin", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.action_exit -> {
                finishAffinity() // Uygulamayı kapatır
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makePhoneCall(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), 1)
        } else {
            val callIntent = Intent(Intent.ACTION_CALL)
            callIntent.data = Uri.parse("tel:$phoneNumber")
            startActivity(callIntent)
        }
    }
}