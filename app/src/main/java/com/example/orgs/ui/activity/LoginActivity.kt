package com.example.orgs.ui.activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.databas.AppDatabase
import com.example.orgs.extensions.vaiPara
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.extensions.toHash
import com.example.orgs.extensions.toast
import dataStore
import kotlinx.coroutines.launch
import usuarioLogadoPreferences

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val usuarioDao by lazy {
        AppDatabase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }
    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            autentica(usuario, senha)
        }
    }

    private fun autentica(usuario: String, senha: String) {
        lifecycleScope.launch {
            val senhaHash = senha.toHash()
            usuarioDao.autentica(usuario, senhaHash)?.let { usuario ->
                dataStore.edit { preferences ->
                    preferences[usuarioLogadoPreferences] = usuario.id
                    Log.i("DadosUsuario", "usuario: $usuario, senha: $senhaHash")
                }
                vaiPara(ListaProdutosActivity::class.java)
                finish()
            } ?: toast("Falha na autenticação!")
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioCadastroUsuarioActivity::class.java)
        }
    }
}