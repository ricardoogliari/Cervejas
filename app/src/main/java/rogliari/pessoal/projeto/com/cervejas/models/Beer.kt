package rogliari.pessoal.projeto.com.cervejas.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Created by ricardoogliari on 1/31/18.
 * Modelo de dado da cerveja que seja serializado na chamada HTTP pelo Retrofit
 */
open class Beer (@PrimaryKey var id: Int,
                  var name: String,
                  var tagline: String,
                  var description: String,
                  var image_url: String,
                  var favorite: Boolean) : RealmObject() {

    /*
    Construtor vazio é premissa do Realm API
    */
    constructor() : this(0, "", "", "", "", false){

    }

}