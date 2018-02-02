package rogliari.pessoal.projeto.com.cervejas.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_beer_detail.*
import kotlinx.android.synthetic.main.item_beer_list.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.extensions.load
import rogliari.pessoal.projeto.com.cervejas.extensions.px
import rogliari.pessoal.projeto.com.cervejas.models.Beer
import rogliari.pessoal.projeto.com.cervejas.util.Utilitario

class BeerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_detail)

        txtDetailName.text = intent.getStringExtra("name")
        txtDetailTagline.text = intent.getStringExtra("tagline")
        txtDetailDescription.text = intent.getStringExtra("description")

        if (Utilitario.isConnected(this)) {
            actDetailThumb.load(intent.getStringExtra("image_url")) { request -> request.resize(120.px, 150.px).centerInside() }
        } else {
            actDetailThumb.setImageResource(R.drawable.ic_wifi_off)
        }


    }

}
