package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.databas.AppDatabase
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

private const val TAG = "ListaProdutosActivity"

class ListaProdutosActivity : AppCompatActivity(R.layout.activity_lista_produtos) {

    private val adapter = ListaProdutosAdapter(context = this)

    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraRecyclerView()
        configuraFab()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val db = AppDatabase.instancia(this)
        val produtoDao = db.produtoDao()
        adapter.atualiza(produtoDao.buscaTodos())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_ordenacao, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val produtosOrdenado: List<Produto>? = when (item.itemId) {
            R.id.menu_lista_produtos_ordenar_nome_asc ->
                produtoDao.buscaTodosOrdenadorPorNomeAsc()
            R.id.menu_lista_produtos_ordenar_nome_desc ->
                produtoDao.buscaTodosOrdenadorPorNomeDesc()
            R.id.menu_lista_produtos_ordenar_descricao_asc ->
                produtoDao.buscaTodosOrdenadorPorDescricaoAsc()
            R.id.menu_lista_produtos_ordenar_descricao_desc ->
                produtoDao.buscaTodosOrdenadorPorDescricaoDesc()
            R.id.menu_lista_produtos_ordenar_valor_asc ->
                produtoDao.buscaTodosOrdenadorPorValorAsc()
            R.id.menu_lista_produtos_ordenar_valor_desc ->
                produtoDao.buscaTodosOrdenadorPorValorDesc()
            R.id.menu_lista_produtos_ordenar_sem_ordem ->
                produtoDao.buscaTodos()
            else -> null
        }
        produtosOrdenado?.let {
            adapter.atualiza(it)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            vaiParaFormularioProduto()
        }
    }

    private fun vaiParaFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter

        // implementação do listener para abrir a Activity de detalhes do produto
        // com o produto clicado
        adapter.quandoClicaNoItem =
            {
                Log.i("Formulario", "Produto clicado: $it")

                val intent = Intent(this, DetalhesProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, it.id)
                }
                startActivity(intent)
            }
        adapter.quandoClicaEmEditar = {
            Log.i(TAG, "configuraRecyclerView: Editar $it")
        }
        adapter.quandoClicaEmRemover = {
            Log.i(TAG, "configuraRecyclerView: Remover $it")
        }
    }
}