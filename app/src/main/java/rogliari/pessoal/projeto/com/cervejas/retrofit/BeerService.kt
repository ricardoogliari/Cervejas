package rogliari.pessoal.projeto.com.cervejas.retrofit

import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query
import rogliari.pessoal.projeto.com.cervejas.models.Beer

/**
 * Created by ricardoogliari on 1/31/18.
 * Interface que define o comportamento da requisição HTTP para o serviço das cervejas
 */
interface BeerService {

    /*
    Requisição com o método GET e com um query parâmetro para definir a página a ser buscada
    O uso de Flowable é referente ao Observable da requisição HTTP
    */
    @GET("beers?per_page=10")
    fun list(@Query("page") page : Int) : Flowable<List<Beer>>

}