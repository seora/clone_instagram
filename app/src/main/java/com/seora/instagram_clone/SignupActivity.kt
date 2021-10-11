package com.seora.instagram_clone

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)


        var btn_signup = findViewById<Button>(R.id.signupBtn)
        btn_signup.setOnClickListener {
            CreateAccount()
        }
    }


    private fun CreateAccount(){

        val id_email = txt_emailId.text.toString()
        val fullname = txt_fullname.text.toString()
        val username = txt_username.text.toString()
        val password = txt_password.text.toString()

        when{
            TextUtils.isEmpty(fullname) -> Toast.makeText(this, "Full name을 입력해주세요", Toast.LENGTH_SHORT)
            TextUtils.isEmpty(id_email) -> Toast.makeText(this, "ID(Email)을 입력해주세요", Toast.LENGTH_SHORT)
            TextUtils.isEmpty(username) -> Toast.makeText(this, "Username을 입력해주세요", Toast.LENGTH_SHORT)
            TextUtils.isEmpty(password) -> Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT)

            else -> {

                val progressDialog = ProgressDialog(this@SignupActivity)
                progressDialog.setTitle("SignUp")
                progressDialog.setMessage("Please wait, this may take a while...")
                progressDialog.setCanceledOnTouchOutside(false)
                progressDialog.show()

                val mAuth : FirebaseAuth = FirebaseAuth.getInstance()
                mAuth.createUserWithEmailAndPassword(id_email, password)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            saveUserInfo(fullname, username, id_email, progressDialog)
                        }
                        else{
                            val message = task.exception!!.toString()
                            Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG)
                            mAuth.signOut()
                            progressDialog.dismiss()
                        }
                    }
            }

        }
    }


    private fun saveUserInfo(fullName : String, userName: String, id_email:String, progressDialog: ProgressDialog){
        val currentUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")

        val userMap = HashMap<String, Any>()
        userMap["uid"] = currentUserID
        userMap["fullname"] = currentUserID
        userMap["email"] = currentUserID
        userMap["bio"] = "using instagram clone app!"
        userMap["image"] = "gs://clone-instagram-app-9e6f7.appspot.com/Default Images/profile.png"

        usersRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    progressDialog.dismiss()
                    Toast.makeText(this, "Account has been created successfully", Toast.LENGTH_LONG)

                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)
                    finish()

                }
                else{
                    val message = task.exception!!.toString()
                    Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG)
                    FirebaseAuth.getInstance().signOut()
                    progressDialog.dismiss()
                }
            }

    }
}