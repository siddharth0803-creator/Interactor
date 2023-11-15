package com.example.interactor_nitrr.model

data class UserModel(
    val number: String?="",
    val name: String?="",
    val branch: String?="",
    val interests: ArrayList<String>?=null,
    val image: String?="",
    val email:String?=""
)
