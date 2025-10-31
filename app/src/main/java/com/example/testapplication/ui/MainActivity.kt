package com.example.testapplication.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.ui.analogclockview.AnalogClockActivity
import com.example.testapplication.ui.biometric.BiometricActivity
import com.example.testapplication.ui.broadcast_receiver.BroadcastReceiverActivity
import com.example.testapplication.ui.calendar.CalendarActivity
import com.example.testapplication.ui.canvas.CoordinateActivity
import com.example.testapplication.ui.chooseandcropimage.ChooseAndCropImageActivity
import com.example.testapplication.ui.circle.CircleActivity
import com.example.testapplication.ui.downloadimage.DownloadImageActivity
import com.example.testapplication.ui.draw.DrawActivity
import com.example.testapplication.ui.facebook.FaceBookActivity
import com.example.testapplication.ui.filterimage.FilterImageActivity
import com.example.testapplication.ui.languageselect.LanguageSelectActivity
import com.example.testapplication.ui.luckywheel.LuckyWheelActivity
import com.example.testapplication.ui.map.MapActivity
import com.example.testapplication.ui.movebutton.MoveButtonActivity
import com.example.testapplication.ui.portraitandlandscape.PortraitAndLandscapeActivity
import com.example.testapplication.ui.recycleviewmultilevel.RecycleViewMultiLevelActivity
import com.example.testapplication.ui.recycleviewswipe.RecycleViewSwipeActivity
import com.example.testapplication.ui.recycleviewtouch.RecycleViewTouchActivity
import com.example.testapplication.ui.searchtag.SearchTagActivity
import com.example.testapplication.ui.tiktok.TikTokActivity
import com.example.testapplication.ui.websocketchat.WebSocketChatActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {
    companion object {
        const val DEEP_LINK_KEY = "deeplink_key"
        private const val KEY = "key"
        private const val PATH_CALENDAR = "/calendar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        handleDeepLink(intent)
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // When MainActivity already exists and receives a new intent, the user clicks a deep link ⇒ the flow goes here
        handleDeepLink(intent)
    }

    private fun handleDeepLink(intent: Intent?) {
        val uri: Uri? = intent?.data
        if (uri != null && uri.path == PATH_CALENDAR) {
            val key = uri.getQueryParameter(KEY)
            val calendarIntent = CalendarActivity.newIntentWithKey(this, key ?: "")
            startActivity(calendarIntent)
        }
    }

    private fun setupUi() {
        binding.apply {
            btnMap.setOnClickListener {
                startActivity(MapActivity.Companion.newIntent(this@MainActivity))
            }

            btnCoordinate.setOnClickListener {
                startActivity(CoordinateActivity.Companion.newIntent(this@MainActivity))
            }

            btnMoveButton.setOnClickListener {
                startActivity(MoveButtonActivity.Companion.newIntent(this@MainActivity))
            }

            btnDraw.setOnClickListener {
                startActivity(DrawActivity.Companion.newIntent(this@MainActivity))
            }

            btnFilterImage.setOnClickListener {
                startActivity(FilterImageActivity.Companion.newIntent(this@MainActivity))
            }

            btnRecycleViewTouch.setOnClickListener {
                startActivity(RecycleViewTouchActivity.Companion.newIntent(this@MainActivity))
            }

            btnRecycleViewSwipe.setOnClickListener {
                startActivity(RecycleViewSwipeActivity.Companion.newIntent(this@MainActivity))
            }

            btnRecycleViewMultiLevel.setOnClickListener {
                startActivity(RecycleViewMultiLevelActivity.Companion.newIntent(this@MainActivity))
            }

            btnSearchTag.setOnClickListener {
                startActivity(SearchTagActivity.Companion.newIntent(this@MainActivity))
            }

            btnChooseAndCropImage.setOnClickListener {
                startActivity(ChooseAndCropImageActivity.Companion.newIntent(this@MainActivity))
            }

            btnDownloadImage.setOnClickListener {
                startActivity(DownloadImageActivity.Companion.newIntent(this@MainActivity))
            }

            btnChooseLanguage.setOnClickListener {
                startActivity(LanguageSelectActivity.Companion.newIntent(this@MainActivity))
            }

            btnBiometric.setOnClickListener {
                startActivity(BiometricActivity.Companion.newIntent(this@MainActivity))
            }

            btnCalendar.setOnClickListener {
                startActivity(CalendarActivity.Companion.newIntent(this@MainActivity))
            }

            btnCircle.setOnClickListener {
                startActivity(CircleActivity.Companion.newIntent(this@MainActivity))
            }

            btnPortraitAndLandscape.setOnClickListener {
                startActivity(PortraitAndLandscapeActivity.Companion.newIntent(this@MainActivity))
            }

            btnBroadcastReceiver.setOnClickListener {
                startActivity(BroadcastReceiverActivity.Companion.newIntent(this@MainActivity))
            }

            btnWebsocketChat.setOnClickListener {
                startActivity(WebSocketChatActivity.Companion.newIntent(this@MainActivity))
            }

            btnAnalogClock.setOnClickListener {
                startActivity(AnalogClockActivity.Companion.newIntent(this@MainActivity))
            }

            btnTikTok.setOnClickListener {
                startActivity(TikTokActivity.Companion.newIntent(this@MainActivity))
            }

            btnFaceBook.setOnClickListener {
                startActivity(FaceBookActivity.Companion.newIntent(this@MainActivity))
            }

            btnLuckyWheel.setOnClickListener {
                startActivity(LuckyWheelActivity.Companion.newIntent(this@MainActivity))
            }
        }
    }
}