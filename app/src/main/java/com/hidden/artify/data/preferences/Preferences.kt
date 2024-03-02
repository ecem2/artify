package com.hidden.artify.data.preferences


interface Preferences {

    fun setFirstLaunch(isFirstTime: Boolean)

    fun getFirstLaunch(): Boolean

    fun setToken(token: String)

    fun getToken(): String

    fun setBaseUrl(url: String)

    fun getBaseUrl(): String

}