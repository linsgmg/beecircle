package com.xz.beecircle.activity.mine

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import com.lin.baselib.utils.WebViewInitUtils
import com.xz.beecircle.R
import com.xz.beecircle.base.ModuleActivity
import com.xz.beecircle.databinding.ActivityBrouserBinding


class BrowserActivity : ModuleActivity<ActivityBrouserBinding>() {

    private  var link: String? = null

    override fun initView() {
        link = intent.getStringExtra("link")

        addTitleBar {
            val title = it.findViewById<TextView>(R.id.tv_title)
            title.text = "在线客服"
        }
        initWebView()
    }

    /**
     * 初始化webView
     */
    private fun initWebView() {
        binding.apply {
            WebViewInitUtils.init(webView)
            //设置客户端-不跳转到默认浏览器中
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                    return try {
                        if (url.startsWith("http:") || url.startsWith("https:")) {
                            view!!.loadUrl(url)
                        } else {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            startActivity(intent)
                        }
                        true
                    } catch (e: Exception) {
                        false
                    }
                }
            }
            //监听手机返回按键：
            webView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        //表示按返回键时的操作
                        webView.goBack()
                        return@OnKeyListener true
                    }
                }
                false
            })
            //访问网页
            webView.loadUrl(link!!)
        }
    }

    override fun initDate() {

    }

}