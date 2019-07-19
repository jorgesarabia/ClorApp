package ge.sarabiajor.open.clorapp.registerlogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ge.sarabiajor.open.clorapp.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object{
        val TAG = "RegisterActivity"
    }

    //Extension functions
    fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //Escondo la barra de titulo para el Login:
        supportActionBar?.hide()

        button_register.setOnClickListener {
            performRegister()
        }

        textview_tengocuenta_register.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(){
        val pass = edittext_password_register.text.toString()
        val retype = edittext_password_register_2.text.toString()

        if(!pass.equals(retype)){
            toast("Las contraseñas no coinciden")
            edittext_password_register_2.text.clear()
        }

        val email = edittext_email_register.text.toString()

        Log.d(TAG, "user: " + email + " pass: "+pass)
    }
}
