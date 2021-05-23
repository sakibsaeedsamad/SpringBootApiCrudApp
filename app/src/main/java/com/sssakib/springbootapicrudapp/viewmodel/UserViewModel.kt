package com.sssakib.springbootapicrudapp.viewmodel

import android.content.Intent
import android.widget.Toast
import com.sssakib.springbootapicrudapp.model.User
import com.sssakib.springbootapicrudapp.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sssakib.springbootapicrudapp.model.DeleteResponse
import org.w3c.dom.Entity
import kotlin.coroutines.CoroutineContext

class UserViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var allUsers: MutableLiveData<List<User>>

    init {
        allUsers = MutableLiveData()
       getAllUsers()
    }

    fun getAllUsersObservers(): MutableLiveData<List<User>> {

        println(allUsers)
        return allUsers

    }

    fun getAllUsers() {

        val call: Call<List<User>?>? = RetrofitClient
            .instance
            ?.aPI
            ?.findAllUsers()
        call?.enqueue(object : Callback<List<User>?> {
            override fun onFailure(call: Call<List<User>?>?, t: Throwable) {
            }
            override fun onResponse(call: Call<List<User>?>?, response: Response<List<User>?>?) {
                if (response!!.isSuccessful) {
                    val list = response.body()
                    allUsers.postValue( list as List<User>)
                }
            }

        })

    }

    fun insertUserInfo(user: User) {

        val call: Call<User?>? = RetrofitClient
            .instance
            ?.aPI
            ?.insertUser(user)
        call?.enqueue(object : Callback<User?> {
            override fun onFailure(call: Call<User?>, t: Throwable) {

            }

            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    getAllUsers()
                }
            }


        })


    }

    fun updateUserInfo(user: User) {
        val call: Call<User?>? = RetrofitClient
            .instance
            ?.aPI
            ?.updateUser(user, user.id)
        call?.enqueue(object : Callback<User?> {
            override fun onFailure(call: Call<User?>, t: Throwable) {

            }

            override fun onResponse(call: Call<User?>, response: Response<User?>) {
                if (response.isSuccessful) {
                    getAllUsers()
                }
            }


        })
    }

    fun deleteUserInfo(user: User) {
        val call: Call<DeleteResponse?>? = RetrofitClient
            .instance
            ?.aPI
            ?.deleteUser(user.id)
        call?.enqueue(object : Callback<DeleteResponse?> {
            override fun onFailure(call: Call<DeleteResponse?>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<DeleteResponse?>,
                response: Response<DeleteResponse?>
            ) {
                if (response.isSuccessful) {
                    getAllUsers()
                }
            }


        })
    }


}






