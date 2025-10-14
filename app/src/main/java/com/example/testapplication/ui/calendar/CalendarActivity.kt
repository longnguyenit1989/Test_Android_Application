package com.example.testapplication.ui.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewpager2.widget.ViewPager2
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityCalendarBinding
import com.example.testapplication.utils.LocaleHelper
import java.time.format.TextStyle
import java.util.Locale

class CalendarActivity : BaseActivity<ActivityCalendarBinding>() {

    private lateinit var adapter: CalendarPagerAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, CalendarActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityCalendarBinding {
        return ActivityCalendarBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        adapter = CalendarPagerAdapter(this)
        binding.viewPagerCalendar.adapter = adapter

        var locale = Locale.ENGLISH
        if (LocaleHelper.getSavedLanguage(this) == LocaleHelper.KEY_LANGUAGE_VI) {
            locale = Locale.forLanguageTag("vi")
        }

        val currentPos = adapter.itemCount / 2
        binding.viewPagerCalendar.setCurrentItem(currentPos, false)
        updateMonthTitle(currentPos, locale)

        binding.viewPagerCalendar.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateMonthTitle(position, locale)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateMonthTitle(position: Int, locale: Locale) {
        val ym = adapter.getMonthAt(position)
        val monthName = ym.month.getDisplayName(TextStyle.SHORT, locale).uppercase()
        binding.tvMonthTitle.text = "$monthName ${ym.year}"
    }

}
