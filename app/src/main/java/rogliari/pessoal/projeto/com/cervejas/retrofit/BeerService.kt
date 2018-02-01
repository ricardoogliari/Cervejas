package rogliari.pessoal.projeto.com.cervejas.retrofit

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import rogliari.pessoal.projeto.com.cervejas.models.Beer

/**
 * Created by ricardoogliari on 1/31/18.
 */
interface BeerService {

    @GET("beers?page1&per_page=10")
    fun list() : Flowable<List<Beer>>

}