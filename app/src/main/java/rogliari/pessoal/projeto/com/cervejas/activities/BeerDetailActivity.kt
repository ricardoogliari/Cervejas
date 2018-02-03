package rogliari.pessoal.projeto.com.cervejas.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.vicpin.krealmextensions.save
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_beer_detail.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.extensions.load
import rogliari.pessoal.projeto.com.cervejas.extensions.px
import rogliari.pessoal.projeto.com.cervejas.models.Beer
import rogliari.pessoal.projeto.com.cervejas.util.Utilitario

/*
* Tela de detalhe de uma cerveja
* */
class BeerDetailActivity : AppCompatActivity() {

    var miFavorite : MenuItem? = null
    var miNoFavorite : MenuItem? = null

    val realm = Realm.getDefaultInstance()
    var beer : Beer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_detail)

        realm.beginTransaction()

        /*
        * Recebe pelo extra da Intent apenas o id da cerveja. Através de uma busca no banco de dados recupera os outros dados.
        * */
        beer = realm.where<Beer>().equalTo("id", intent.getIntExtra("idBeer", 0).toInt()).findFirst()
        realm.commitTransaction()

        beer?.let {
            txtDetailName.text = it.name
            txtDetailTagline.text = it.tagline
            txtDetailDescription.text = it.description

            /*
            * Se o dispositivo estiver conectado busca a imagem da internet, senão usa a imagem padrão, indicando a ausência de conexão
            * */
            if (Utilitario.isConnected(this)) {
                actDetailThumb.load(it.image_url) { request -> request.resize(120.px, 150.px).centerInside() }
            } else {
                actDetailThumb.setImageResource(R.drawable.ic_wifi_off)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)

        miFavorite = menu?.findItem(R.id.miFavorite)
        miNoFavorite = menu?.findItem(R.id.miNoFavorite)

        holdFavorite(beer?.favorite!!)

        return true;
    }

    fun holdFavorite(favorite: Boolean){
        miFavorite?.setVisible(favorite)
        miNoFavorite?.setVisible(!favorite)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.miFavorite) {
            holdFavorite(false)
        } else {
            holdFavorite(true)
        }

        beer?.let {
            Log.e("PASSO", "passo 1")
            realm.executeTransaction { realm ->
                Log.e("PASSO", "passo 2")
                it.favorite = !(item?.itemId == R.id.miFavorite);
                Log.e("PASSO", "passo 3: " + (!(item?.itemId == R.id.miFavorite)))
                realm.copyToRealmOrUpdate(it);
                Log.e("PASSO", "passo 4")
            }
        }

        return true;
    }

}
