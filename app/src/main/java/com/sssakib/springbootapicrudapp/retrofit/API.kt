package com.sssakib.springbootapicrudapp.retrofit

import com.sssakib.springbootapicrudapp.model.DeleteResponse
import com.sssakib.springbootapicrudapp.model.User
import retrofit2.Call
import retrofit2.http.*

interface API {


    @GET("allUser")
    fun findAllUsers(): Call<User>?

    @POST("insertUser")
    fun insertUser(@Body user: User?): Call<User?>?

    @PUT("updateUser/{id}")
    fun updateUser(
        @Body user: User?,
        @Path("id") id: Int
    ): Call<User?>?

    @DELETE("deleteUser/{id}")
    fun deleteUser(@Path("id") id:Int): Call<DeleteResponse?>
}