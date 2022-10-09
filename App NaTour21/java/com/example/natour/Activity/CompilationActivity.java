package com.example.natour.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.natour.DAO.CompilationDAO;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.R;

public class CompilationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compilation);

        CompilationDAO compilationDAO = DAOFactory.getDaoFactory().getCompilationDAO();

        RecyclerView recycle_compilation = findViewById(R.id.recycle_itinerari_compilationView);
        recycle_compilation.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycle_compilation.setHasFixedSize(true);
        compilationDAO.getListaCompilation(recycle_compilation, CompilationActivity.this);
        Button btnEsciComp = findViewById(R.id.btnEsciComp);
        btnEsciComp.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HomeActivity.class)));

        SwipeRefreshLayout refreshLayoutComp = findViewById(R.id.swipe_refresh_comp);
        refreshLayoutComp.setOnRefreshListener(() -> {
            compilationDAO.getListaCompilation(recycle_compilation, CompilationActivity.this);
            refreshLayoutComp.setRefreshing(false);
        });
    }
}