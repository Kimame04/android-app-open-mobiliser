package com.example.openmobiliser.ui.map

import android.content.Intent
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
import com.example.openmobiliser.SubmitActivity
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.example.openmobiliser.models.Location
import com.example.openmobiliser.models.Locations
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
        val locations = Locations.get()

        for (loc in locations){
            googleMap.addMarker(MarkerOptions()
                .position(
                    LatLng(loc.lat,loc.long),
                )
                .title(loc.title)
            )
        }

        googleMap.setOnMapLongClickListener { result ->
            val intent = Intent(activity, SubmitActivity::class.java)
                .putExtra("lat",result.latitude)
                .putExtra("long",result.longitude)
            startActivity(intent)
        }

    }

}