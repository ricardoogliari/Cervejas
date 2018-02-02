package rogliari.pessoal.projeto.com.cervejas.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by ricardoogliari on 2/1/18.
 */
object Utilitario {

    /*
    Funcão que checa a conectividade da rede, utilizada para usar o cache de cervejas ou uma requisição HTTP
     */
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected

    }

}
