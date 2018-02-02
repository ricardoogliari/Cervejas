package rogliari.pessoal.projeto.com.cervejas.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

/**
 * Created by ricardoogliari on 1/31/18.
 */
open class Beer (@PrimaryKey var name: String,
                  var tagline: String,
                  var description: String,
                  var image_url: String) : Serializable, RealmObject() {

    constructor() : this("", "", "", ""){

    }

}