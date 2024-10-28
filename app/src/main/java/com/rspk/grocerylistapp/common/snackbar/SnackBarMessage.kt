package com.rspk.grocerylistapp.common.snackbar

import android.content.Context
import androidx.annotation.StringRes
import com.rspk.grocerylistapp.R.string as AppText

sealed class SnackBarMessage {
    class StringSnackBar(val message: String) : SnackBarMessage()
    class ResourceSnackBar(@StringRes val message: Int) : SnackBarMessage()

    companion object{
        fun SnackBarMessage.toMessage(context: Context):String{
            //here [this] is the either one the classes above since we wrote
            //our function as extension function of sealed class
            return when(this){
                is StringSnackBar -> this.message
                is ResourceSnackBar -> context.resources.getString(this.message)
            }
        }

        fun Throwable.toSnackMessage():SnackBarMessage{
            val message = this.message.orEmpty()//got text if not empty show 1 else from resources show general error of 2
            return if(message.isNotBlank()) StringSnackBar(message)
            else ResourceSnackBar(AppText.generic_error)
        }
    }
}