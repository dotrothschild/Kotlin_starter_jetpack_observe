package com.dotrothschild.mysamplesensorswithlivedata.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rank(
    @SerializedName("Id") val id: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("ImageName") val imageName: String,
    @SerializedName("Date") val date: String? = null,
    @SerializedName("Description") val description: String
): Parcelable

