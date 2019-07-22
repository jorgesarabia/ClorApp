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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import ge.sarabiajor.open.clorapp.MainActivity
import ge.sarabiajor.open.clorapp.R
import ge.sarabiajor.open.clorapp.models.User
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*


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
        edittext_email_register.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                scrollview_register.smoothScrollTo(0,150)
            }
        }
        edittext_password_register.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                scrollview_register.smoothScrollTo(0,250)
            }
        }
        edittext_password_register_2.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                scrollview_register.smoothScrollTo(0,350)
            }
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

        if(pass.length < 6){
            toast("La contraseña debe tener al menos 6 caracteres")
            return
        }

        if(!pass.equals(retype)){
            toast("Las contraseñas no coinciden")
            edittext_password_register_2.text.clear()
            edittext_password_register.text.clear()
            return
        }

        register_progressbar.visibility = View.VISIBLE

        //Crear un usuario con Firebase:
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("RegisterActivity", "Successfully created user")
                if(selectedPhotoUri == null) {
                    val r = FirebaseStorage.getInstance().getReference("/logo")
                    r.child("icon512x512.png").downloadUrl.addOnSuccessListener {
                        Log.d(TAG,"Se pudo conectar: ${it}")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }else
                    uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.e(TAG,"Fallo la creacion: ${it.message}")
                var message = when(it.message.toString()){
                    "The email address is badly formatted."-> "El email está mal formado."
                    "The email address is already in use by another account."-> "El email ya está en uso."
                    else -> it.message.toString()
                }
                if(message.contains("El email")){
                    edittext_email_register.setTextColor(Color.parseColor("#FF0000"))
                }
                toast("Fallo a crear el usuario: $message")
                register_progressbar.visibility = View.INVISIBLE
            }

        Log.d(TAG, "user: " + email + " pass: "+pass)
    }

    private fun uploadImageToFirebaseStorage(){
        if( selectedPhotoUri == null )return
        val filename = UUID.randomUUID().toString()
        //Obtenemos la instancia del singleton de FirebaseStorage
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(TAG, "Imagen subida de manera exitosa: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d(TAG,"file Location: $it")
                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                //Do some login stuff
                Log.e(TAG,"No se pudo subir la imagen al Storage: ${it.message}")
                register_progressbar.visibility = View.INVISIBLE
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        //Se usa el elvis operator, para setear en caso de que sea null
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
            uid,
            username_edittext_register.text.toString(),
            profileImageUrl
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG,"Finalmente se guarda el usuario en Firebase Database")
                //Aca tengo que ir para el 'Home'
                val intent = Intent(this, MainActivity::class.java)
                /*Con estas banderas se limpia la cola de activities y el que se abre es el primero en la lista*/
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG,"No se pudo guardar en Firebase Database: ${it.message}")
                register_progressbar.visibility = View.INVISIBLE
            }
    }

}
