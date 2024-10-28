package com.rspk.grocerylistapp.model

data class UserInfo(
    val userName: String = "",
    val userBio: String = "",
    val admin:Boolean = false,
    val email: String = "",
    val photoUrl: String? = "",
    val guestAccount:Boolean = false,
    val accountCreationDate:String = "",
    val lastLoginDate:String = "",
    val phoneNumber: String = "",
    val firebaseInstallationId:String = "",
)

data class UserInfoUpdate(
    val userName: String = "",
    val userBio: String = "",
    val email: String = "",
    val photoUrl: String? = "",
    val phoneNumber: String = "",
)