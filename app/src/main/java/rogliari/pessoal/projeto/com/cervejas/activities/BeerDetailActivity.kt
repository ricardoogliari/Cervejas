package rogliari.pessoal.projeto.com.cervejas.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_beer_detail.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.extensions.load
import rogliari.pessoal.projeto.com.cervejas.extensions.px
import rogliari.pessoal.projeto.com.cervejas.util.Utilitario

/*
* Tela de detalhe de uma cerveja
* */
class BeerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_detail)

        /*
        * Recebe por parâmetro da Intent os valores relacionados a cerveja.
        * Isso foi preciso por problemas do Realm em relação a junção de Serializable e RealmObject
        * */
        txtDetailName.text = intent.getStringExtra("name")
        txtDetailTagline.text = intent.getStringExtra("tagline")
        txtDetailDescription.text = intent.getStringExtra("description")

        /*
        * Se o dispositivo estiver conectado busca a imagem da internet, senão usa a imagem padrão, indicando a ausência de conexão
        * */
        if (Utilitario.isConnected(this)) {
            actDetailThumb.load(intent.getStringExtra("image_url")) { request -> request.resize(120.px, 150.px).centerInside() }
        } else {
            actDetailThumb.setImageResource(R.drawable.ic_wifi_off)
        }


    }

}
