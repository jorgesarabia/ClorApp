package ge.sarabiajor.open.clorapp.registerlogin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ge.sarabiajor.open.clorapp.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Escondo la barra de titulo para el Login:
        supportActionBar?.hide()
    }
}
