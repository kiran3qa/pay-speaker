package com.example.pay_speaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PayTts.init(this)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 80, 40, 40)
            gravity = Gravity.CENTER_HORIZONTAL
        }

        statusText = TextView(this).apply {
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 30)
        }
        root.addView(statusText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        val openSettingsBtn = Button(this).apply {
            text = "Open Notification Access Settings"
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        root.addView(openSettingsBtn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 12, 0, 12) })

        val testVoiceBtn = Button(this).apply {
            text = "Test Voice"
            setOnClickListener {
                PayTts.speak(this@MainActivity, "This is a payment notification test. Rupees one hundred.")
            }
        }
        root.addView(testVoiceBtn, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 12, 0, 12) })

        val helpText = TextView(this).apply {
            textSize = 13f
            gravity = Gravity.CENTER
            text = "Will speak notifications from Google Pay and Paytm for Business only."
            setPadding(0, 20, 0, 0)
        }
        root.addView(helpText, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        setContentView(root)
    }

    override fun onResume() {
        super.onResume()
        updateNotificationAccessStatus()
    }

    private fun updateNotificationAccessStatus() {
        val enabled = isNotificationServiceEnabled(this)
        statusText.text = if (enabled) {
            "Notification access: ENABLED"
        } else {
            "Notification access: DISABLED\n(Open settings and enable for this app)"
        }
    }

    private fun isNotificationServiceEnabled(context: Context): Boolean {
        val packages = NotificationManagerCompat.getEnabledListenerPackages(context)
        return packages.contains(context.packageName)
    }

    override fun onDestroy() {
        super.onDestroy()
        PayTts.shutdown()
    }
}