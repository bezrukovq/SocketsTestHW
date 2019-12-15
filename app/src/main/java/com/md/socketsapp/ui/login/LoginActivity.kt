package com.md.socketsapp.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.md.socketsapp.utils.Const
import com.md.socketsapp.utils.Const.DEVICE_ID
import com.md.socketsapp.utils.Const.PREFERENCES
import com.md.socketsapp.utils.Const.USERNAME
import com.md.socketsapp.R
import com.md.socketsapp.api.LoginApiService
import com.md.socketsapp.models.User
import com.md.socketsapp.ui.chat.ChatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private val api = Retrofit.Builder()
        .baseUrl(Const.API_BASE)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        .create(LoginApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener { login(User(et_name.text.toString(),et_device_id.text.toString())) }
    }

    @SuppressLint("CheckResult")
    fun login(user: User) {
            api.login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.status == "ok") {
                        val sharedPref =
                            getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
                        sharedPref.edit()
                            .putString(DEVICE_ID, user.deviceId)
                            .putString(USERNAME, user.username).apply()
                        startActivity(Intent(this, ChatActivity::class.java))
                    }
                }, {
                    Toast.makeText(this, "Error ${it.message}", Toast.LENGTH_SHORT).show()
                    it.printStackTrace()
                })
    }
}
