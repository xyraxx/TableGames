package pgo.roulettepro.tablegames.main

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import pgo.roulettepro.tablegames.GlobalData
import pgo.roulettepro.tablegames.databinding.ActivityPolicyConsentBinding

class PolicyConsent : AppCompatActivity() {

    private lateinit var policyView : WebView
    private lateinit var acpt: Button
    private lateinit var decl: Button

    private lateinit var binding : ActivityPolicyConsentBinding

    private var consentDialog : AlertDialog.Builder? = null
    private lateinit var sf : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPolicyConsentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sf = getSharedPreferences(GlobalData.appCode, MODE_PRIVATE)

        policyView = binding.policyView
        acpt = binding.BtnAgree
        decl = binding.BtnDisagree

        policyView.webViewClient = WebViewClient()
        policyView.loadUrl(GlobalData.policyURL)

        acpt.setOnClickListener {
            consentDialog = AlertDialog.Builder(this@PolicyConsent)
            consentDialog!!.setTitle("User Data Consent")
            consentDialog!!.setMessage("We may collect your information based on your activities during the usage of the app, to provide better user experience.")
            consentDialog!!.setPositiveButton(
                "Agree"
            ) { dialogInterface: DialogInterface, _: Int ->
                sf.edit().putBoolean("permitSendData", true).apply()
                sf.edit().putString("userConsent","accepted")
                dialogInterface.dismiss()
            }
            consentDialog!!.setNegativeButton(
                "Disagree"
            ) { dialogInterface: DialogInterface, _: Int ->
                sf.edit().putBoolean("permitSendData", false).apply()
                sf.edit().putString("userConsent","decline")
                dialogInterface.dismiss()
            }

            consentDialog!!.setOnDismissListener {
                if (sf.getBoolean("permitSendData",true)) {
                    openGame()
                } else openGame()
            }
            consentDialog!!.show()

        }
        decl.setOnClickListener {
            finishAffinity()
        }
    }

    private fun openGame() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}