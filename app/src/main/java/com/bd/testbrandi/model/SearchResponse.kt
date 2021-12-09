package com.bd.testbrandi.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("documents") val documents: List<Documents>
)