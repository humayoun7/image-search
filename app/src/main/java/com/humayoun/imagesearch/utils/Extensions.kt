package com.humayoun.imagesearch.utils

import android.app.Activity
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            true
        }

        false
    }
}


fun Activity.hideKeyboard() {
    val inputMethodManager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
}