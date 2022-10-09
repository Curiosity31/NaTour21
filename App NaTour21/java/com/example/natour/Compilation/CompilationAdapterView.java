package com.example.natour.Compilation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Activity.ListaItinerariInCompilationActivity;
import com.example.natour.Activity.ProfiloActivity;
import com.example.natour.R;

import java.util.List;

public class CompilationAdapterView extends RecyclerView.Adapter<CompilationAdapterView.Recycle_compilation_holder_view> {
    private final List<Compilation> lista_compilation;
    private final Context appContext;


    public CompilationAdapterView(List<Compilation> lista_compilation, Context appContext) {
        this.lista_compilation = lista_compilation;
        this.appContext = appContext;
    }

    @NonNull
    @Override
    public Recycle_compilation_holder_view onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Recycle_compilation_holder_view(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_elemento_compilation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Recycle_compilation_holder_view holder, int position) {
        holder.bind(this.lista_compilation.get(position));
    }

    @Override
    public int getItemCount() {
        return this.lista_compilation.size();
    }


    protected class Recycle_compilation_holder_view extends RecyclerView.ViewHolder {
        private final TextView id_nome_elemento_compilation_view;
        private final TextView descrizione_compilation_view;
        private final Button btn_nickname_utente_compilation_view;
        private final Button btn_visualizza_itinerari;

        public Recycle_compilation_holder_view(@NonNull View itemView) {
            super(itemView);

            id_nome_elemento_compilation_view = itemView.findViewById(R.id.id_nome_elemento_compilation_view);
            descrizione_compilation_view = itemView.findViewById(R.id.descrizione_compilation_view);
            btn_nickname_utente_compilation_view = itemView.findViewById(R.id.btn_nickname_utente_compilation_view);
            btn_visualizza_itinerari = itemView.findViewById(R.id.btn_visualizza_itinerari);

        }


        @SuppressLint("UseCompatLoadingForDrawables")
        void bind(Compilation compilation) {
            id_nome_elemento_compilation_view.setText(compilation.getName());
            descrizione_compilation_view.setText(compilation.getDescription());
            btn_nickname_utente_compilation_view.setText(compilation.getUtente().getNickname());
            btn_nickname_utente_compilation_view.setOnClickListener(v -> {
                Intent intent = new Intent(appContext, ProfiloActivity.class);
                intent.putExtra("emailUtente", compilation.getUtente().getEmail());
                appContext.startActivity(intent);
            });
            btn_visualizza_itinerari.setOnClickListener(v ->
            {
                Intent intent = new Intent(appContext, ListaItinerariInCompilationActivity.class);

                intent.putExtra("CompilationId", compilation.getId());
                appContext.startActivity(intent);

            });
        }
    }

}
