package com.seora.instagram_clone

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        btn_cancel.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))

        }

        btn_save.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}