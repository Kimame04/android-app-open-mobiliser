package com.example.openmobiliser.models

import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class Locations {
    companion object {
        private lateinit var locations: CollectionReference
        private var list = ArrayList<Location>()

        fun retrieveLocations(){
            locations = Firebase.firestore.collection("locations")
            locations.get().addOnSuccessListener { result ->
                for (document in result){
                    val loc = Location(
                        document.get("title") as String,
                        document.get("description") as String,
                        document.get("lat") as Double,
                        document.get("long") as Double
                    )
                    list.add(loc)
                }
            }.addOnFailureListener{ exception ->
            }

            locations.addSnapshotListener{ value, e ->
                for (document in value!!){
                    val loc = Location(
                        document.get("title") as String,
                        document.get("description") as String,
                        document.get("lat") as Double,
                        document.get("long") as Double
                    )
                    list.add(loc)
                }
            }
        }

        fun get(): ArrayList<Location> {
            return list;
        }
    }

}