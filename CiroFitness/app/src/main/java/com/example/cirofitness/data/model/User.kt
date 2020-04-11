package com.example.cirofitness.data.model

import java.util.*

class User(username:String,password:String, email:String, fechaNacimiento: Date, sexo:String) {
    //Atrb
    val username: String
    val password:String
    val email:String
    val sexo:String
    val fechaNacimiento:Date

    init {
        this.username = username
        this.password = password
        this.email = email
        this.sexo = sexo
        this.fechaNacimiento = fechaNacimiento
    }
    //Methods
    override fun toString(): String {
        return "User(username='$username', password='$password', email='$email', sexo='$sexo', fechaNacimiento=$fechaNacimiento)"
    }





}
