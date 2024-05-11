
package com.example.notify.LoginAndSignIn

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.loginsignup.UserData
import com.example.notify.R
import com.example.notify.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var progressBarDialog: Dialog

    private var spinnerRole: String = ""
    private var email: String = ""
    private var password: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBarDialog= Dialog(this)
        progressBarDialog.setContentView(R.layout.progress_dialog)
        progressBarDialog.setCancelable(false)
        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        val category = resources.getStringArray(R.array.role)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, category)
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerRole = category[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@SignupActivity, "Select Category", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupButton.setOnClickListener {
            progressBarDialog.show()
            email = binding.signupUsername.text.toString()
            password = binding.signupPassword.text.toString()
            validateData(email, password, spinnerRole)
        }
    }

    private fun validateData(email: String, password: String, spinnerRole: String) {
        when {
            email.isNullOrEmpty() || password.isNullOrEmpty() -> {
                Toast.makeText(this, "Enter your details", Toast.LENGTH_SHORT).show()
                progressBarDialog.dismiss()
            }
            !isValidEmail(email) -> {
                Toast.makeText(this, "Please enter correct email", Toast.LENGTH_SHORT).show()
                progressBarDialog.dismiss()
            }
            else -> {
                signupUser(email, password, spinnerRole)
            }
        }
    }

    private fun signupUser(email: String, password: String, spinnerRole: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    val userData = UserData(user?.uid, email, password, spinnerRole)
                    addUserToDatabase(userData)
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                    progressBarDialog.dismiss()
                    finish()
                } else {
                    Toast.makeText(this, "Signup failed. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    progressBarDialog.dismiss()
                }
            }
    }

    private fun addUserToDatabase(userData: UserData) {
        val id = userData.id ?: return
        databaseReference.child(id).setValue(userData)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@abes\\.ac\\.in$"
        val pattern = Pattern.compile(emailRegex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }
}
