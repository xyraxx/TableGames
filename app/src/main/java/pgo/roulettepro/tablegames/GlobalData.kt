package pgo.roulettepro.tablegames

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class GlobalData : Application(){

    private var GlobalData = Application()

    companion object {
        const val appCode = "TG12103"
        var apiURL = ""
        var gameURL = ""
        var policyURL = ""
        var apiResponse = ""
        var userConsent = false

    }


    override fun onCreate() {
        super.onCreate()
        GlobalData = this
        FirebaseApp.initializeApp(GlobalData)
        appconfig()
    }

    private fun appconfig() {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
    }

    @Synchronized
    private fun getInstance(): GlobalData? {
        return GlobalData() as GlobalData
    }
}