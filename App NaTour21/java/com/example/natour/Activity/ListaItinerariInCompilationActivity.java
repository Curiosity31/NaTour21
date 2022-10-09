package com.example.natour.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.natour.DAO.DAOFactory;
import com.example.natour.R;

public class ListaItinerariInCompilationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_iitinerari_in_compilation);

        RecyclerView recycle_itinerari_in_compilation_view = findViewById(R.id.recycle_itinerari_in_compilation_view);
        recycle_itinerari_in_compilation_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycle_itinerari_in_compilation_view.setHasFixedSize(true);
        long compilationId = getIntent().getLongExtra("CompilationId",-1);
        DAOFactory.getDaoFactory().getItinerarioDAO().getListaItinerariCompilationById(compilationId, getApplicationContext(), savedInstanceState, recycle_itinerari_in_compilation_view, this);

        Button btnEsci = findViewById(R.id.btnEsci);
        btnEsci.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CompilationActivity.class)));
    }
}