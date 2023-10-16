package pgo.roulettepro.tablegames.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.json.JSONObject
import pgo.roulettepro.tablegames.GlobalData
import pgo.roulettepro.tablegames.R
import pgo.roulettepro.tablegames.api.ApiResponse
import pgo.roulettepro.tablegames.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoadingScreen : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 201234

    private lateinit var sharedPref : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_screen)

        sharedPref = getSharedPreferences(GlobalData.appCode, MODE_PRIVATE)

        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task: Task<Boolean?> ->
                if (task.isSuccessful) {
                    Log.d("Firebase Remote Config", "Connected")
                    GlobalData.apiURL = mFirebaseRemoteConfig.getString("apiURL")
                    Log.d("apiURL", GlobalData.apiURL)

                }

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://5gbapps.site/") // Base API URL
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val apiService = retrofit.create(ApiService::class.java)

                val endPoint = "${GlobalData.apiURL}?appid=${GlobalData.appCode}"
                Log.d("Config", "endpoint: $endPoint")

                val requestBody = JSONObject().apply {
                    put("appid", GlobalData.appCode)
                }
                val call = apiService.getData(endPoint, requestBody)

                call.enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            val responseData = response.body()

                            if (responseData != null) {
                                GlobalData.gameURL = responseData.gameURL
                                GlobalData.policyURL = responseData.policyURL
                                GlobalData.apiResponse = responseData.status
                                Log.d("gameURL", GlobalData.gameURL)
                                Log.d("policyURL", GlobalData.policyURL)

                                if (!permissionChecker()){
                                    requestPermission()
                                }else openActivity()


                            } else {
                                Log.e("apiResponse", "Response not successful")
                            }

                        } else {
                            Log.e("apiResponse", "Response is not successful")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.e("apiResponse", "Request failed: " + t.message)
                    }
                })

                /*val requestQueue = Volley.newRequestQueue(this)
                val url: String = GlobalData.apiURL + "?request&appid=" + GlobalData.appCode

                Log.d("url", url)
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    { response: String ->
                        Log.d("response", response)
                        val parseValues = Gson()
                        val jsonObject =
                            parseValues.fromJson(response, JsonObject::class.java)
                        GlobalData.gameURL = jsonObject["gameURL"].asString
                        GlobalData.policyURL = jsonObject["policyURL"].asString
                        Log.d("gameURL", GlobalData.gameURL)
                        Log.d("policyURL", GlobalData.policyURL)

                        val gameIntent = Intent(this, PolicyConsent::class.java)
                        startActivity(gameIntent)

                    }
                ) { error: VolleyError? -> }
                requestQueue.add(stringRequest)*/
            }
    }

    private fun permissionChecker(): Boolean {
        val location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        val camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val media =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        return location == PackageManager.PERMISSION_GRANTED
                && camera == PackageManager.PERMISSION_GRANTED
                && media == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )
            } else {
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }

        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val locationGranted = grantResults.getOrNull(0) == PackageManager.PERMISSION_GRANTED
            val cameraGranted = grantResults.getOrNull(1) == PackageManager.PERMISSION_GRANTED
            val mediaGranted = grantResults.getOrNull(2) == PackageManager.PERMISSION_GRANTED

            sharedPref.edit {
                putBoolean("locationGranted", locationGranted)
                putBoolean("cameraGranted", cameraGranted)
                putBoolean("mediaGranted", mediaGranted)
                putBoolean("runOnce", locationGranted && cameraGranted && mediaGranted)
                apply()
            }
        }

        openActivity()
    }

    private fun openActivity() {
        GlobalData.userConsent = sharedPref.getBoolean("permitSendData",true)
        if(!sharedPref.getBoolean("permitSendData", false)){
            val policyIntent = Intent(this, PolicyConsent::class.java)
            policyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(policyIntent)
            finish()
        }else{
            val gameIntent = Intent(this, MainActivity::class.java)
            gameIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(gameIntent)
            finish()
        }

    }
}