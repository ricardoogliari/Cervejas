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
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.adapters.BeersAdapter
import rogliari.pessoal.projeto.com.cervejas.listeners.ClickInBeerListInterface
import rogliari.pessoal.projeto.com.cervejas.models.Beer
import rogliari.pessoal.projeto.com.cervejas.retrofit.RetrofitInitializer
import rogliari.pessoal.projeto.com.cervejas.util.Utilitario

class MainActivity : AppCompatActivity(), ClickInBeerListInterface, View.OnClickListener, SwipyRefreshLayout.OnRefreshListener {

    var beers : List<Beer> = ArrayList<Beer>()
    lateinit var choresFlowable : Flowable<List<Beer>>
    lateinit var mAdapter : BeersAdapter
    var page : Int = 1;
    val realm = Realm.getDefaultInstance()
    val realmResults = realm.where<Beer>().findAll()

    var connected  : Boolean = false

    override fun onRefresh(direction: SwipyRefreshLayoutDirection?) {
        page++
        getBeers()
    }

    override fun onClick(view: View?) {
        showInitActionBar()
    }

    override fun click(beer: Beer) {
        val intent = Intent(this, BeerDetailActivity::class.java)
        intent.putExtra("name", beer.name)
        intent.putExtra("tagline", beer.tagline)
        intent.putExtra("description", beer.description)
        intent.putExtra("image_url", beer.image_url)
        startActivity(intent)
    }

    private fun getBeers(){
        if (connected) {

            choresFlowable = RetrofitInitializer().beerService().list(page) //val makes reference final
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            choresFlowable.subscribe { response ->

                workWithData(response.toList(), false)
            }
        } else {
            workWithData(realmResults, true)
        }
    }

    fun  workWithData(data: List<Beer>, cache: Boolean){
        if (page == 1) {
            beers = if (cache) data.subList(0, 10) else data
            mAdapter = BeersAdapter(beers, this, this)
            recMainList.adapter = mAdapter

            if (!cache) {
                for (beer: Beer in beers) {
                    realm.beginTransaction()
                    realm.copyToRealmOrUpdate(beer)
                    realm.commitTransaction()
                }
            }
        } else {
            beers = beers.plus(if (cache) data.subList(Math.min((page * 10) - 10, data.size), Math.min(page * 10, data.size)) else data)
            mAdapter.newSetOfData(beers)

            if (!cache) {
                for (beer: Beer in data) {
                    realm.beginTransaction()
                    realm.copyToRealmOrUpdate(beer)
                    realm.commitTransaction()
                }
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarDefault)

        connected = Utilitario.isConnected(this)

        recMainList.setHasFixedSize(true)
        recMainList.layoutManager = LinearLayoutManager(this)

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))
        recMainList.addItemDecoration(itemDecoration)

        swipeRefreshLayout.isRefreshing = true;
        swipeRefreshLayout.setOnRefreshListener(this);

        getBeers();

        btnBack.setOnClickListener(this)
        btnClose.setOnClickListener(this)

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
