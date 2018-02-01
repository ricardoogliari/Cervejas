package rogliari.pessoal.projeto.com.cervejas.extensions

import android.widget.ImageView
import com.squareup.picasso.RequestCreator

/**
 * Created by ricardoogliari on 2/1/18.
 */
public fun ImageView.load(path: String, request: (RequestCreator) -> RequestCreator) {
    request(getContext().picasso.load(path)).into(this)
}