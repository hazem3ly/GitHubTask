package com.hazem.githubtask.extensions

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun Uri.fileName(context: Context): String {
    val cursor: Cursor? = context.contentResolver.query(
            this, null,
            null,
            null,
            null,
            null
    )
    var displayName = "Unknown"
    cursor?.use {
        // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
        // "if there's anything to look at, look at it" conditionals.
        if (it.moveToFirst()) {
            // Note it's called "Display Name".  This is
            // provider-specific, and might not necessarily be the file name.
            displayName =
                    it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))

        } else {
            "Unknown"
        }
    }
    return displayName
}

fun Uri.createTempFile(context: Context): File {
    val f = File(context.cacheDir.parent + File.separator + fileName(context))
    try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            val out = FileOutputStream(f)
            val buf = ByteArray(1024)
            while (true) {
                val length = inputStream.read(buf)
                if (length <= 0)
                    break
                out.write(buf, 0, length)
            }
            out.close()
        }
        return f
    } catch (e: IOException) {
        throw IOException()
    }
}

fun TextInputEditText.isValidateEditText(): Boolean {
    return this.text?.isNotEmpty() ?: false
}

fun TextInputEditText.value(): String {
    return this.text.toString()
}

fun String.saveToSP(context: Context): Boolean {
    val sp = context.getSharedPreferences("my_sp", 0)
    val editor = sp.edit()
    editor.putString("user", this)
    return editor.commit()
}


fun Context.loadSavedUser(): String {
    val sp = getSharedPreferences("my_sp", 0)
    return sp.getString("user", "") ?: ""
}
