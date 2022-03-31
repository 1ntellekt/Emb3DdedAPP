package com.example.emb3ddedapp.database.repository

import android.util.Log
import com.example.emb3ddedapp.database.api.RetrofitInstance
import com.example.emb3ddedapp.models.*
import com.example.emb3ddedapp.utils.*
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository : DataRepository {

    private val auth = FirebaseAuth.getInstance()



    override fun logInEmail(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
               // initDatabase()
                //onSuccess()
                loginUser(auth.uid!!,password,{onSuccess()},{onFail(it)})
            }
            .addOnFailureListener {
                onFail(it.message.toString())
            }
    }

    override fun singUpEmail(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                if(!authResult.user!!.isEmailVerified)
                sendVerifyEmail(authResult.user!!,password,{onSuccess()},{onFail(it)})
                else {
                    //initDatabase()
                    registerUser(authResult.user!!.uid,password,{onSuccess()},{onFail(it)})
                }
            }
            .addOnFailureListener { exception ->
                    /*                if (exception is FirebaseAuthUserCollisionException)
                {
                    if (exception.errorCode=="ERROR_EMAIL_ALREADY_IN_USE"){
                        linkEmailToGoogle(email,password, {onSuccess()}, {onFail(it)})
                    }
                }
                else */
                    onFail(exception.message.toString())
            }
    }

    override fun sendVerifyEmail(user: FirebaseUser, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        user.sendEmailVerification()
            .addOnSuccessListener {
                //initDatabase()
                registerUser(user.uid,password,{onSuccess()},{onFail(it)})
            }
            .addOnFailureListener { onFail(it.message.toString()) }
    }

/*    override fun linkEmailToGoogle(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser?.linkWithCredential(credential)
            ?.addOnSuccessListener {
                //auth.currentUser?.sendEmailVerification()
                   // ?.addOnSuccessListener {
                        //initDatabase()
                        //setUser({onSuccess()},{onFail(it)})
                   // }
                   // ?.addOnFailureListener { onFail(it.message.toString()) }
            }
            ?.addOnFailureListener { onFail(it.message.toString()) }
    }*/

/*    override fun signUpLogInGoogle(isSignUp: Boolean, token: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                //initDatabase()
                if (isSignUp) {
                    //setUser({onSuccess()}, {onFail(it)})
                }
                else {

                }
            }
            .addOnFailureListener { onFail(it.message.toString()) }
    }*/

    override fun loginUser(uid:String,password: String,onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.login(uid,password).enqueue(object : Callback<UserAuthResponse> {
            override fun onResponse(call: Call<UserAuthResponse>, response: Response<UserAuthResponse>) {
                if (response.isSuccessful){
                    response.body()?.let { body->
                        setAccessToken(body.token)
                        setInitUserId(body.user.id)
                        body.user.apply {
                            CurrUser.number = number
                            CurrUser.id = id
                            CurrUser.email = email
                            CurrUser.login
                            CurrUser.status = status
                            CurrUser.uid = uid
                            CurrUser.url_profile = url_profile
                        }
                        if (!getInitTokenDevice()) addDevice(body.user.id,android.os.Build.MODEL,{onSuccess()},{onFail(it)})
                        else onSuccess()
                    }
                } else  {
                    Log.i("tagAPI","Error login: ${response.code()}")
                    onFail("Error login: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserAuthResponse>, t: Throwable) {
                Log.i("tagAPI","Error login: ${t.message}")
                onFail("Error login: ${t.message}")
            }
        })
    }

    override fun registerUser(uid: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        CurrUser.uid = uid
        RetrofitInstance.api.register(
            User(id = 0, login = CurrUser.login, email = CurrUser.email, uid = uid, number = CurrUser.number, status = "", url_profile = null),
            password
        ).enqueue(object : Callback<UserAuthResponse> {
            override fun onResponse(call: Call<UserAuthResponse>, response: Response<UserAuthResponse>) {
                if (response.isSuccessful){
                        response.body()?.let { body->
                            setAccessToken(body.token)
                            setInitUserId(body.user.id)
                            CurrUser.status = body.user.status
                            CurrUser.id = body.user.id
                            addDevice(body.user.id,android.os.Build.MODEL,{onSuccess()},{onFail(it)})
                        }
                } else  {
                    auth.currentUser!!.delete()
                    Log.i("tagAPI","Error register: ${response.code()}")
                    onFail("Error register: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserAuthResponse>, t: Throwable) {
                auth.currentUser!!.delete()
                Log.i("tagAPI","Error register: ${t.message}")
                onFail("Error register: ${t.message}")
            }
        })
    }



    override fun editCurrentUser(oldPassword: String?, password: String?, user: User?, onSuccess: () -> Unit, onFail: (String) -> Unit) {

    }

    override fun addDevice(user_id: Int, nameDevice: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
            FirebaseMessaging.getInstance().token
            .addOnSuccessListener {
                RetrofitInstance.api.addDevice(Device(name_device =nameDevice, user_id = user_id, token = it ),"Bearer ${getTokenAccess()}")
                    .enqueue(object : Callback<DeviceDefaultResponse>{
                        override fun onResponse(call: Call<DeviceDefaultResponse>, response: Response<DeviceDefaultResponse>) {
                            if (response.isSuccessful){
                                response.body()?.let { body->
                                    setInitTokenDevice(true)
                                    onSuccess()
                                }
                            } else {
                                Log.i("tagAPI","Error add device: ${response.code()}")
                                onFail("Error add device: ${response.code()}")
                            }
                        }
                        override fun onFailure(call: Call<DeviceDefaultResponse>, t: Throwable) {
                            Log.i("tagAPI","Error add device: ${t.message.toString()}")
                            onFail("Error add device: ${t.message.toString()}")
                        }
                    })
            }
            .addOnFailureListener {
                onFail("Error generated token: ${it.message.toString()}")
                Log.e("tagDevice","Error generated token: ${it.message.toString()}")
            }
    }

    override fun deleteDevice(token: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {

    }


    override fun signOut() {

    }

}