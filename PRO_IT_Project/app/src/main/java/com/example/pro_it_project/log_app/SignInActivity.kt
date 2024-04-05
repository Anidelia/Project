package com.example.pro_it_project.log_app

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pro_it_project.Bottom_Navigation
import com.example.pro_it_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInActivity : AppCompatActivity() {
    // Поле для работы с Firebase Authentication
    private lateinit var auth: FirebaseAuth

    // Переопределение метода onCreate при создании активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Инициализация Firebase Authentication
        auth = Firebase.auth

        // Инициализация элементов пользовательского интерфейса по их идентификаторам (R.id)
        val loginEditText: EditText = findViewById(R.id.editTextEmailAddress)
        val passwordEditText: EditText = findViewById(R.id.editTextPassword)
        val showPasswordCheckBox: CheckBox = findViewById(R.id.showPasswordCheckBox)
        val buttonReg: Button = findViewById(R.id.button_reg)
        val buttonSignIn: Button = findViewById(R.id.button_sign_in)

        // Установка слушателя на чекбокс
        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            // В зависимости от состояния чекбокса, устанавливаем или снимаем видимость пароля
            val inputType = if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            passwordEditText.inputType = inputType
            // Необходимо установить курсор в конец текста после изменения типа ввода
            passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
        }

        // Слушатель для кнопки "Войти"
        buttonSignIn.setOnClickListener {
            // Получение введенных данных из полей ввода
            val mail = loginEditText.text.toString()
            val pass = passwordEditText.text.toString()

            // Проверка наличия данных в полях ввода
            if(mail.isEmpty() || pass.isEmpty()){
                // Вывод сообщения об ошибке при отсутствии данных
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            // Попытка входа через Firebase Authentication
            auth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // успешный вход, переход в MainActivity
                        Toast.makeText(baseContext, "Authentication Success.",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Bottom_Navigation::class.java))
                    } else {
                        // Если вход не удался, выводим сообщение об ошибке
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // Слушатель для кнопки "Регистрация"
        buttonReg.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}