package com.theapache64.nemo.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import com.theapache64.nemo.data.remote.Config
import com.theapache64.nemo.data.remote.ConfigJsonAdapter
import com.theapache64.nemo.data.remote.NemoApi
import javax.inject.Inject

/**
 * Created by theapache64 : Aug 02 Sun,2020 @ 16:33
 */
class ConfigRepo @Inject constructor(
    private val nemoApi: NemoApi,
    private val moshi: Moshi,
    private val sharedPreferences: SharedPreferences
) {
    companion object {
        private const val KEY_CONFIG = "config"
    }

    private val configJsonAdapter by lazy {
        ConfigJsonAdapter(moshi)
    }

    fun getRemoteConfig() =
        nemoApi.getConfig()

    fun getLocalConfig(): Config? {
        return sharedPreferences.getString(KEY_CONFIG, null)?.run {
            configJsonAdapter.fromJson(this)
        }
    }

    fun saveConfigToLocal(data: Config) {
        sharedPreferences.edit {
            putString(KEY_CONFIG, configJsonAdapter.toJson(data))
        }
    }

}