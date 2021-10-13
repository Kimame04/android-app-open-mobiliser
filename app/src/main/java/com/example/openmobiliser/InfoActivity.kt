package com.example.openmobiliser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.openmobiliser.models.Location
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.io.Serializable

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val data: Location = intent.extras?.get("marker") as Location
        val title = findViewById<TextView>(R.id.display_title)
        val desc = findViewById<TextView>(R.id.display_desc)
        val chipGrp = findViewById<ChipGroup>(R.id.display_tags)
        val confirmBtn = findViewById<View>(R.id.display_confirm)
        val disputeBtn = findViewById<View>(R.id.display_dispute)

        title.setText(data.title)
        desc.setText(data.description)
        data.tags.forEach {
            val chip: Chip = Chip(this)
            chip.setText(it)
            chipGrp.addView(chip)
        }

    }
}