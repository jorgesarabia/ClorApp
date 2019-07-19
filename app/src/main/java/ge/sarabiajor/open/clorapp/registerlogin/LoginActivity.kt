package ge.sarabiajor.open.clorapp.registerlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ge.sarabiajor.open.clorapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Escondo la barra de titulo para el Login:
        supportActionBar?.hide()
        button_login.setOnClickListener {
            performLogin()
        }

        textview_crearcuenta_login.setOnClickListener {
            Log.d(TAG,"Ir al Register")
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(){
        val message = "Se va loguear usando:" + edittext_email_login.text.toString() +
                " y " + edittext_password_login.text.toString()

        Log.d(TAG,message)
    }
}
