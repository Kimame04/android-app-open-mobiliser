package com.example.openmobiliser.models

import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.common.base.Strings
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.ArrayList

class Locations {
    companion object {
        private lateinit var locations: CollectionReference
        private lateinit var images: StorageReference
        private var list = ArrayList<Location>()
        private val cats = arrayOf("Category 1", "Category 2", "Category 3")

        fun retrieveLocations(){
            list.clear()
            locations = Firebase.firestore.collection("locations")
            locations.get().addOnSuccessListener { result ->
                for (document in result){
                    val loc = Location(
                        document.get("title") as String,
                        document.get("description") as String,
                        document.get("lat") as Double,
                        document.get("long") as Double,
                        document.get("tags") as ArrayList<String>,
                        document.get("accepts") as Long,
                        document.get("disputes") as Long,
                        document.get("imageRef") as String
                    )
                    list.add(loc)
                }
            }.addOnFailureListener{ exception ->
            }

        }

        fun retrieveImages(){
            images = FirebaseStorage.getInstance().reference
        }

        fun get(): ArrayList<Location> {
            return list;
        }

        fun getImageRef(): StorageReference {
            return images
        }

        fun getCategories(): Array<String> {
            return cats
        }
    }

}