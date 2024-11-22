package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.databinding.ActivityInicialBinding

class ActivityInicial : AppCompatActivity() {

    private lateinit var binding: ActivityInicialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflando o layout da tela
        binding = ActivityInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurando o clique no botão "Vamos lá"
        binding.activityInicialButton.setOnClickListener {
            // Ir para a próxima Activity (ListaProdutosActivity)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Finalizar a Activity Inicial para que o usuário não volte para ela
            finish()
        }
    }
}
