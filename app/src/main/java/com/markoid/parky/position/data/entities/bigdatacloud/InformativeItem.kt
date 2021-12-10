package com.markoid.parky.position.data.entities.bigdatacloud

import com.google.gson.annotations.SerializedName

data class InformativeItem(

    @field:SerializedName("geonameId")
    private val _geonameId: Int? = null,

    @field:SerializedName("wikidataId")
    private val _wikidataId: String? = null,

    @field:SerializedName("name")
    private val _name: String? = null,

    @field:SerializedName("description")
    private val _description: String? = null,

    @field:SerializedName("order")
    private val _order: Int? = null,

    @field:SerializedName("isoCode")
    private val _isoCode: String? = null
)
