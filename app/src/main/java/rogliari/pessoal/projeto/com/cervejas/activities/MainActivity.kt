package rogliari.pessoal.projeto.com.cervejas.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.adapters.BeersAdapter
import rogliari.pessoal.projeto.com.cervejas.listeners.ClickInBeerListInterface
import rogliari.pessoal.projeto.com.cervejas.models.Beer
import rogliari.pessoal.projeto.com.cervejas.retrofit.RetrofitInitializer

class MainActivity : AppCompatActivity(), ClickInBeerListInterface, Callback<List<Beer>> {

    override fun onFailure(call: Call<List<Beer>>?, t: Throwable?) {

    }

    override fun onResponse(call: Call<List<Beer>>?, response: Response<List<Beer>>?) {
        response?.body()?.let {
            val beers : List<Beer> = it
            val mAdapter = BeersAdapter(beers, this, this)
            recMainList.adapter = mAdapter
        }
    }

    override fun click(beer: Beer) {
        val intent = Intent(this, BeerDetailActivity::class.java)
        intent.putExtra("beer", beer)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recMainList.setHasFixedSize(true)

        recMainList.layoutManager = LinearLayoutManager(this)

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))
        recMainList.addItemDecoration(itemDecoration)

        val call = RetrofitInitializer().beerService().list()
        call.enqueue(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
