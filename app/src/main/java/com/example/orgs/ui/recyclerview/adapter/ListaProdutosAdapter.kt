package com.example.orgs.ui.recyclerview.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.GifDecoder
import com.example.orgs.R
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.extensions.tentaCarregarImagem
import com.example.orgs.model.Produto
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Base64.Decoder
import java.util.Locale


class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

    inner class ViewHolder(private val binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()

        fun vincula(produto: Produto) {
            val nome = binding.produtoItemNome
            nome.text = produto.nome
            val descricao = binding.produtoItemDescricao
            descricao.text = produto.descricao
            val valor = binding.produtoItemValor
            val valorEmMoeda = formataParaMoedaBrasileira(produto.valor)
            valor.text = valorEmMoeda

            val visibilidade = if(produto.imagem != null){
                View.VISIBLE
            }else {
                View.GONE
            }

            binding.imageView.visibility = visibilidade

            binding.imageView.tentaCarregarImagem(produto.imagem, imageLoader)
            //binding.imageView.load("https://img.freepik.com/fotos-gratis/uvas-morangos-abacaxi-kiwi-damasco-banana-e-abacaxi-inteiro_23-2147968680.jpg")
        }

        private fun formataParaMoedaBrasileira(valor: BigDecimal): String{
            val formatador: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt","br"))
            return formatador.format(valor)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProdutoItemBinding.inflate(LayoutInflater.from(context), parent, false)

        ///val inflater = LayoutInflater.from(context)
        ///val view = inflater.inflate(R.layout.produto_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = produtos.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }

}
