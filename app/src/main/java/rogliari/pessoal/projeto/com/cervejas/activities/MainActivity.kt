package rogliari.pessoal.projeto.com.cervejas.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import kotlinx.android.synthetic.main.activity_main.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.adapters.BeersAdapter
import rogliari.pessoal.projeto.com.cervejas.models.Beer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recMainList.setHasFixedSize(true)

        recMainList.layoutManager = LinearLayoutManager(this)

        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(resources.getDrawable(R.drawable.divider))
        recMainList.addItemDecoration(itemDecoration)

        //RequestService.getRacesByMotorista(this, object : RequestService.CallbackDefault {
          //  override fun onSuccess(result: JsonObject) {
            //    val container = Gson().fromJson<ContainerRaces>(result.toString(), ContainerRaces::class.java!!)

                // specify an adapter (see also next example)
        val beers = ArrayList<Beer>()
        beers.add(Beer("Skol", "Jesus, que ruim"))
        beers.add(Beer("Guaipeca", "Melhor cerveja que tomei nos últimos 15 anos, sensacional, recomendo"))
        beers.add(Beer("Braham Extra", "Também tem seu valor, fica entre as duas citadas acima"))
        val mAdapter = BeersAdapter(beers, this)
        recMainList.adapter = mAdapter
          //  }

       //     override fun onError() {

         //   }
        //}, auth.data.id)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
}
