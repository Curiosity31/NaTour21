package com.example.natour.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.natour.Presenter.ProfiloPresenter;
import com.example.natour.R;

public class ProfiloActivity extends AppCompatActivity {

    private ProfiloPresenter profiloPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        this.profiloPresenter = new ProfiloPresenter(this, savedInstanceState);
        initViewComponents();

    }

    private void initViewComponents() {

        Button btn_torna_home = findViewById(R.id.btn_torna_home);
        btn_torna_home.setOnClickListener(v -> profiloPresenter.onBtnTornaHomeClicked());
    }


}
