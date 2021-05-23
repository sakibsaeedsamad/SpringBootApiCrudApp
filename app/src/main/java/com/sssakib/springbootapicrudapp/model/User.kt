package com.sssakib.springbootapicrudapp.model

import com.google.gson.annotations.SerializedName


class User {
    @SerializedName("id")
    var id: Int = 0
    @SerializedName("name")
    var name: String? = null

    @SerializedName("number")
    var number: String? = null

    @SerializedName("age")
    var age: String? = null

    @SerializedName("gender")
    var gender: String? = null

    @SerializedName("location")
    lateinit var location: String


    @SerializedName("image")
    var image: String? = null

    constructor(
        id: Int,
        name: String?,
        number: String?,
        age: String?,
        gender: String?,
        location: String,
        image: String?
    ) {
        this.id = id
        this.name = name
        this.number = number
        this.age = age
        this.gender = gender
        this.location = location
        this.image = image
    }

    constructor(
        name: String?,
        number: String?,
        age: String?,
        gender: String?,
        location: String,
        image: String?
    ) {
        this.name = name
        this.number = number
        this.age = age
        this.gender = gender
        this.location = location
        this.image = image
    }



}