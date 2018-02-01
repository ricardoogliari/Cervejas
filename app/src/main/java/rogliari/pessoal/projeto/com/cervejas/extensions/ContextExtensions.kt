package rogliari.pessoal.projeto.com.cervejas.extensions

import android.content.Context
import com.squareup.picasso.Picasso

/**
 * Created by ricardoogliari on 2/1/18.
 */
public val Context.picasso: Picasso
    get() = Picasso.with(this)