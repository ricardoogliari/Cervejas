package rogliari.pessoal.projeto.com.cervejas.extensions

import android.content.res.Resources

/**
 * Created by ricardoogliari on 2/1/18.
 */
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()