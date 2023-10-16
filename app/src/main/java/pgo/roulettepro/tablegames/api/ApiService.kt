package pgo.roulettepro.tablegames.api

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    fun getData(@Url url: String, @Body requestBody: JSONObject): Call<ApiResponse>
}
