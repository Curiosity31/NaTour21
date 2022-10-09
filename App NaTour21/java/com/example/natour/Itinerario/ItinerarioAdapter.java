package com.example.natour.Itinerario;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconOffset;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.natour.Activity.DettagliActivity;
import com.example.natour.Activity.ProfiloActivity;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.PointDAO;
import com.example.natour.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import timber.log.Timber;

public class ItinerarioAdapter extends RecyclerView.Adapter<ItinerarioAdapter.Recycle_itinerari_holder> {
    private final List<Itinerario> lista_itinerari;
    private final Context appContext;
    private final Bundle savedInstanceState;
    private final AppCompatActivity activity;
    private final PointDAO pointDAO;

    public ItinerarioAdapter(List<Itinerario> lista_itinerari, Context appContext, Bundle savedInstanceState, AppCompatActivity activity) {
        this.lista_itinerari = lista_itinerari;
        pointDAO = DAOFactory.getDaoFactory().getPointDAO();
        this.appContext = appContext;
        this.savedInstanceState = savedInstanceState;
        this.activity = activity;
        Mapbox.getInstance(appContext, appContext.getString(R.string.Map_Box_access_token));
    }

    @NonNull
    @Override
    public Recycle_itinerari_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Recycle_itinerari_holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_elemento_itinerario, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Recycle_itinerari_holder holder, int position) {
        holder.bind(this.lista_itinerari.get(position));
    }

    @Override
    public int getItemCount() {
        return this.lista_itinerari.size();
    }


    protected class Recycle_itinerari_holder extends RecyclerView.ViewHolder {
        private final TextView nome_elemento_itinerario;
        private final Button btn_nickname_utente;
        private final Button btn_dettagli_elemento;
        private final MapView mapViewElemento;
        private final Spinner spinner;

        public Recycle_itinerari_holder(@NonNull View itemView) {
            super(itemView);

            nome_elemento_itinerario = itemView.findViewById(R.id.id_nome_elemento_itinerario);
            btn_nickname_utente = itemView.findViewById(R.id.btn_nickname_utente);
            btn_dettagli_elemento = itemView.findViewById(R.id.btn_dettagli_elemento);
            spinner = itemView.findViewById(R.id.sp_menu_itinerario);
            mapViewElemento = itemView.findViewById(R.id.mapViewElemento);
            mapViewElemento.onCreate(savedInstanceState);
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
            nome_elemento_itinerario.setText(itinerario.getName());
            btn_nickname_utente.setText(itinerario.getUtente().getNickname());
            btn_nickname_utente.setOnClickListener(v -> {
                Intent intent = new Intent(appContext, ProfiloActivity.class);
                intent.putExtra("emailUtente", itinerario.getUtente().getEmail());
                activity.startActivity(intent);
            });

            mapViewElemento.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
                style.addImage(appContext.getString(R.string.symbolIconId), BitmapFactory.decodeResource(appContext.getResources(), com.mapbox.mapboxsdk.plugins.markerview.R.drawable.mapbox_marker_icon_default));
                setUpSource(style);
                setupLayer(style);
                pointDAO.getPointsTracciatoById(mapboxMap, itinerario.getId(), appContext);
            }));

            btn_dettagli_elemento.setOnClickListener(v ->
            {
                Intent intent = new Intent(appContext, DettagliActivity.class);
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

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0) {
                        Timber.d("Item Selected");
                    } else if (position == 1) {
                        String name = itinerario.getName();
                        name = name + ".gpx";
                        requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
                        pointDAO.getPointsForDownloadFile(itinerario.getId(), name, appContext, activity);
                    } else {
                        String name = itinerario.getName();
                        name = name + ".pdf";
                        requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 31);
                        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                            if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                PdfDocument myPdfDocument = new PdfDocument();
                                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,450,1).create();
                                PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
                                int time = itinerario.getTime();
                                int difficolta = itinerario.getDifficulties();
                                String diff;
                                if (difficolta == 0) {
                                    diff = "Facile";
                                } else if (difficolta == 1) {
                                    diff = "Intermedio";
                                } else if (difficolta == 2) {
                                    diff = "Difficile";
                                } else {
                                    diff = "Estremo";
                                }
                                String des = itinerario.getDescription();
                                StringBuilder myName = new StringBuilder(des);
                                int count = 0, index = -1;
                                for(char c: des.toCharArray()) {
                                    index ++;
                                    if(c == ' ') {
                                        count ++;
                                        if (count == 6) {
                                            count = 0;
                                            myName = new StringBuilder(des);
                                            myName.setCharAt(index, '\n');
                                            des = myName.toString();
                                        }
                                    }
                                }
                                Paint myPaint = new Paint();
                                String myString = "Nome: " + itinerario.getName() + "\n\n" + "Tempo Totale:\n" + "   ore: " + (time/60) + "\n" + "   minuti: " + (time%60) + "\n\n" +
                                        "Difficoltà: " + diff + "\n\n" + "Punto di inizio: " + itinerario.getStartingPoint() + "\n\n" +
                                        "Descrizione: " + "\n   " + myName.toString().replace("\n", "\n   ") + "\n\n" + "Accesso ai disabili: " + itinerario.isDisAccess();
                                int x = 10, y = 25;

                                for (String line:myString.split("\n")){
                                    myPage.getCanvas().drawText(line, x, y, myPaint);
                                    y+=myPaint.descent()-myPaint.ascent();
                                }

                                myPdfDocument.finishPage(myPage);
                                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), name);
                                try {
                                    myPdfDocument.writeTo(new FileOutputStream(file));
                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                    intent.setData(Uri.fromFile(file));
                                    activity.sendBroadcast(intent);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                                myPdfDocument.close();

                                Timber.d("File scaricato correttamente : %s", file.getPath());
                                new MaterialAlertDialogBuilder(activity, R.style.Theme_NaTour_AlertDialog)
                                        .setBackground(activity.getDrawable(R.drawable.for1))
                                        .setTitle("File " + name + " salvato correttamente")
                                        .setMessage("Il file è stato salvato nella cartella download del tuo dispositivo ed è pronto per essere stampato")
                                        .setPositiveButton("OK", (dialogInterface, i) -> Timber.d("Uscita dal material."))
                                        .show();
                            } else {
                                Timber.d("PDF NON SCARICATO 1 ");
                            }

                        } else {
                            Timber.d("PDF NON SCARICATO 2");
                        }

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

    }
}
