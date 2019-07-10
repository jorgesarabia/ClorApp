package ge.sarabiajor.open.clorapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    //Constantes
    private val RC_SIGN_IN = 1

    //extension functions:
    fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        if (auth.getCurrentUser() != null) {
            toast("Ya está logueado")
        } else {
            //No está logueado, se tiene que pasar al activity:

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        toast("HOLA")
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                toast("Se logueó")
            } else if (resultCode == RESULT_CANCELED) {
                toast("No se logueó")
                finish()
            }
        }
    }


}
