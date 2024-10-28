package com.rspk.grocerylistapp.model

import android.net.Uri

data class UserStorageInfo(
    val uri: Uri = Uri.EMPTY,
    val fileName: String = "",
    val size: Long = 0,
    val type: String = ""
)