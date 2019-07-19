package ge.sarabiajor.open.clorapp.models

//El Parcelable se agrega para poder pasar objetos entre Intents:
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val profileImageUrl: String): Parcelable{
    constructor(): this("","","")
}