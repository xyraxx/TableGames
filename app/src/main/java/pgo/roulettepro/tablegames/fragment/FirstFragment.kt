package pgo.roulettepro.tablegames.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import pgo.roulettepro.tablegames.GlobalData
import pgo.roulettepro.tablegames.R
import pgo.roulettepro.tablegames.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var gameContent : WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        gameContent = binding.gameContent

        gameContent.webChromeClient = WebChromeClient()
        gameContent.settings.javaScriptEnabled = true
        gameContent.settings.loadsImagesAutomatically = true
        gameContent.settings.domStorageEnabled = true
        gameContent.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        gameContent.settings.allowContentAccess = true
        gameContent.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        gameContent.settings.javaScriptCanOpenWindowsAutomatically = true
        gameContent.settings.setSupportMultipleWindows(true)

        if(!GlobalData.apiResponse.contains("success")){
            binding.fab.visibility = View.GONE
        }

        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameContent.loadUrl(GlobalData.gameURL)
        Log.d("gameContent onViewCreated",GlobalData.gameURL)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}