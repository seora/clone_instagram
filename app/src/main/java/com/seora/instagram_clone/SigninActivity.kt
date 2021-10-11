package com.seora.instagram_clone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)


        var btn_signup = findViewById<Button>(R.id.btn_signup)

        btn_signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }
}