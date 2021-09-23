package com.example.openmobiliser.ui.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.BuildConfig
import com.example.openmobiliser.R
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.ui.submission.SubmissionFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var galleryViewModel: MapViewModel
    private lateinit var map: MapView
    private var _binding: FragmentMapBinding? = null
    private lateinit var locations: CollectionReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        galleryViewModel =
            ViewModelProvider(this).get(MapViewModel::class.java)

        _binding = FragmentMapBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        val fab: View = binding.fab
        fab.setOnClickListener { view ->
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.map,SubmissionFragment())
                setTransition(TRANSIT_FRAGMENT_OPEN)
                commit()
            }

        }*/

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val locations = Firebase.firestore.collection("locations")

        locations.get().addOnSuccessListener { result ->
            for (document in result){
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(document.get("lat") as Double,
                            document.get("long") as Double
                        ))
                        .title(document.get("title") as String?)
                )
            }
            Toast.makeText(this.context, "data retrieved successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{ exception ->
            Toast.makeText(this.context, "failed to retrieve data", Toast.LENGTH_SHORT).show()
        }

        googleMap.setOnMapLongClickListener { result ->
            val location = Location("temp","temp",result.latitude,result.longitude)
            locations.document().set(location)
            googleMap.addMarker(
                MarkerOptions().position(result).title(location.title)
            )
        }

    }

}