package tech.abd3lraouf.learn.codeinjection

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast

class JavaScriptShareInterface(private val context: Context) {

    @JavascriptInterface
    @Suppress("unused")
    fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    companion object {
        /**
         * key used by Javascript to call the native function on
         */
        private const val JS_KEY = "Android"

        /**
         * key used as a wrapper key, not visible in Javascript
         */
        private const val JS_MEDIATOR_KEY = "JsMediator"

        /**
         * called at onCreate to initiate code injection
         */
        fun bind(context: Context, webView: WebView) {
            webView.addJavascriptInterface(JavaScriptShareInterface(context), JS_MEDIATOR_KEY)
        }

        /**
         * called in on destroy view to clear javascript interfaces for security reasons
         */
        fun unbind(webView: WebView) {
            webView.removeJavascriptInterface(JS_MEDIATOR_KEY)
        }

        /**
         * called before start loading the page, for better handling
         */
        fun injectMediator(webView: WebView) {
            webView.evaluateJavascript(
                "$JS_KEY = {}; $JS_KEY.toast = function(message) { $JS_MEDIATOR_KEY.toast(message); }",
                null
            )
        }

        /**
         * called to send a message to the web app
         */
        fun WebView.sendMessage(message: String) {
            evaluateJavascript("messageMe(\"$message\")", null)
        }
    }
}
