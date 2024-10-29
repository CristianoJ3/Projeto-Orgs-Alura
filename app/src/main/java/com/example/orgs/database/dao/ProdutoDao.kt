package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.orgs.model.Produto

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM produto")
    fun buscaTodos() : List<Produto>

    @Insert
    fun salva(vararg produto: Produto)
}