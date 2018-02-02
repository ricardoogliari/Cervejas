package rogliari.pessoal.projeto.com.cervejas.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_beer_list.view.*
import rogliari.pessoal.projeto.com.cervejas.R
import rogliari.pessoal.projeto.com.cervejas.extensions.load
import rogliari.pessoal.projeto.com.cervejas.extensions.px
import rogliari.pessoal.projeto.com.cervejas.listeners.ClickInBeerListInterface
import rogliari.pessoal.projeto.com.cervejas.models.Beer
import rogliari.pessoal.projeto.com.cervejas.util.Utilitario

/**
 * Created by ricardoogliari on 1/31/18.
 */
class BeersAdapter(
        private var beers: List<Beer>,
        private val context: Context,
        private val listener: ClickInBeerListInterface): Adapter<BeersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name = itemView.txtItemBeerName
        var tagline = itemView.txtItemBeerTagline
        var photo = itemView.imgItemBeerThumb
    }

    fun newSetOfData(newBeers : List<Beer>){
        beers = newBeers
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val beer = beers[position]
        holder?.let {
            it.name.text = beer.name
            it.tagline.text = beer.tagline

            if (Utilitario.isConnected(context)) {
                it.photo.load(beer.image_url) { request -> request.resize(70.px, 90.px).centerInside() }
            } else {
                it.photo.setImageResource(R.drawable.ic_wifi_off)
            }

            holder.itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    listener.click(beers[position])
                }
            })
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_beer_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return beers.size
    }

}