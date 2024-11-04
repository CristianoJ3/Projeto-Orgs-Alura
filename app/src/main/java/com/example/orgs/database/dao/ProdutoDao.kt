package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.orgs.model.Produto

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM produto")
    fun buscaTodos() : List<Produto>

    @Insert
    fun salva(vararg produto: Produto)

    @Delete
    fun remove(produto: Produto)

    @Update
    fun atualiza(produto: Produto)
}