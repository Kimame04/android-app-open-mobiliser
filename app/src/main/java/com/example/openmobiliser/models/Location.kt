package com.example.openmobiliser.models

import java.io.Serializable

data class Location(
    val title: String = "",
    val description: String = "",
    val lat: Double,
    val long: Double,
    val tags: ArrayList<String>,
    val accepts: Long,
    val disputes: Long,
    val imageRef: String,
    val id: String
): Serializable