package com.rmaproject.storyapp.data.preferences

import com.chibatching.kotpref.KotprefModel

object UserInfo : KotprefModel() {
    var name by stringPref("", key = "userEmail")
    var token by stringPref("", key = "userToken")
    var userId by stringPref("", key = "userId")

    override fun clear() {
        super.clear()
        name = ""
        token = ""
        userId = ""
    }
}