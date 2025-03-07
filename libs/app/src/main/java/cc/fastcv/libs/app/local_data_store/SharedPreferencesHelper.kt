package cc.fastcv.libs.app.local_data_store

import android.content.Context

object SharedPreferencesHelper {

    /**
     * 存入String值
     */
    fun put(context: Context, spName:String, key: String, value: String) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putString(key, value).apply()
    }

    /**
     * 存入Int值
     */
    fun put(context: Context, spName:String , key: String, value: Int) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
    }

    /**
     * 存入Long值
     */
    fun put(context: Context, spName:String , key: String, value: Long) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putLong(key, value).apply()
    }

    /**
     * 存入Float值
     */
    fun put(context: Context, spName:String , key: String, value: Float) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putFloat(key, value).apply()
    }

    /**
     * 存入Boolean值
     */
    fun put(context: Context, spName:String , key: String, value: Boolean) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply()
    }

    /**
     * 存入Set<String?>值
     */
    fun put(context: Context, spName:String , key: String, values: Set<String?>) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().putStringSet(key, values).apply()
    }

    /**
     * 获取String值
     */
    fun getString(context: Context, spName:String , key: String, defaultValue: String): String {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getString(key, defaultValue)?:defaultValue
    }

    /**
     * 获取Int值
     */
    fun getInt(context: Context, spName:String , key: String, defaultValue: Int): Int {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getInt(key, defaultValue)
    }

    /**
     * 获取Long值
     */
    fun getLong(context: Context, spName:String , key: String, defaultValue: Long): Long {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getLong(key, defaultValue)
    }

    /**
     * 获取Float值
     */
    fun getFloat(context: Context, spName:String , key: String, defaultValue: Float): Float {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getFloat(key, defaultValue)
    }

    /**
     * 获取Boolean值
     */
    fun getBoolean(context: Context, spName:String , key: String, defaultValue: Boolean): Boolean {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getBoolean(key, defaultValue)
    }

    /**
     * 获取Set<String?>值
     */
    fun getStringSet(context: Context, spName:String , key: String, defaultValue: Set<String?>): Set<String>? {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).getStringSet(key, defaultValue)
    }

    /**
     * 判断是否包含key值
     */
    fun contains(context: Context, spName:String , key: String): Boolean {
        return context.getSharedPreferences(spName, Context.MODE_PRIVATE).contains(key)
    }

    /**
     * 移除key值及对应的value值
     */
    fun remove(context: Context, spName:String , key: String) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().remove(key).apply()
    }

    /**
     * 清空sp中的数据
     */
    fun clear(context: Context, spName:String) {
        context.getSharedPreferences(spName, Context.MODE_PRIVATE).edit().clear().apply()
    }
}