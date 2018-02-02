package rogliari.pessoal.projeto.com.cervejas

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by ricardoogliari on 2/1/18.
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        var c = RealmConfiguration.Builder()
        c.name("student")
        c.deleteRealmIfMigrationNeeded()
        Realm.setDefaultConfiguration(c.build())
    }
}
