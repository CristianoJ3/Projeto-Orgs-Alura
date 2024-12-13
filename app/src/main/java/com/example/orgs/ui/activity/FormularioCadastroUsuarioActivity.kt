package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.databas.AppDatabase
import com.example.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.example.orgs.extensions.toHash
import com.example.orgs.extensions.toast
import com.example.orgs.model.Usuario
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsuario = criaUsuario()
            cadastra(novoUsuario)
        }
    }

    private fun cadastra(usuario: Usuario) {
        lifecycleScope.launch {
            try {
                dao.salva(usuario)
                Log.i("DadosUsuario", "Usuario: $usuario")
                finish()
            } catch (e: Exception) {
                Log.e("CadastroUsuario", "onCreate: $usuario")
                toast("Falha ao cadastrar usuário!")
            }
        }
    }

    private fun criaUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString().toHash()
        Log.i("DadosUsuario", "Usuario: $usuario, Nome: $nome, Senha: $senha")
        return Usuario(usuario, nome, senha)
    }
}