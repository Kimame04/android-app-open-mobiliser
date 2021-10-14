package com.example.openmobiliser

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.with
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.models.Locations
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.storage.StorageReference
import java.io.InputStream

class InfoActivity : AppCompatActivity() {
    /*
    @GlideModule
    class AppGlide: AppGlideModule(){

        override fun registerComponents(
            context: android.content.Context,
            glide: Glide,
            registry: Registry
        ) {
            super.registerComponents(context, glide, registry)
            registry.append(
                StorageReference::class.java, InputStream::class.java,
                FirebaseImageLoader.Factory()
            )

        }
    }

     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val data: Location = intent.extras?.get("marker") as Location
        val title = findViewById<TextView>(R.id.display_title)
        val desc = findViewById<TextView>(R.id.display_desc)
        val chipGrp = findViewById<ChipGroup>(R.id.display_tags)
        val confirmBtn = findViewById<View>(R.id.display_confirm)
        val disputeBtn = findViewById<View>(R.id.display_dispute)
        val image = findViewById<ImageView>(R.id.display_image)

        title.setText(data.title)
        desc.setText(data.description)
        val ref = Locations.getImageRef().child(data.imageRef)
        Glide.with(applicationContext).load(ref).into(image)
        data.tags.forEach {
            val chip: Chip = Chip(this)
            chip.setText(it)
            chipGrp.addView(chip)
        }

    }
}