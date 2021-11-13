package tech.abd3lraouf.learn.codeinjection

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebView.setWebContentsDebuggingEnabled
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import tech.abd3lraouf.learn.codeinjection.JavaScriptShareInterface.Companion.sendMessage
import tech.abd3lraouf.learn.codeinjection.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            settings.javaScriptEnabled = true
            setWebContentsDebuggingEnabled(true)
            JavaScriptShareInterface.bind(requireContext(), this)
            webViewClient = CustomWebViewClient

            loadUrl("https://abd3lraouf.github.io/webview_code_injection_from_android")
        }

        binding.btnSend.setOnClickListener {
            val message = binding.etMessage.text.toString()
            binding.etMessage.setText("")
            binding.webView.sendMessage(message = message)
            hideKeyboard()
        }
    }

    private fun Fragment.hideKeyboard(): Boolean {
        return (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow((activity?.currentFocus ?: View(context)).windowToken, 0)
    }

    override fun onDestroyView() {
        JavaScriptShareInterface.unbind(binding.webView)
        _binding = null
        super.onDestroyView()
    }

    object CustomWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            JavaScriptShareInterface.injectMediator(view)
        }
    }
}
