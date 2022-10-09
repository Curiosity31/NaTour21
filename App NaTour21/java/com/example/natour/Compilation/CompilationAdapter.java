package com.example.natour.Compilation;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Activity.DettagliActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.Itinerario.Itinerario;
import com.example.natour.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.LinkedList;
import java.util.List;

public class CompilationAdapter extends RecyclerView.Adapter<CompilationAdapter.Recycle_compilation_holder> {
    private final List<Itinerario> lista_itinerari;
    private final Context appContext;
    private final Bundle savedInstanceState;
    private final List<Itinerario> lista_itinerari_checked;

    public CompilationAdapter(List<Itinerario> lista_itinerari, Context appContext, Bundle savedInstanceState) {
        this.lista_itinerari = lista_itinerari;
        this.appContext = appContext;
        this.savedInstanceState = savedInstanceState;
        lista_itinerari_checked = new LinkedList<>();
        Mapbox.getInstance(appContext, appContext.getString(R.string.Map_Box_access_token));
    }

    @NonNull
    @Override
    public Recycle_compilation_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Recycle_compilation_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_elemento_inserisci_itinerario_in_compilation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Recycle_compilation_holder holder, int position) {
        holder.bind(this.lista_itinerari.get(position));
    }

    @Override
    public int getItemCount() {
        return this.lista_itinerari.size();
    }

    public List<Itinerario> getLista_itinerari_checked() {
        return lista_itinerari_checked;
    }


    protected class Recycle_compilation_holder extends RecyclerView.ViewHolder {
        private final TextView id_nome_elemento_compilation;
        private final TextView id_nickname_utente_compilation;
        private final Button btn_dettagli_elemento_compilation;
        private final MapView mapViewElementoCompilation;

        public Recycle_compilation_holder(@NonNull View itemView) {
            super(itemView);

            id_nome_elemento_compilation = itemView.findViewById(R.id.id_nome_elemento_compilation);
            id_nickname_utente_compilation = itemView.findViewById(R.id.id_nickname_utente_compilation);
            btn_dettagli_elemento_compilation = itemView.findViewById(R.id.btn_dettagli_elemento_compilation);
            mapViewElementoCompilation = itemView.findViewById(R.id.mapViewElementoCompilation);
            mapViewElementoCompilation.onCreate(savedInstanceState);
        }

        private void setUpSource(@NonNull Style loadedMapStyle) {
            loadedMapStyle.addSource(new GeoJsonSource(appContext.getString(R.string.geojsonSourceLayerId)));
        }

        private void setupLayer(@NonNull Style loadedMapStyle) {
            loadedMapStyle.addLayer(new SymbolLayer(appContext.getString(R.string.SYMBOL_LAYER_ID), appContext.getString(R.string.geojsonSourceLayerId)).withProperties(
                    iconImage(appContext.getString(R.string.symbolIconId)),
                    iconOffset(new Float[]{0f, -8f})
            ));
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        void bind(Itinerario itinerario) {
            id_nome_elemento_compilation.setText(itinerario.getName());
            id_nickname_utente_compilation.setText(itinerario.getUtente().getNickname());
            mapViewElementoCompilation.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                style.addImage(appContext.getString(R.string.symbolIconId), BitmapFactory.decodeResource(appContext.getResources(), com.mapbox.mapboxsdk.plugins.markerview.R.drawable.mapbox_marker_icon_default));
                setUpSource(style);
                setupLayer(style);
                DAOFactory.getDaoFactory().getPointDAO().getPointsTracciatoById(mapboxMap, itinerario.getId(), appContext);
            }));

            btn_dettagli_elemento_compilation.setOnClickListener(v ->
            {  Intent intent = new Intent(appContext, DettagliActivity.class);
                intent.putExtra("itinerarioId", itinerario.getId());
                intent.putExtra("nome_it", itinerario.getName());
                intent.putExtra("time", itinerario.getTime());
                intent.putExtra("difficolta", itinerario.getDifficulties());
                intent.putExtra("punto_di_inizio", itinerario.getStartingPoint());
                intent.putExtra("accessibilita_dis", itinerario.isDisAccess());
                intent.putExtra("descrizione", itinerario.getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appContext.startActivity(intent);
            });

            CheckBox cbElementoCompilation = itemView.findViewById(R.id.cbElementoCompilation);
            cbElementoCompilation.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    lista_itinerari_checked.add(itinerario);
                } else {
                    lista_itinerari_checked.remove(itinerario);
                }
            } );
        }
    }

}
