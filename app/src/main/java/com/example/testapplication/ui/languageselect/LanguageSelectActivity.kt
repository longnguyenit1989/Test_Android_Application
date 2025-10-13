package com.example.testapplication.ui.languageselect

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityLanguageSelectBinding
import com.example.testapplication.extension.beVisible
import com.example.testapplication.ui.MainActivity
import com.example.testapplication.utils.LocaleHelper

class LanguageSelectActivity : BaseActivity<ActivityLanguageSelectBinding>() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LanguageSelectActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityLanguageSelectBinding {
        return ActivityLanguageSelectBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            btnVietnamese.setOnClickListener {
                changeLanguage("vi")
            }

            btnEnglish.setOnClickListener {
                changeLanguage("en")
            }
        }
    }

    private fun changeLanguage(languageCode: String) {
        binding.apply {
            progressBar.beVisible()
            btnEnglish.isEnabled = false
            btnVietnamese.isEnabled = false
        }

        Handler(Looper.getMainLooper()).postDelayed({
            LocaleHelper.setLocale(applicationContext, languageCode)

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, 1000)
    }
}

