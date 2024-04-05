package com.example.pro_it_project.log_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pro_it_project.Bottom_Navigation
import com.example.pro_it_project.FirebaseAPI
import com.example.pro_it_project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity(){
    // Поле для работы с Firebase Authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = Firebase.auth
            //Сделать повторный пароль

        val nameEditText: EditText = findViewById(R.id.namefield)
        val surnameEditText: EditText = findViewById(R.id.surnamefield)
        val loginEditText: EditText = findViewById(R.id.loginfield)
        val passwordEditText: EditText = findViewById(R.id.passwordfield)
        val passwordConfirmEditText: EditText = findViewById(R.id.passwordConfirmField)
        val showPasswordCheckBox: CheckBox = findViewById(R.id.showPasswordCheckBox)
        val buttonsubmit: Button = findViewById(R.id.buttonReg)

        // Установка слушателя на чекбокс
        showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            // В зависимости от состояния чекбокса, устанавливаем или снимаем видимость пароля
            val inputType = if (isChecked) InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            passwordEditText.inputType = inputType
            // Необходимо установить курсор в конец текста после изменения типа ввода
            passwordEditText.text?.let { passwordEditText.setSelection(it.length) }
        }

        buttonsubmit.setOnClickListener {

            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val email = loginEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = passwordConfirmEditText.text.toString()

            if(email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()){
                Toast.makeText(this, "Заполните все поля ", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Проверка совпадения паролей
            if (password != confirmPassword) {
                setRedBackground(passwordConfirmEditText)
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                resetBackground(passwordConfirmEditText)
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //успешный ввод, переход к main activity и сохранение в ShaderPref
                        val shaderPref = getSharedPreferences("SPDB", Context.MODE_PRIVATE)
                        val editor = shaderPref.edit()

                        FirebaseAPI().writeUserFB(name, email, surname){ id ->
                            editor.apply{
                                putString("login", email)
                                putString("password", password)
                                putInt("id", id)
                                apply()
                            }
                        }

                        Toast.makeText(baseContext, "Authentication Success.",
                            Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Bottom_Navigation::class.java))

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(this, "Error occurred ${it.localizedMessage}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
    // Функция для установки красного фона для поля ввода
    private fun setRedBackground(editText: EditText) {
        editText.setBackgroundResource(android.R.color.holo_red_light)
    }

    // Функция для сброса фона поля ввода к исходному цвету
    private fun resetBackground(editText: EditText) {
        editText.setBackgroundResource(android.R.color.transparent)
    }
}