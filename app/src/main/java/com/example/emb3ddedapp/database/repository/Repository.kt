package com.example.emb3ddedapp.database.repository

import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Repository : DataRepository {

    private val auth = FirebaseAuth.getInstance()
    private val dbFireStore = FirebaseFirestore.getInstance()

    private var users: CollectionReference = dbFireStore.collection("users")
    //private var tasks: CollectionReference = dbFireStore.collection("tasks")
    private val fireFileStorage = FirebaseStorage.getInstance()

    init {
        //TASKS = tasks
    }
/*
    override fun initDatabase() {
        CurrUser.id = auth.currentUser?.uid.toString()
        CurrUser.email = auth.currentUser?.email.toString()
    }

    override fun logInEmail(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                initDatabase()
                //onSuccess()
                getCurrentUser({onSuccess()},{onFail(it)})
            }
            .addOnFailureListener {
                onFail(it.message.toString())
            }
    }

    override fun singUpEmail(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                if(!authResult.user!!.isEmailVerified)
                sendVerifyEmail(authResult.user!!,{onSuccess()},{onFail(it)})
                else {
                    initDatabase()
                    setUser({onSuccess()},{onFail(it)})
                }
            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseAuthUserCollisionException)
                {
                    if (exception.errorCode=="ERROR_EMAIL_ALREADY_IN_USE"){
                        linkEmailToGoogle(email,password, {onSuccess()}, {onFail(it)})
                    }
                }
                else onFail(exception.message.toString())
            }
    }

    override fun sendVerifyEmail(user: FirebaseUser, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        user.sendEmailVerification()
            .addOnSuccessListener {
                initDatabase()
                setUser({onSuccess()},{onFail(it)})
            }
            .addOnFailureListener { onFail(it.message.toString()) }
    }

    override fun linkEmailToGoogle(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser?.linkWithCredential(credential)
            ?.addOnSuccessListener {
                //auth.currentUser?.sendEmailVerification()
                   // ?.addOnSuccessListener {
                        initDatabase()
                        setUser({onSuccess()},{onFail(it)})
                   // }
                   // ?.addOnFailureListener { onFail(it.message.toString()) }
            }
            ?.addOnFailureListener { onFail(it.message.toString()) }
    }

    override fun signUpLogInGoogle(isSignUp: Boolean, token: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                initDatabase()
                if (isSignUp) setUser({onSuccess()}, {onFail(it)})
                else {
                    checkUserExists({
                        getCurrentUser({onSuccess()},{onFail(it)})
                    }, {onFail(it)})

                }
            }
            .addOnFailureListener { onFail(it.message.toString()) }
    }

    override fun checkUserExists(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        users.document(CurrUser.id).get().addOnSuccessListener {
            val id = it["id"] as String?
            //Log.e("tag_user", "$user")
            if (id==null){
                //"There is no such user in the database"
                onFail("There is no such user in the database")
            } else onSuccess()
        }.addOnFailureListener {
            onFail(it.message.toString())
        }
    }

    override fun getCurrentUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {
       // initDatabase()
            users.document(CurrUser.id).get()
                .addOnSuccessListener {
                    CurrUser.tokenMsg = it["tokenMsg"] as String
                    CurrUser.email = it["email"] as String
                    CurrUser.login = it["login"] as String
                    CurrUser.password = it["password"] as String
                    CurrUser.status = it["status"] as String
                    CurrUser.telNumber = it["telNumber"] as String
                    CurrUser.profileUrlPhoto = it["profileUrlPhoto"] as? String

                    onSuccess()
                }
                .addOnFailureListener {

                    onFail(it.message.toString())
                }
    }

    override fun setUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token->
                CurrUser.tokenMsg = token
                users.document(CurrUser.id).set(mapOf(
                    "id" to CurrUser.id,
                    "login" to CurrUser.login,
                    "telNumber" to CurrUser.telNumber,
                    "status" to CurrUser.status,
                    "tokenMsg" to CurrUser.tokenMsg,
                    "profileUrlPhoto" to CurrUser.profileUrlPhoto,
                    "email" to CurrUser.email,
                    "password" to CurrUser.password
                )).addOnSuccessListener { onSuccess() }
                  .addOnFailureListener { onFail(it.message.toString()) }
            }.addOnFailureListener { onFail(it.message.toString()) }
    }

    override fun editCurrentUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {

    }

    override fun signOut() {
        if (getInitUser()){
            setInitUser(false)
        }
    }*/

}