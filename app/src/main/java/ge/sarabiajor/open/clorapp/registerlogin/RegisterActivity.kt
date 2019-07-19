package ge.sarabiajor.open.clorapp.registerlogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
            /*Estas banderas ponen al activity que se va abrir como root:
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            */
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP//Borra todas las activities arriba de la que se va abrir
            startActivity(intent)
        }

        select_photo_register.setOnClickListener {
            /** Para seleccionar una foto de la galeria
             * se necesita un startActivityForResult()
             * Con un intent, que infla la galeria*/
            Log.d(TAG,"Try to show photo selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //Obtenemos la localización de la imagen:
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selected_photo_imageview_register.setImageBitmap(bitmap)

            //Hacemos que el boton sea invisible
            select_photo_register.alpha = 0f
        }
    }

    private fun performRegister(){
        val pass = edittext_password_register.text.toString()
        val retype = edittext_password_register_2.text.toString()
        val email = edittext_email_register.text.toString()
        val username = username_edittext_register.text.toString()

        if(email.isEmpty() || pass.isEmpty() || username.isEmpty()){
            toast("Debe ingresar email, username y contraseña")
            return
        }

        if(!pass.equals(retype)){
            toast("Las contraseñas no coinciden")
            edittext_password_register_2.text.clear()
            edittext_password_register.text.clear()
        }

        Log.d(TAG, "user: " + email + " pass: "+pass)
    }
}
