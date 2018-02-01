package rogliari.pessoal.projeto.com.cervejas.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.reactivestreams.Subscriber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.adapters.BeersAdapter
import rogliari.pessoal.projeto.com.cervejas.listeners.ClickInBeerListInterface
import rogliari.pessoal.projeto.com.cervejas.models.Beer
import rogliari.pessoal.projeto.com.cervejas.retrofit.RetrofitInitializer

class MainActivity : AppCompatActivity(), ClickInBeerListInterface {

    lateinit var beers : List<Beer>
    lateinit var choresFlowable : Flowable<List<Beer>>
    lateinit var mAdapter : BeersAdapter

    override fun click(beer: Beer) {
        val intent = Intent(this, BeerDetailActivity::class.java)
        intent.putExtra("beer", beer)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarDefault)

        recMainList.setHasFixedSize(true)

        recMainList.layoutManager = LinearLayoutManager(this)

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))
        recMainList.addItemDecoration(itemDecoration)

        choresFlowable = RetrofitInitializer().beerService().list() //val makes reference final
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        choresFlowable.subscribe { response ->
            beers = response.toList()
            mAdapter = BeersAdapter(beers, this, this)
            recMainList.adapter = mAdapter
        }

        btnBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showInitActionBar()
            }
        })

        btnClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showInitActionBar()
            }
        })

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = Unit
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = subscriber.onNext(s.toString())
            })
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            text ->
                Log.e("Output", "->>>> " + text)
            if (text.length > 0){
                var tempList = ArrayList<Beer>()

                for (beer: Beer in beers) {
                    if (beer.name.contains(text) || beer.tagline.contains(text)) tempList.add(beer)
                }

                mAdapter.newSetOfData(tempList)
            }
        })
    }

    override fun onBackPressed() {
        if (toolbarSearch.visibility == View.VISIBLE){
            showInitActionBar()
        } else {
            super.onBackPressed()
        }
    }

    fun showInitActionBar(){
        toolbarDefault.visibility = View.VISIBLE
        toolbarSearch.visibility = View.GONE
        edtSearch.setText("")
        mAdapter.newSetOfData(beers)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        toolbarDefault.visibility = View.GONE
        toolbarSearch.visibility = View.VISIBLE

        return true
    }
}
