package com.project.samay.data.source.local

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey

object PreferencesKeys {
    val TARGET_KEY = longPreferencesKey("target_key_v2")
    val GOAL_CALENDER_KEY = intPreferencesKey("goal_calender_key")
}


