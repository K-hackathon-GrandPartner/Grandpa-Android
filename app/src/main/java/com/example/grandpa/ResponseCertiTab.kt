package com.example.grandpa

data class ResponseCertiTab(
    val `data`: List<Data>,
    val message: String,
    val status: Int,
    val success: Boolean
)
{
    data class Data(
        val id: Int,
        val login_type: Int,
        val user_name: String,
        val status: Int,
    )
}