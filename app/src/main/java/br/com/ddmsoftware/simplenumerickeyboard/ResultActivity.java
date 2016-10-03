package br.com.ddmsoftware.simplenumerickeyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends AppCompatActivity {
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

        Button btnVoltar = (Button)findViewById(R.id.btnVoltar) ;

        TextView tvResultAcertos =  (TextView)findViewById(R.id.tvTotalAcertos);
        TextView tvResultErros =  (TextView)findViewById(R.id.tvTotalErros);
        TextView tvResultPercAcerto =  (TextView)findViewById(R.id.textView11);
        TextView tvResult = (TextView) findViewById(R.id.tvResult2);
        ImageView imgResultOK = (ImageView) findViewById(R.id.imgResultIcon);

        imgResultOK.setImageResource(R.drawable.iconok);

        float fPercentual = 0;

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        message = intent.getStringExtra(TableGameActivity.EXTRA_MESSAGE);

        String[] sResultados = message.split(";");


        tvResultAcertos.setText(sResultados[0]);
        tvResultErros.setText(sResultados[1]);
        tvResultPercAcerto.setText(sResultados[2] + "%");
        fPercentual = Float.parseFloat(sResultados[2]);

        Toast.makeText(ResultActivity.this, sResultados[0] + ";" + sResultados[1] + ";" + sResultados[2], Toast.LENGTH_SHORT).show();

        if( fPercentual >= 80)
            tvResult.setText("Parabéns. Você esta no caminho certo.");
        else
        if( fPercentual >= 50)
            tvResult.setText("Você esta no caminho certo, mas pode melhorar.");
        else
        if( fPercentual < 50) {
            tvResult.setText("Vamos estudar um pouco mais.");
            imgResultOK.setImageResource(R.drawable.iconnok);
        }

    }
}
