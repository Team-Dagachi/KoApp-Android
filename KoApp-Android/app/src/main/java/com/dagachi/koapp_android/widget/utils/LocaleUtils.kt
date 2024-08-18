package com.dagachi.koapp_android.widget.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale

/* Locale 관련 파일 */
class LocaleUtils {
    companion object {
        // 사용자가 핸드폰에 적용 중인 언어 정보 가져오기
        fun getCurrentLanguage(context: Context): String {
            val locale: Locale = context.resources.configuration.locales[0]
            return locale.language
        }

        // locale 정보 가져오기
        private fun getLocalizedResources(context: Context, locale: Locale): Resources {
            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config).resources
        }

        // strings.xml에 저장된 한국어 값 가져오기
        fun getKoreanStrings(context: Context, resId: Int): String {
            val locale = Locale("ko", "KR")
            return getLocalizedResources(context, locale).getString(resId)
        }

        // strings.xml에 저장된 베트남어 값 가져오기
        fun getVietnameseStrings(context: Context, resId: Int): String {
            val locale = Locale("vi")
            return getLocalizedResources(context, locale).getString(resId)
        }

        // strings.xml에 저장된 중국어(간체) 값 가져오기
        fun getChineseStrings(context: Context, resId: Int): String {
            val locale = Locale("zh", "CN")
            return getLocalizedResources(context, locale).getString(resId)
        }
    }
}