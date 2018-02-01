package rogliari.pessoal.projeto.com.cervejas.retrofit

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by ricardoogliari on 1/31/18.
 */
class RetrofitInitializer {


    private val retrofit = Retrofit.Builder()
            .baseUrl("https://api.punkapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

    fun beerService() = retrofit.create(BeerService::class.java)

}