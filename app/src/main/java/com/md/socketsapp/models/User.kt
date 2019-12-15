package com.md.socketsapp.models

import com.google.gson.annotations.SerializedName

data class User(val username: String,
                @SerializedName("device_id")
                val deviceId: String)