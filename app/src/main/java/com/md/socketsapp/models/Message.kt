package com.md.socketsapp.models

data class Message(
    val id: Long,
    val message: String = "",
    val user: String = ""
)
