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
    var favorites : List<Beer> = ArrayList<Beer>()

    lateinit var choresFlowable : Flowable<List<Beer>>
    lateinit var mAdapter : BeersAdapter
    var page : Int = 1;
    val realm = Realm.getDefaultInstance()
    val realmResults = realm.where<Beer>().findAll()

    var connected  : Boolean = false

    var all : Boolean = true

    /*
    * Método da interface ObRefreshListener.
    * Incrementa a paginação e faz a busca por novas cervejas (cache ou requisição HTTTP).
    * */
    override fun onRefresh(direction: SwipyRefreshLayoutDirection?) {
        page++
        getBeers()
    }

    /*
    * Método da interface View.OnClickListener.
    * Será chamado no clique dos botões da barra de pesquisa:
    * Botão voltar a esquerda e fechar a direita.
    * */
    override fun onClick(view: View?) {
        showInitActionBar()
    }

    /*
    * Método da interface ClickInBeerListInterface.
    * Trata do clique em uma das cervejas da listagem
    * Chama a tela de detalhes passando por extras os dados da mesma.
    * */
    override fun click(beer: Beer) {
        val intent = Intent(this, BeerDetailActivity::class.java)
        intent.putExtra("idBeer", beer.id)
        startActivity(intent)
    }

    /*
    * O método busca as cervejas.
    * Se o usuário está conectado faz uma requisição HTTP através do Retrofit e Observable
    * Caso contrário, usa os dados salvos em cache com o Realm.
    * */
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
                saveBeersInBD(beers)
            }
        } else {
            beers = beers.plus(if (cache) data.subList(Math.min((page * 10) - 10, data.size), Math.min(page * 10, data.size)) else data)
            mAdapter.newSetOfData(beers)

            if (!cache) {
                saveBeersInBD(data)
            }
        }
        swipeRefreshLayout.isRefreshing = false
    }

    fun saveBeersInBD(data: List<Beer>){
        for (beer: Beer in data) {
            realm.beginTransaction()
            var tempBeer = realm.where<Beer>().equalTo("id", beer.id.toInt()).findFirst()
            if (tempBeer != null) {
                beer.favorite = tempBeer.favorite
            }
            realm.copyToRealmOrUpdate(beer)
            realm.commitTransaction()
        }
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

        /*
        * Uso do RxKotlin para, através do padrão Observable, identificar a edição do campo de pesquisa e
        * refletir na listagem das cervejas
        * */
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
            if (text.length > 0){
                var tempList = ArrayList<Beer>()

                for (beer: Beer in (if (all) beers else favorites)) {
                    if (beer.name.contains(text) || beer.tagline.contains(text)) tempList.add(beer)
                }

                mAdapter.newSetOfData(tempList)
            }
        })
    }

    /*
    * Sobrecarrega o comportamento de pressionamento do botão virtual de Back.
    * O teste lógico verifica se a toolbar visible é a da pesquisa. Neste caso a Activity não é fechada. Apenas
    * a toolbar padrão é mostrada novamente.
    * */
    override fun onBackPressed() {
        if (toolbarSearch.visibility == View.VISIBLE){
            showInitActionBar()
        } else {
            super.onBackPressed()
        }
    }

    /*
    * Existem três momentos em que a Toolbar padrão deve ser mostrada: quando o botão de boltar do topo for clicado,
    * quando o botão de fechar a barra de pesquisa for clicado, quando o botão de voltar na parte inferior da tela for clicada
    * Em todos os três casos esse método será chado.
    * O mais importante aqui é que o adapter recebe a listagem original das cervejas para mostrar
    * */
    fun showInitActionBar(){
        toolbarDefault.visibility = View.VISIBLE
        toolbarSearch.visibility = View.GONE
        edtSearch.setText("")
        mAdapter.newSetOfData(beers)
        swipeRefreshLayout.isEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /*
    * Quando o action item da lupa for clicado, altera a toolbar para mostra a caixa de pesquisa
    * */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.miFind) {
            toolbarDefault.visibility = View.GONE
            toolbarSearch.visibility = View.VISIBLE
            swipeRefreshLayout.isEnabled = false
        } else if (item?.itemId == R.id.miAll) {
            //todos
            if (!all){
                //mudar para todos
                all = true;
                mAdapter.newSetOfData(beers)
                swipeRefreshLayout.isEnabled = true
            }
        } else {
            //favoritos
            if (all){
                //mudar para favoritos
                swipeRefreshLayout.isEnabled = false
                all = false
                favorites = realm.where<Beer>().equalTo("favorite", true).findAll()
                mAdapter.newSetOfData(favorites)
            }
        }

        return true
    }
}
