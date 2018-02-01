package rogliari.pessoal.projeto.com.cervejas.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_beer_detail.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.models.Beer

class BeerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_detail)

        val beer : Beer = intent.getSerializableExtra("beer") as Beer
        txtDetailName.text = beer.name
        txtDetailTagline.text = beer.tagline
        txtDetailDescription.text = beer.description
    }

}
