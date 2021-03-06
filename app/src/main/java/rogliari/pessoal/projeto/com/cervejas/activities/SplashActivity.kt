package rogliari.pessoal.projeto.com.cervejas.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import rogliari.pessoal.projeto.com.cervejas.R
import java.util.*

/*
* Tela de entrada da aplicação. O Handler espera por 1 segundo e meio e chama a tela principal da aplicação. (Lista de cervejas)
* */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 1500)
    }

}
