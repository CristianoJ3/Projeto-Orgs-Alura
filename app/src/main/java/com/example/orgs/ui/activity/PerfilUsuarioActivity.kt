package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.orgs.databinding.ActivityPerfilUsuarioBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class PerfilUsuarioActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("AcessoPerfilUsuario", "Activity do perfil de usuario criada")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        usuariologout()
        preencheCampos()
    }

    private fun usuariologout() {
        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                deslogaUsuario()
            }
        }
    }

    private fun preencheCampos() {
        lifecycleScope.launch {
            usuario
                .filterNotNull()
                .collect { usuarioLogado ->
                    //binding.id.text = usuarioLogado.id
                    binding.profileName.text = usuarioLogado.nome
                }
        }
    }

}