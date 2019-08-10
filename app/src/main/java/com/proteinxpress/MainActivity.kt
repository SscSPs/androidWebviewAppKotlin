package com.proteinxpress

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.StrictMode
import android.view.KeyEvent
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import java.net.InetSocketAddress
import java.net.Socket


class MainActivity : Activity() {
    private lateinit var myWebView: WebView
    private val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()

    companion object {
        const val protocol = "https://"
        const val websiteDomain = "proteinxpress.com"
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE) //will hide the title
        myWebView = WebView(this)
        setContentView(myWebView)
        StrictMode.setThreadPolicy(policy)
        myWebView.settings.javaScriptEnabled = true
        myWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        myWebView.webViewClient = MyWebViewClient()
        startApp()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return if (myWebView.canGoBack()) {
                myWebView.goBack()
                true
            } else {
                showAlertBoxForExit(getString(R.string.exitingString), getString(R.string.exitingDescription), true)
                true
            }
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event)
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return false
        }
    }

    private fun showAlertBoxForExit(header: String, body: String, cancelable: Boolean) {
        AlertDialog.Builder(this)
                .setTitle(header)
                .setMessage(body)
                .setCancelable(cancelable)
                .setPositiveButton(getString(R.string.confirmWord)) { _: DialogInterface, _: Int -> this.finish() }
                .show()
    }

    private fun startApp() {
        if (isNetworkAvailable(websiteDomain)) {
            myWebView.loadUrl(protocol + websiteDomain)
        } else {
            //Exit the app if no internet is available
            showAlertBoxForExit(getString(R.string.noInternetConnection),
                    getString(R.string.noInternetHelpText),
                    false)
        }
    }

    private fun isNetworkAvailable(testURL: String): Boolean {
        return try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(testURL, 80), 2000)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
