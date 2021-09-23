package com.example.openmobiliser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.openmobiliser.models.Location
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SubmitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        val title = findViewById<EditText>(R.id.submit_title)
        val desc = findViewById<EditText>(R.id.submit_desc)
        val btn = findViewById<View>(R.id.submit_btn)

        val data = intent.extras

        btn.setOnClickListener {
            val loc = Location(
                title.text.toString(),
                desc.text.toString(),
                data?.get("lat") as Double,
                data?.get("long") as Double
            )
            Firebase.firestore.collection("locations").document().set(loc)
            finish()
        }
    }
}