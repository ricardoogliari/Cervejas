package rogliari.pessoal.projeto.com.cervejas.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ricardoogliari on 1/31/18.
 */
class RetrofitInitializer {

    fun init() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.punkapi.com/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

}