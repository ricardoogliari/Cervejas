package rogliari.pessoal.projeto.com.cervejas.retrofit

import retrofit2.Call
import retrofit2.http.GET
import rogliari.pessoal.projeto.com.cervejas.models.Beer

/**
 * Created by ricardoogliari on 1/31/18.
 */
interface BeerService {

    @GET("beers?brewed_before=11-2012&abv_gt=6")
    fun list() : Call<List<Beer>>

}