package com.example.pro_it_project.log_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.pro_it_project.Bottom_Navigation
import com.example.pro_it_project.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        // Инициализация элементов пользовательского интерфейса по их идентификаторам (R.id)
        val buttonLogIn: Button = findViewById(R.id.button_login)
        val buttonRegistration: Button = findViewById(R.id.button_registration)
        val buttonWithoutAcc: Button = findViewById(R.id.button_login_without_an_account)

        // Слушатель для кнопки "Войти"
        buttonLogIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }

        // Слушатель для кнопки "Зарегистрироваться"
        buttonRegistration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        // Слушатель для кнопки "Войти без аккаунта"
        buttonWithoutAcc.setOnClickListener {
            startActivity(Intent(this, Bottom_Navigation::class.java))
        }
    }
}