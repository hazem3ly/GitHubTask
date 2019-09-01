package com.hazem.githubtask.data.network.response

import com.google.gson.annotations.SerializedName


data class Owner(
        @SerializedName("id") var id: Int?,
        @SerializedName("login") var login: String?,
        @SerializedName("avatar_url") var avatar_url: String?
)