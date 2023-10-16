package pgo.roulettepro.tablegames.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.navigation.fragment.findNavController
import pgo.roulettepro.tablegames.GlobalData
import pgo.roulettepro.tablegames.R
import pgo.roulettepro.tablegames.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var policyContent : WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        policyContent = binding.policyContent

        policyContent.loadUrl(GlobalData.policyURL)

        binding.fab.setOnClickListener { view ->
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        return binding.root

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}