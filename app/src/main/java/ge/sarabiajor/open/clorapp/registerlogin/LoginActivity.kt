package ge.sarabiajor.open.clorapp.registerlogin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import ge.sarabiajor.open.clorapp.MainActivity
import ge.sarabiajor.open.clorapp.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "LoginActivity"
    }

    //Extension functions
    fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

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
        edittext_password_login.setOnFocusChangeListener { v, hasFocus ->
            Log.d(TAG,hasFocus.toString())
            if(hasFocus){
                scrollview_login.smoothScrollTo(0,200)
            }
        }
    }

    private fun performLogin(){
        val email = edittext_email_login.text.toString()
        val pass = edittext_password_login.text.toString()

        if(email.isEmpty() || pass.isEmpty()){
            toast("Debe proveer un email y una contraseña.")
            return
        }

        val ref = FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    toast("Logueado exitosamente")
                    val intent = Intent(this,MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }else{
                    val exept = it.exception?.message.toString()
                    Log.e(TAG,"Falla al iniciar sesión: $exept")
                    val message = when {
                        //The email address is badly formatted.
                        exept.contains("email address is badly formatted.")->"El email está mal formateado"
                        //There is no user record corresponding to this identifier. The user may have been deleted
                        exept.contains("no user record corresponding to")->"email/password no se corresponden"
                        //The password is invalid or the user does not have a password.
                        exept.contains("password is invalid or the user")->"email/password no se corresponden"
                        else->""
                    }

                    toast("Error al iniciar sesión. $message")
                }

            }

        val message = "Se va loguear usando:" + edittext_email_login.text.toString() +
                " y " + edittext_password_login.text.toString()

        Log.d(TAG,message)
    }


}
