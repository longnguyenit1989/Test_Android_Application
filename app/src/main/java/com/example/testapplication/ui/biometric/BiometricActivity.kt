package com.example.testapplication.ui.biometric

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityBiometricBinding
import com.example.testapplication.extension.beGone
import com.example.testapplication.extension.beVisible
import com.example.testapplication.utils.SecurePrefs
import java.util.concurrent.Executor

class BiometricActivity : BaseActivity<ActivityBiometricBinding>() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private var isSettingPin = false
    private var isSettingPattern = false

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, BiometricActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityBiometricBinding {
        return ActivityBiometricBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor, biometricCallback)

        setupButtons()
        showOptionSelection()
    }

    private fun setupButtons() {
        with(binding) {
            btnUseFace.setOnClickListener { startFaceAuth() }
            btnUseBiometric.setOnClickListener { startFingerprintAuth() }
            btnUsePin.setOnClickListener { showPin(false) }
            btnUsePattern.setOnClickListener { showPattern(false) }

            btnSetPin.setOnClickListener { showPin(true) }
            btnSetPattern.setOnClickListener { showPattern(true) }

            btnBack.setOnClickListener {
                pinContainer.beGone()
                patternContainer.beGone()
                optionContainer.beVisible()
                tvStatus.text = getString(R.string.status_choose_method)
            }
        }
    }

    private fun showOptionSelection() {
        binding.optionContainer.beVisible()
        binding.pinContainer.beGone()
        binding.patternContainer.beGone()
    }

    private fun showPin(settingMode: Boolean) {
        isSettingPin = settingMode
        binding.optionContainer.beGone()
        binding.patternContainer.beGone()
        binding.pinContainer.beVisible()
        binding.etPin.text?.clear()

        if (isSettingPin) {
            binding.tvStatus.text = getString(R.string.status_enter_new_pin)
            binding.btnSubmitPin.text = getString(R.string.submit)
        } else {
            binding.tvStatus.text = getString(R.string.status_enter_pin)
            binding.btnSubmitPin.text = getString(R.string.submit)
        }

        binding.btnSubmitPin.setOnClickListener {
            val pin = binding.etPin.text.toString()
            if (pin.length != 4) {
                Toast.makeText(this, getString(R.string.pin_length_error), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isSettingPin) {
                SecurePrefs.savePin(this, pin)
                Toast.makeText(this, getString(R.string.pin_saved), Toast.LENGTH_SHORT).show()
                showOptionSelection()
            } else {
                val saved = SecurePrefs.getPin(this)
                if (pin == saved) onAuthenticated(getString(R.string.method_pin))
                else binding.tvStatus.text = getString(R.string.pin_incorrect)
            }
        }
    }

    private fun showPattern(settingMode: Boolean) {
        isSettingPattern = settingMode
        binding.optionContainer.beGone()
        binding.pinContainer.beGone()
        binding.patternContainer.beVisible()
        binding.patternView.clearPattern()

        if (isSettingPattern) {
            binding.tvStatus.text = getString(R.string.status_draw_new_pattern)
        } else {
            binding.tvStatus.text = getString(R.string.status_draw_pattern)
        }

        binding.patternView.setOnPatternListener { pattern ->
            if (pattern.size < 4) {
                Toast.makeText(this, getString(R.string.pattern_too_short), Toast.LENGTH_SHORT).show()
                return@setOnPatternListener
            }

            val patternStr = pattern.joinToString("-")
            if (isSettingPattern) {
                SecurePrefs.savePattern(this, patternStr)
                Toast.makeText(this, getString(R.string.pattern_saved), Toast.LENGTH_SHORT).show()
                showOptionSelection()
            } else {
                val saved = SecurePrefs.getPattern(this)
                if (patternStr == saved) onAuthenticated(getString(R.string.method_pattern))
                else {
                    binding.tvStatus.text = getString(R.string.pattern_incorrect)
                    binding.patternView.clearPattern()
                }
            }
        }
    }

    private fun startFingerprintAuth() {
        val manager = BiometricManager.from(this)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.auth_fingerprint_title))
                    .setSubtitle(getString(R.string.auth_fingerprint_subtitle))
                    .setNegativeButtonText(getString(R.string.auth_cancel))
                    .setAllowedAuthenticators(authenticators)
                    .build()
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                binding.tvStatus.text = getString(R.string.no_fingerprint_sensor)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                binding.tvStatus.text = getString(R.string.no_fingerprint_enrolled)
            else ->
                binding.tvStatus.text = getString(R.string.no_biometric_available)
        }
    }

    private fun startFaceAuth() {
        val manager = BiometricManager.from(this)
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_WEAK

        when (manager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(getString(R.string.auth_face_title))
                    .setSubtitle(getString(R.string.auth_face_subtitle))
                    .setNegativeButtonText(getString(R.string.auth_cancel))
                    .setAllowedAuthenticators(authenticators)
                    .build()
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                binding.tvStatus.text = getString(R.string.no_face_support)
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                binding.tvStatus.text = getString(R.string.no_face_enrolled)
            else ->
                binding.tvStatus.text = getString(R.string.no_biometric_available)
        }
    }

    private val biometricCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onAuthenticated(getString(R.string.method_biometric))
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            binding.tvStatus.text = getString(R.string.auth_error, errString)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            binding.tvStatus.text = getString(R.string.auth_failed)
        }
    }

    private fun onAuthenticated(method: String) {
        binding.tvStatus.text = getString(R.string.auth_success, method)
        Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()

        binding.pinContainer.beGone()
        binding.patternContainer.beGone()
        binding.optionContainer.beVisible()
    }
}
