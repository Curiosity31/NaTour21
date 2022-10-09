package com.example.natour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.natour.AWS.Cognito;
import com.example.natour.BuildConfig;
import com.example.natour.Client.Retrieve;
import com.example.natour.DAO.DAOFactory;
import com.example.natour.DAO.UtenteDAO;
import com.example.natour.Presenter.HomePresenter;
import com.example.natour.R;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import timber.log.Timber;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private HomePresenter homePresenter;
    private Context appContext;
    private DrawerLayout drawerLayout;
    private UtenteDAO utenteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
        utenteDAO = DAOFactory.getDaoFactory().getUtenteDAO();
        appContext = getApplicationContext();
        RecyclerView recycle_itinerari = findViewById(R.id.recycle_itinerari);
        recycle_itinerari.setLayoutManager(new LinearLayoutManager(appContext));
        recycle_itinerari.setHasFixedSize(true);
        homePresenter = new HomePresenter(this, recycle_itinerari, savedInstanceState, appContext, utenteDAO);
        initViewComponents();
    }


    private void initViewComponents() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        SwipeRefreshLayout refreshLayout = findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(() -> homePresenter.refresh(refreshLayout));

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Button btnFiltri = findViewById(R.id.btnFiltri);
        btnFiltri.setOnClickListener(v -> homePresenter.onBtnFiltriClicked());

        Button btnVisualizzaCompilation = findViewById(R.id.btnVisualizzaCompilation);
        btnVisualizzaCompilation.setOnClickListener(v -> homePresenter.onBtnVisualizzaCompilationClicked());
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onNavigationItemSelected (@NonNull MenuItem item) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(appContext);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        utenteDAO.getLoggedIn(Cognito.getCognitoInstance(appContext).getUserPool().getCurrentUser().getUserId(), this,  new Retrieve() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onSuccessBoolean(@NonNull Boolean value) {

                //Se non è un utente registrato c'è bisogno di effettuare il log in
                if (account == null && (accessToken == null || accessToken.isExpired()) && !value) {
                    startActivity(new Intent(appContext, LoginActivity.class));
                } else {

                    switch (item.getItemId()) {
                        case R.id.nav_profile:
                            homePresenter.onBtnProfiloClicked();
                            break;

                        case R.id.nav_itinerario:
                            homePresenter.onBtnAddNewItinerarioClicked();
                            break;

                        case R.id.nav_compilation:
                            homePresenter.onBtnAddNewCompilationClicked();
                            break;

                        case R.id.nav_settings:
                            homePresenter.onBtnSettingClicked();
                            break;

                        case R.id.nav_log_out:
                            new MaterialAlertDialogBuilder(HomeActivity.this, R.style.Theme_NaTour_AlertDialog)
                                    .setBackground(getDrawable(R.drawable.for1))
                                    .setTitle("Esci")
                                    .setMessage("Sei sicuro di voler uscire?")
                                    .setPositiveButton("Esci", (dialogInterface, i) -> {
                                        Timber.d("Signout eseguito.");
                                        homePresenter.onBtnSignOutClicked();
                                    })
                                    .setNegativeButton("Annulla", (dialogInterface, i) -> Timber.d("Signout annullato."))
                                    .show();
                            break;
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Timber.d("Error getLoggedIn: %s", t.getMessage());
            }

        });

        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}