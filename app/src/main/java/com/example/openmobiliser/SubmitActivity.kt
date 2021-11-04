package com.example.openmobiliser

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.children
import androidx.core.view.get
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.models.Locations
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.common.base.Strings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.ByteArrayOutputStream
import java.net.URI
import java.util.*
import com.firebase.ui.storage.images.FirebaseImageLoader

import com.google.firebase.storage.StorageReference

import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule

import com.bumptech.glide.module.AppGlideModule
import com.google.android.material.snackbar.Snackbar
import java.io.InputStream


class SubmitActivity : AppCompatActivity() {

    val REQUEST_CODE = 100
    private lateinit var filePath: Uri
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)

        val title = findViewById<EditText>(R.id.submit_title)
        val desc = findViewById<EditText>(R.id.submit_desc)
        val btn = findViewById<View>(R.id.submit_btn)
        val cg = findViewById<ChipGroup>(R.id.tagGroup)
        val imgBtn = findViewById<View>(R.id.submit_img)
        imageView = findViewById<ImageView>(R.id.imageview)

        var strings: ArrayList<String>

        val data = intent.extras

        imgBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }

        Locations.getCategories().forEach {
            val chip = Chip(this@SubmitActivity)
            chip.setText(it)
            cg.addView(chip)
            chip.isCheckable = true
        }

        btn.setOnClickListener {
            strings = ArrayList<String>()

            cg.checkedChipIds.forEach {
                val chip = findViewById<Chip>(it)
                strings.add(chip.text as String)
                Log.d("test","tet")
            }

            val name = UUID.randomUUID().toString()

            if(cg.checkedChipIds.size == 0 || title.text.toString() == "" || desc.text.toString() == ""
                || imageView.drawable == null)
                    Snackbar.make(window.decorView.rootView,"Invalid input",Snackbar.LENGTH_SHORT).show()
            else {

                val loc = Location(
                    title.text.toString(),
                    desc.text.toString(),
                    data?.get("lat") as Double,
                    data?.get("long") as Double,
                    strings,
                    1,
                    1,
                    name
                )
                Firebase.firestore.collection("locations").document().set(loc)
                Locations.getImageRef().child(name).putFile(filePath)

                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            filePath = data?.data!!
            imageView.setImageURI(filePath) // handle chosen image
        }
    }
}