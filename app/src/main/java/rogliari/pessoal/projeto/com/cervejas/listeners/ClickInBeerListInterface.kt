package rogliari.pessoal.projeto.com.cervejas.listeners

import rogliari.pessoal.projeto.com.cervejas.models.Beer

/**
 * Created by ricardoogliari on 1/31/18.
 * Listener usado no adapter para avisar o RecyclerView do evento de clique
 */
interface ClickInBeerListInterface {

    fun click(beer: Beer)

}