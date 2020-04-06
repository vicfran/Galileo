/*
 * Copyright (C) 2016 The CyanogenMod Project
 *
 * Modified Work: Copyright (c) 2018 fr4nk1
 *
 * Modified Work: Copyright (c) 2018 josedlpozo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.josedlpozo.galileo.grid.utils

import android.content.Context
import android.content.SharedPreferences

internal object PreferenceUtils {
    private const val PREFERENCES_FILE = "com.josedlpozo.galileo.design_preferences"

    fun getShardedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, 0)
    }

    private fun putBoolean(context: Context, key: String, value: Boolean) {
        getShardedPreferences(context).edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(context: Context, key: String, defValue: Boolean): Boolean {
        return getShardedPreferences(context).getBoolean(key, defValue)
    }

    private fun putInt(context: Context, key: String, value: Int) {
        getShardedPreferences(context).edit().putInt(key, value).apply()
    }

    private fun getInt(context: Context, key: String, defValue: Int): Int {
        return getShardedPreferences(context).getInt(key, defValue)
    }

    object GridPreferences {
        const val KEY_GRID_TILE = "grid_enabled"
        const val KEY_SHOW_GRID = "grid_increments"
        const val KEY_SHOW_KEYLINES = "keylines"
        const val KEY_USE_CUSTOM_GRID_SIZE = "use_custom_grid_size"
        const val KEY_GRID_COLUMN_SIZE = "grid_column_size"
        const val KEY_GRID_ROW_SIZE = "grid_row_size"
        const val KEY_GRID_LINE_COLOR = "grid_line_color"
        const val KEY_KEYLINE_COLOR = "keyline_color"

        fun setGridEnabled(context: Context, enabled: Boolean) {
            putBoolean(context, KEY_GRID_TILE, enabled)
        }

        fun getGridEnabled(context: Context, defValue: Boolean): Boolean {
            return getBoolean(context, KEY_GRID_TILE, defValue)
        }

        fun getShowGrid(context: Context, defValue: Boolean): Boolean {
            return getBoolean(context, KEY_SHOW_GRID, defValue)
        }

        fun setShowKeylines(context: Context, show: Boolean) {
            putBoolean(context, KEY_SHOW_KEYLINES, show)
        }

        fun getShowKeylines(context: Context, defValue: Boolean): Boolean {
            return getBoolean(context, KEY_SHOW_KEYLINES, defValue)
        }

        fun setUseCustomGridSize(context: Context, use: Boolean) {
            putBoolean(context, KEY_USE_CUSTOM_GRID_SIZE, use)
        }

        fun getUseCustomGridSize(context: Context, defValue: Boolean): Boolean {
            return getBoolean(context, KEY_USE_CUSTOM_GRID_SIZE, defValue)
        }

        fun setGridColumnSize(context: Context, stepSize: Int) {
            putInt(context, KEY_GRID_COLUMN_SIZE, stepSize)
        }

        fun getGridColumnSize(context: Context, defValue: Int): Int {
            return getInt(context, KEY_GRID_COLUMN_SIZE, defValue)
        }

        fun setGridRowSize(context: Context, stepSize: Int) {
            putInt(context, KEY_GRID_ROW_SIZE, stepSize)
        }

        fun getGridRowSize(context: Context, defValue: Int): Int {
            return getInt(context, KEY_GRID_ROW_SIZE, defValue)
        }

        fun setGridLineColor(context: Context, color: Int) {
            putInt(context, KEY_GRID_LINE_COLOR, color)
        }

        fun getGridLineColor(context: Context, defValue: Int): Int {
            return getInt(context, KEY_GRID_LINE_COLOR, defValue)
        }

        fun setKeylineColor(context: Context, color: Int) {
            putInt(context, KEY_KEYLINE_COLOR, color)
        }

        fun getKeylineColor(context: Context, defValue: Int): Int {
            return getInt(context, KEY_KEYLINE_COLOR, defValue)
        }
    }

}