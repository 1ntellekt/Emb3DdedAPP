package com.example.emb3ddedapp.database.repository

import android.util.Log
import com.example.emb3ddedapp.database.api.RetrofitInstance
import com.example.emb3ddedapp.models.*
import com.example.emb3ddedapp.utils.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class Repository : DataRepository {

    private val auth = FirebaseAuth.getInstance()


    override fun logInEmail(email: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
               // initDatabase()
                //onSuccess()
                if (!auth.currentUser!!.isEmailVerified){
                    //REPOSITORY.logout({},{})
                    onFail("Verified email please!")
                } else if (getInitResetPassword(email)){
                    resetPassword(auth.uid!!,password,{
                       setInitResetPassword(email,false)
                       loginUser(auth.uid!!,password, {onSuccess()},{onFail(it)})
                    }, {onFail(it)})
                } else {
                    loginUser(auth.uid!!,password,{onSuccess()},{onFail(it)})
                }
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

    override fun resetPasswordEmail(email: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnSuccessListener {
                setInitResetPassword(email,true)
                onSuccess()
            }
            .addOnFailureListener { onFail(it.message.toString()) }
    }


    override fun resetPassword(uid: String, password: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.resetPassword(uid,password)
            .enqueue(object : Callback<StatusMsgResponse>{
                override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                    if (response.isSuccessful){
                        onSuccess()
                    } else {
                        Log.i("tagAPI","Error reset password: ${response.code()}")
                        onFail("Error reset password: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                    Log.i("tagAPI","Error reset password: ${t.message}")
                    onFail("Error reset password: ${t.message}")
                }
            })
    }


    override fun loginUser(uid:String,password: String,onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.login(LoginModel(uid,password)).enqueue(object : Callback<UserAuthResponse> {
            override fun onResponse(call: Call<UserAuthResponse>, response: Response<UserAuthResponse>) {
                if (response.isSuccessful){
                    response.body()?.let { body->
                        setAccessToken(body.token)
                        RetrofitInstance.setAuthorizationBearer(body.token)
                        setInitUserId(body.user.id)
                        body.user.apply {
                            CurrUser.number = number
                            CurrUser.id = id
                            CurrUser.email = email!!
                            CurrUser.login = login
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
                            //setAccessToken(body.token)
                            //RetrofitInstance.setAuthorizationBearer(body.token)
                            //setInitUserId(body.user.id)
                            CurrUser.status = body.user.status
                            CurrUser.id = body.user.id
                            //addDevice(body.user.id,android.os.Build.MODEL,{onSuccess()},{onFail(it)})
                            onSuccess()
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

    override fun getCurrUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.getUserById(getInitUserId())
            .enqueue(object : Callback<UserDefaultResponse>{
                override fun onResponse(call: Call<UserDefaultResponse>, response: Response<UserDefaultResponse>) {
                    if (response.isSuccessful){
                        response.body()?.let { body->
                            body.user.apply {
                                CurrUser.id = id
                                CurrUser.url_profile = url_profile
                                CurrUser.uid = uid!!
                                CurrUser.status = status
                                CurrUser.login = login
                                CurrUser.email = email!!
                                CurrUser.number = number
                            }
                            onSuccess()
                        }
                    } else {
                        Log.i("tagAPI","Error get user by id: ${response.code()}")
                        onFail("Error get user by id: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<UserDefaultResponse>, t: Throwable) {
                    Log.i("tagAPI","Error get user by id: ${t.message.toString()}")
                    onFail("Error get user by id: ${t.message.toString()}")
                }
            })
    }

    override fun editCurrentUser(oldPassword: String?, password: String?, user: User?, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.updateUser(CurrUser.id,user,oldPassword,password)
            .enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    onSuccess()
                }else {
                    Log.i("tagAPI","Error update user: ${response.code()}")
                    onFail("Error update user: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI","Error update user: ${t.message.toString()}")
                onFail("Error update user: ${t.message.toString()}")
            }
        })
    }


    override fun addDevice(user_id: Int, nameDevice: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
            FirebaseMessaging.getInstance().token
            .addOnSuccessListener { token ->
                RetrofitInstance.api.addDevice(Device(name_device =nameDevice, user_id = user_id, token = token ))
                    .enqueue(object : Callback<DeviceDefaultResponse>{
                        override fun onResponse(call: Call<DeviceDefaultResponse>, response: Response<DeviceDefaultResponse>) {
                            if (response.isSuccessful){
                                response.body()?.let {
                                    setInitTokenDevice(true)
                                    onSuccess()
                                }
                            } else {
                                setInitUserId(0)
                                setAccessToken(null)
                                logout({},{})
                                Log.i("tagAPI","Error add device: ${response.code()} ${response.headers()}")
                                onFail("Error add device: ${response.code()}")
                            }
                        }
                        override fun onFailure(call: Call<DeviceDefaultResponse>, t: Throwable) {
                            setInitUserId(0)
                            setAccessToken(null)
                            logout({},{})
                            Log.i("tagAPI","Error add device: ${t.message.toString()}")
                            onFail("Error add device: ${t.message.toString()}")
                        }
                    })
            }
            .addOnFailureListener {
                Log.e("tagDevice","Error generated token: ${it.message.toString()}")
                onFail("Error generated token: ${it.message.toString()}")
            }
    }

    override fun deleteDevice(token: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
       RetrofitInstance.api.deleteDeviceByToken(token)
                .enqueue(object : Callback<StatusMsgResponse>{
                    override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                        if (response.isSuccessful){
                            logout({onSuccess()},{onFail(it)})
                        } else {
                            Log.i("tagAPI","Error delete device: ${response.code()}")
                            onFail("Error delete device: ${response.code()}")
                        }
                    }
                    override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                        Log.i("tagAPI","Error delete device: ${t.message.toString()}")
                        onFail("Error delete device: ${t.message.toString()}")
                    }
        })
    }


    override fun logout(onSuccess: () -> Unit, onFail: (String) -> Unit){
        RetrofitInstance.api.logout()
            .enqueue(object : Callback<StatusMsgResponse>{
                override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                    if (response.isSuccessful){
         //               setInitResetPassword(auth.uid!!,null)
                        setInitTokenDevice(false)
                        setAccessToken(null)
                        setInitUserId(0)
                        onSuccess()
                    }else {
                        Log.i("tagAPI","Error logout user: ${response.code()}")
                        onFail("Error logout user: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                    Log.i("tagAPI","Error logout user: ${t.message.toString()}")
                    onFail("Error logout user: ${t.message.toString()}")
                }
            })
    }

    override fun signOut(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token->
                FirebaseMessaging.getInstance().deleteToken()
                deleteDevice(token,{onSuccess()}, {onFail(it)})
        }.addOnFailureListener {
            onFail("Error generated token: ${it.message.toString()}")
            Log.e("tagDevice","Error generated token: ${it.message.toString()}")
        }
    }

    //orders
    override fun addOrder(order: Order, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.addOrder(order).enqueue(object : Callback<OrderDefaultResponse>{
            override fun onResponse(call: Call<OrderDefaultResponse>, response: Response<OrderDefaultResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess()
                    }
                } else {
                    Log.i("tagAPI","Error add order: ${response.code()}")
                    onFail("Error add order: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<OrderDefaultResponse>, t: Throwable) {
                Log.i("tagAPI","Error add order: ${t.message.toString()}")
                onFail("Error add order: ${t.message.toString()}")
            }
        })
    }

    override fun updateOrder(id: Int, order: Order, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.updateOrder(id = id, order).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    onSuccess()
                } else {
                    Log.i("tagAPI","Error update order: ${response.code()}")
                    onFail("Error update order: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI","Error update order: ${t.message.toString()}")
                onFail("Error update order: ${t.message.toString()}")
            }
        })
    }

    override fun deleteOrder(id: Int, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.deleteOrder(id).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    onSuccess()
                } else {
                    Log.i("tagAPI","Error delete order: ${response.code()}")
                    onFail("Error delete order: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI","Error delete order: ${t.message.toString()}")
                onFail("Error delete order: ${t.message.toString()}")
            }
        })
    }

    override fun getOrdersByUserId(user_id: Int): Call<OrdersByUserResponse> {
        return  RetrofitInstance.api.getOrdersByUser(user_id = user_id)
    }

    override fun getOrderById(id: Int): Call<OrderDefaultResponse> {
        return RetrofitInstance.api.getOrderById(id)
    }

    override fun getAllOrders(): Call<OrdersByUserResponse> {
        return RetrofitInstance.api.getAllOrders()
    }

    //news
    override fun addNewsItem(newsItem: NewsItem, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.addNewsItem(newsItem).enqueue(object : Callback<NewsDefaultResponse>{
            override fun onResponse(call: Call<NewsDefaultResponse>, response: Response<NewsDefaultResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess()
                    }
                } else {
                    Log.i("tagAPI", "Error add news_item: ${response.code()}")
                    onFail("Error add news_item: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<NewsDefaultResponse>, t: Throwable) {
                Log.i("tagAPI", "Error add news_item: ${t.message}")
                onFail("Error add news_item: ${t.message}")
            }
        })
    }

    override fun updateNewsItem(id: Int, newsItem: NewsItem, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.updateNewsItem(id,newsItem).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess()
                    }
                } else {
                    Log.i("tagAPI", "Error update news_item: ${response.code()}")
                    onFail("Error update news_item: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI", "Error update news_item: ${t.message}")
                onFail("Error update news_item: ${t.message}")
            }
        })
    }

    override fun deleteNewsItem(id: Int, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.deleteNews(id).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess()
                    }
                } else {
                    Log.i("tagAPI", "Error delete news_item: ${response.code()}")
                    onFail("Error delete news_item: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI", "Error delete news_item: ${t.message}")
                onFail("Error delete news_item: ${t.message}")
            }
        })
    }

    override fun getNewsByUserId(user_id: Int): Call<NewsByUserResponse> {
        return RetrofitInstance.api.getNewsByUser(user_id)
    }

    override fun getNewsItemById(id: Int): Call<NewsDefaultResponse> {
        return RetrofitInstance.api.getNewsItemById(id)
    }

    override fun getAllNews(): Call<NewsByUserResponse> {
        return RetrofitInstance.api.getAllNews()
    }

    //chats
    override fun addNewChat(user_id_first: Int, user_id_second: Int, onSuccess: (Chat) -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.addChat(user_id_first, user_id_second).enqueue(object : Callback<ChatDefaultResponse>{
            override fun onResponse(call: Call<ChatDefaultResponse>, response: Response<ChatDefaultResponse>) {
                if (response.isSuccessful){
                    response.body()?.let { body->
                        onSuccess(body.chat)
                    }
                } else {
                    Log.i("tagAPI", "Error add chat: ${response.code()} ${response.message()}")
                    onFail("Error add chat: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<ChatDefaultResponse>, t: Throwable) {
                Log.i("tagAPI", "Error add chat: ${t.message}")
                onFail("Error add chat: ${t.message}")
            }
        })
    }

    override fun updateChat(id: Int, download_first: Int?, download_second: Int?, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.updateChat(id, download_first, download_second).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    onSuccess()
                } else {
                    Log.i("tagAPI", "Error update chat: ${response.code()}")
                    onFail("Error update chat: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI", "Error update chat: ${t.message}")
                onFail("Error update chat: ${t.message}")
            }
        })
    }

    override fun getChatsByUserId(user_id: Int): Call<ChatsByUserResponse> {
        return RetrofitInstance.api.getChatsByUser(user_id)
    }

    override fun getMessagesByChatId(id: Int): Call<ChatMessagesByChatResponse> {
        return RetrofitInstance.api.getChatMessagesById(id)
    }

    //messages
    override fun addMessage(msg: Message, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.addMessage(msg).enqueue(object : Callback<MessageDefaultResponse>{
            override fun onResponse(call: Call<MessageDefaultResponse>, response: Response<MessageDefaultResponse>) {
                if (response.isSuccessful){
                    onSuccess()
                } else {
                    Log.i("tagAPI", "Error add message: ${response.code()} ${response.message()}")
                    onFail("Error add message: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<MessageDefaultResponse>, t: Throwable) {
                Log.i("tagAPI", "Error add message: ${t.message}")
                onFail("Error add message: ${t.message}")
            }
        })
    }

    override fun editMessage(id: Int, msg: Message, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.updateMessage(id, msg).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                        onSuccess()
                } else {
                    Log.i("tagAPI", "Error edit message: ${response.code()} ${response.message()}")
                    onFail("Error edit message: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI", "Error edit message: ${t.message}")
                onFail("Error edit message: ${t.message}")
            }
        })
    }

    override fun deleteMessage(id: Int, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.deleteMessage(id).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                    onSuccess()
                } else {
                    Log.i("tagAPI", "Error delete message: ${response.code()} ${response.message()}")
                    onFail("Error delete message: ${response.code()} ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI", "Error delete message: ${t.message}")
                onFail("Error delete message: ${t.message}")
            }
        })
    }


    //files
    override fun uploadFile(file: File, fileNameDateForm:String ,onSuccess: (String) -> Unit, onFail: (String) -> Unit) {

        val requestBody = if (file.name.endsWith(".jpg") || file.name.endsWith(".png") || file.name.endsWith(".jpeg")){
            file.asRequestBody("image/*".toMediaType())
        } else {
            file.asRequestBody("multipart/form-data".toMediaType())
        }
        val body: MultipartBody.Part = MultipartBody.Part.createFormData(fileNameDateForm, file.name, requestBody)
        RetrofitInstance.api.uploadFile(body).enqueue(object : Callback<StatusMsgPath> {
            override fun onResponse(call: Call<StatusMsgPath>, response: Response<StatusMsgPath>) {
                if (response.isSuccessful){
                    response.body()?.let {
                        onSuccess(it.path)
                    }
                } else {
                    Log.i("tagAPI", "Error upload file: ${response.code()}")
                    onFail("Error upload file: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgPath>, t: Throwable) {
                Log.i("tagAPI", "Error upload file: ${t.message}")
                onFail("Error upload file: ${t.message}")
            }
        })
    }

    override fun reAuthenticate(email: String,password: String, oldPassword: String, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        auth.currentUser!!.reauthenticate(credential).addOnSuccessListener {
            auth.currentUser!!.updatePassword(password)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { onFail(it.message.toString()) }
        }.addOnFailureListener { onFail(it.message.toString()) }
    }

    override fun addUppMark(rating: Rating, onSuccess: () -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.addUpMark(rating).enqueue(object : Callback<StatusMsgResponse>{
            override fun onResponse(call: Call<StatusMsgResponse>, response: Response<StatusMsgResponse>) {
                if (response.isSuccessful){
                   response.body()?.let {
                       onSuccess()
                   }
                } else {
                    Log.i("tagAPI", "Error add or up mark: ${response.code()}")
                    onFail("Error add or up mark: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<StatusMsgResponse>, t: Throwable) {
                Log.i("tagAPI", "Error add or up mark: ${t.message}")
                onFail("Error add or up mark: ${t.message}")
            }
        })
    }

    override fun getUserMark(news_items_id:Int,onSuccess: (Double) -> Unit, onFail: (String) -> Unit) {
        RetrofitInstance.api.getUserMark(user_id = CurrUser.id, news_items_id).enqueue(object : Callback<RatingDefaultResponse>{
            override fun onResponse(call: Call<RatingDefaultResponse>, response: Response<RatingDefaultResponse>) {
                if (response.isSuccessful){
                    response.body()?.let { body->
                        onSuccess(body.you_mark!!.mark)
                    }
                } else {
                    Log.i("tagAPI", "Error get mark: ${response.code()}")
                    onFail("Error get mark: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<RatingDefaultResponse>, t: Throwable) {
                Log.i("tagAPI", "Error get mark: ${t.message}")
                onFail("Error get mark: ${t.message}")
            }
        })
    }
}