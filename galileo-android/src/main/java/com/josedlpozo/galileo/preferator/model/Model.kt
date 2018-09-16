package com.josedlpozo.galileo.preferator.model

import android.content.SharedPreferences
import java.io.Serializable

typealias PreferenceItem = Pair<String, Any>

data class Preference(val name: String, val items: List<PreferenceItem> = listOf(), val sharedPreferences: SharedPreferences) {
    override fun toString(): String {
        return "===================================================\n" +
                "===================================================\n\n" +
                "$name --> \n\n" + items.joinToString("\n", transform = { "${it.first} --> ${it.second}" }) + "\n\n" +
                "===================================================\n" +
                "===================================================\n"
    }
}

data class Preferences(val items: List<Preference> = listOf())

data class PreferatorConfig(val showingSdkPreferences: Boolean = false) : Serializable