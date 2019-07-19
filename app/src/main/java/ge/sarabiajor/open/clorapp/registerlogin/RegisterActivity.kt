package ge.sarabiajor.open.clorapp.registerlogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }

        register_progressbar.visibility = View.INVISIBLE
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

        if(pass.length < 6){
            toast("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if(!pass.equals(retype)){
            toast("Las contraseñas no coinciden")
            edittext_password_register_2.setTextColor(Color.parseColor("#FF0000"))
            edittext_password_register.setTextColor(Color.parseColor("#FF0000"))
            return
        }

        register_progressbar.visibility = View.VISIBLE

        //Crear un usuario con Firebase:
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("RegisterActivity", "Successfully created user")
                //Hacemos tareas relacionadas al nuevo usuario creado:
                //uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                var message = ""
                Log.e(TAG,"Fallo la creacion: ${it.message}")
                if(it.message.toString().contains("The email address is badly formatted.")){
                    edittext_email_register.setTextColor(Color.parseColor("#FF0000"))
                    message += "El email está mal formado"
                }
                toast("Fallo a crear el usuario: $message")
            }
            .continueWith {
                register_progressbar.visibility = View.INVISIBLE
            }

        Log.d(TAG, "user: " + email + " pass: "+pass)
    }
}
