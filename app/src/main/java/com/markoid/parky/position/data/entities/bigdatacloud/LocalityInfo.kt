package com.markoid.parky.position.data.entities.bigdatacloud

import com.google.gson.annotations.SerializedName

data class LocalityInfo(

    @field:SerializedName("administrative")
    private val _administrative: List<AdministrativeItem?>? = null,

    @field:SerializedName("informative")
    private val _informative: List<InformativeItem?>? = null
)
