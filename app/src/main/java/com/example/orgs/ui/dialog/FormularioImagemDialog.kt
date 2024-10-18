package com.example.orgs.ui.dialog

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.orgs.databinding.FormularioImagemnBinding
import com.example.orgs.extensions.tentaCarregarImagem

class FormularioImagemDialog (private val context: Context){

    // Criar o ImageLoader
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    fun mostra() {
        val binding = FormularioImagemnBinding.inflate(LayoutInflater.from(context))

        binding.formularioImagemBotaoCarregar.setOnClickListener {
            val url = binding.formularioImagemUrl.text.toString()
            binding.formularioImagemImageview.tentaCarregarImagem(url, imageLoader)
        }

        AlertDialog.Builder(context)
            .setView(binding.root)
            .setPositiveButton("Confirmar") { _, _ ->
                val url = binding.formularioImagemUrl.text.toString()
                ///binding.activityFormularioProdutoImagem.tentaCarregarImagem(url, imageLoader)
            }
            .setNegativeButton("Cancelar") { _, _ -> }
            .show()
    }
}