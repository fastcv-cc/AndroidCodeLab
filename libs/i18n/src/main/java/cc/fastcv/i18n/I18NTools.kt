package cc.fastcv.i18n

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import java.util.Locale

/**
 * aab打包时会剔除其他语言包 请加上这个
 *     bundle {
 *         language {
 *             enableSplit = false
 *         }
 *     }
 *
 * 使用WebView时  会刷新Application的语言  请实时更新它
 */
class I18NTools {

    private lateinit var application: Application

    fun initI18n(application: Application) {
        this.application = application
    }

    fun applyAppLanguage(context: Context?, language: String): Context? {
        if (context == null) return null

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //7.0及以上的方法
            createConfiguration(context, language)
        } else {
            updateConfiguration(context, language)
            context
        }
    }

    private fun updateApplicationContext(language: String) {
        //更新ApplicationContext的语言设置
        val resources = application.resources
        val configuration = resources.configuration
        val locale = when (language) {
            "zh_CN" -> {
                Locale.SIMPLIFIED_CHINESE
            }

            "zh_HK" -> {
                Locale.TRADITIONAL_CHINESE
            }

            else -> {
                Locale(language)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
        } else {
            configuration.setLocale(locale)
        }
        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createConfiguration(context: Context, language: String): Context {
        val resources = context.resources
        val locale = when (language) {
            "zh_CN" -> {
                Locale.SIMPLIFIED_CHINESE
            }

            "zh_HK" -> {
                Locale.TRADITIONAL_CHINESE
            }

            else -> {
                Locale(language)
            }
        }

        val configuration = resources.configuration
        configuration.setLocale(locale)
        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun updateConfiguration(context: Context, language: String) {
        val resources = context.resources
        val locale = when (language) {
            "zh_CN" -> {
                Locale.SIMPLIFIED_CHINESE
            }

            "zh_HK" -> {
                Locale.TRADITIONAL_CHINESE
            }

            else -> {
                Locale(language)
            }
        }

        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        val displayMetrics = resources.displayMetrics
        resources.updateConfiguration(configuration, displayMetrics)
    }

    private fun getSystemLanguage(): String {
        val language: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            application.resources.configuration.locales[0].language
        } else {
            application.resources.configuration.locale.language
        }
        return language
    }

    /**
     * 获取国家码
     */
    fun getCountryZipCode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            application.resources.configuration.locales[0].country
        } else {
            application.resources.configuration.locale.country
        }
    }

}