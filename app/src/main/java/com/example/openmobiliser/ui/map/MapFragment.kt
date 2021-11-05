package com.example.openmobiliser.ui.map

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.*
import androidx.lifecycle.ViewModelProvider
import com.example.openmobiliser.InfoActivity
import com.example.openmobiliser.R
import com.example.openmobiliser.SubmitActivity
import com.example.openmobiliser.databinding.FragmentMapBinding
import com.example.openmobiliser.models.Locations
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.CollectionReference
import org.osmdroid.views.MapView
import java.io.Serializable

class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var galleryViewModel: MapViewModel
    private lateinit var map: MapView
    private var _binding: FragmentMapBinding? = null
    private lateinit var locations: CollectionReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                val loc = LatLng(location!!.latitude,location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15.0f))
                googleMap.addMarker(MarkerOptions()
                    .position(
                        LatLng(location.latitude,location.longitude),
                    )
                    .icon(BitmapDescriptorFactory.fromBitmap(
                        getDrawable(resources,R.drawable.ic_action_name,null)!!.toBitmap()))
                )
            }
            .addOnFailureListener {
                val sg = LatLng(1.3521,103.8198)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sg,15.0f))
            }

        locations.forEach {
            val marker = MarkerOptions()
                .position(
                    LatLng(it.lat,it.long),
                )
                .title(it.title)

            if (it.tags.contains(Locations.getCategories().get(1))){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            } else if (it.tags.contains(Locations.getCategories().get(2))){
                marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            }
            googleMap.addMarker(marker).tag = it
        }

        googleMap.setOnMapLongClickListener { result ->
            val intent = Intent(activity, SubmitActivity::class.java)
                .putExtra("lat",result.latitude)
                .putExtra("long",result.longitude)
            startActivity(intent)
        }

        googleMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(activity, InfoActivity::class.java)
                .putExtra("marker", marker.tag as Serializable)
            startActivity(intent)
        }

    }

}