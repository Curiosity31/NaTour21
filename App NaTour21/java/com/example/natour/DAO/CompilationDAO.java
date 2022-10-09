package com.example.natour.DAO;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Compilation.Compilation;
public interface CompilationDAO {

    /* ------------------------------  GET ---------------------------------------------- */
    void getListaCompilation(RecyclerView recycle_compilation_view, Context appContext);

    /* ------------------------------  POST ---------------------------------------------- */

    void addCompilation(Compilation compilation, String email);

}
