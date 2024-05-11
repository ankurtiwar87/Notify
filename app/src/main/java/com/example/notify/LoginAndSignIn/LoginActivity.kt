package com.example.notify.LoginAndSignIn

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.ankur.admin_notifycampus.Activities.MainActivity
import com.example.loginsignup.UserData
import com.example.notify.R
import com.example.notify.databinding.ActivityLoginBinding
import com.example.notify.ui.Activity.NotifyMainActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import androidx.core.content.ContextCompat


private const val PREF_NAME = "LoginPrefs"
private const val KEY_USERNAME = "username"
private const val KEY_PASSWORD = "password"
private const val KEY_ROLE = "role"

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialog: Dialog
    private var spinnerRole:String=""
    private var email:String=""
    private var password:String=""

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestNotificationPermissions()


        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val emailPref = sharedPreferences.getString(KEY_USERNAME, null)
        val passwordPref = sharedPreferences.getString(KEY_PASSWORD, null)
        val rolePref = sharedPreferences.getString(KEY_ROLE, null)

        if (emailPref != null && passwordPref != null && rolePref != null) {
            // User is already logged in, redirect to appropriate activity
            if (rolePref=="Developer" || rolePref=="Admin"){
                val intent =Intent(this@LoginActivity,MainActivity::class.java)
                intent.putExtra("role",rolePref)
                startActivity(intent)
                finish()

            }
            else{
                val intent =Intent(this@LoginActivity,NotifyMainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        dialog= Dialog(this)
        dialog.setContentView(R.layout.progress_dialog)
        dialog.setCancelable(false)



        val category = resources.getStringArray(R.array.role)

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,category)

        binding.spinner.adapter=adapter

        binding.spinner.onItemSelectedListener=object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerRole = category[position].toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@LoginActivity, "Select Category", Toast.LENGTH_SHORT).show()
            }

            
        }

        binding.loginbutton.setOnClickListener {
            email=binding.loginUsername.text.toString()
            password=binding.loginPassword.text.toString()
            dialog.show()
            validateData(email,password,spinnerRole)
        }


    }

    private fun requestNotificationPermissions() {
        // Define your array of permissions
        val permissions = arrayOf(
            Manifest.permission.ACCESS_NOTIFICATION_POLICY,
            Manifest.permission.POST_NOTIFICATIONS
        )

        // Check if permissions are already granted
        if (!arePermissionsGranted(permissions)) {
            // Request permissions
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
    }

    private fun arePermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    // Handle permission request results if needed
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if all permissions were granted
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // Permissions granted, proceed with using the app's features
                // You may want to show a message or do something else here
            } else {
                // Permissions not granted, request again or show a message
                requestNotificationPermissions()
            }
        }
    }



    private fun validateData(email: String, password: String, spinnerRole: String) {

        if (email.isNullOrEmpty()||password.isNullOrEmpty()) {

            Toast.makeText(this,"Enter your details",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            return
        }
        else {
            loginUser(email,password,spinnerRole)
        }
    }

    private fun saveLoginInfo(username: String, password: String, role: String) {
        val sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }


    private fun loginUser(username: String, password: String, spinnerRole: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    var userFound = false
                    for (userSnapshot in dataSnapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password && userData.role == spinnerRole) {
                            // Username, password, and role match
                            saveLoginInfo(username, password, spinnerRole)

                            Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            if (spinnerRole=="Developer" || spinnerRole=="Admin"){
                                val intent =Intent(this@LoginActivity,MainActivity::class.java)
                                intent.putExtra("role",spinnerRole)
                                startActivity(intent)
                                finish()

                            }
                            else{
                                val intent =Intent(this@LoginActivity,NotifyMainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            return
                        } else if (userData?.username == username) {
                            // Username exists but password or role does not match
                            dialog.dismiss()
                            Toast.makeText(this@LoginActivity, "Invalid password or role", Toast.LENGTH_SHORT).show()
                            userFound = true // Username found but not matching password or role
                        }
                    }
                    if (!userFound) {
                        // Username not found
                        dialog.dismiss()
                        Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // User with provided username does not exist
                    dialog.dismiss()
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



}