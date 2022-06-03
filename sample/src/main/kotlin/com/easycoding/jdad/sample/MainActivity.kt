package com.easycoding.jdad.sample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.easycoding.jdad.R
import com.easycoding.jdad.databinding.ActivityMainBinding
import com.easycoding.jdad.sample.models.User
import com.easycoding.jdad.sample.models.UserAddress
import com.easycoding.jdad.serializer.Serializer

class MainActivity: AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val user = User(
            firstName = "Nicola",
            lastName = "Mira",
            age = 17,
            isAdult = false,
            address = UserAddress("Horeaandr", 45)
        )
        Log.d("jdadjson", Serializer.serialize(user))
    }
}