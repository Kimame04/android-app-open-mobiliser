package com.example.openmobiliser.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MapViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Map Fragment"
    }
    val text: LiveData<String> = _text

}