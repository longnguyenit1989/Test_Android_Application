package com.example.testapplication.ui.luckywheel

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AlertDialog
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityLuckyWheelBinding
import kotlin.random.Random

class LuckyWheelActivity : BaseActivity<ActivityLuckyWheelBinding>() {

    private var choices = mutableListOf<String>()
    private var isSpinning = false

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LuckyWheelActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityLuckyWheelBinding {
        return ActivityLuckyWheelBinding.inflate(inflater)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            // setData() first times
            edtChoices.setText("Táo Lê Mận Cam Cải")
            setData()

            btnStart.setOnClickListener {
                setData()
            }
            wheelView.setOnClickListener {
                spinWheel()
            }
        }

    }

    private fun setData() {
        val input = binding.edtChoices.text.toString()
        choices = input.split(" ").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
        binding.wheelView.setItems(choices)
    }

    private fun spinWheel() {
        if (choices.isEmpty() || isSpinning) return

        isSpinning = true
        binding.wheelView.isClickable = false

        val size = choices.size
        val sweep = 360f / size
        val randomIndex = Random.nextInt(size)

        val currentRotation = (binding.wheelView.rotation % 360 + 360) % 360
        val angleCenter = (360f - (randomIndex * sweep + sweep / 2f)) % 360f
        val delta = (angleCenter - currentRotation + 360f) % 360f
        val extraRounds = 5
        val targetRotation = currentRotation + extraRounds * 360f + delta

        val animator = ObjectAnimator.ofFloat(
            binding.wheelView,
            "rotation",
            binding.wheelView.rotation,
            targetRotation
        )
        animator.duration = 4200L
        animator.interpolator = DecelerateInterpolator()
        animator.start()

        animator.addListener(onEnd = {
            binding.wheelView.rotation = angleCenter
            val cx = binding.wheelView.x + binding.wheelView.width / 2
            val cy = binding.wheelView.y + binding.wheelView.height / 2
            binding.confettiView.launchFirework(cx, cy, 250)
            binding.confettiView.postDelayed({
                showResultDialog(randomIndex)
            }, 800)

            isSpinning = false
            binding.wheelView.isClickable = true
        })
    }

    private fun showResultDialog(resultIndex: Int) {
        val result = choices[resultIndex]

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.result))
            .setMessage(result)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.delete)) { dialog, _ ->
                // Remove result from choices
                binding.wheelView.removeItem(resultIndex)
                choices.removeAt(resultIndex)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.back)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
