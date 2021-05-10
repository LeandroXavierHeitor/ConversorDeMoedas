package com.example.currencyconversion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import Retrofit.RetrofitBuilder;
import Retrofit.RetrofitInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    EditText edtValorConversao;
    TextView txvValorConvertido;
    TextView txvSaudacao;
    Spinner spnParaMoeda;
    Spinner spnDaMoeda;
    Button btnConverter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Inicialização

        edtValorConversao = findViewById(R.id.edtValorConversao);
        txvValorConvertido = findViewById(R.id.txvValorConvertido);
        txvSaudacao = findViewById(R.id.txvSaudacao);
        spnParaMoeda = findViewById(R.id.spnParaMoeda);
        spnDaMoeda = findViewById(R.id.spnDaMoeda);
        btnConverter = findViewById(R.id.btnConverter);



        //Trabalhando com Horários

        String currentTime = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
        int hora = Integer.parseInt(currentTime);
        if (hora < 12){
            txvSaudacao.setText("Bom dia ! ");
        }
        else if (hora > 12 && hora < 18){
            txvSaudacao.setText("Bom tarde !");
        }
        else{
            txvSaudacao.setText("Boa noite !");
        }



        //Adicionando funcionalidades e Verficações

        String[] dropDownList = {"USD", "INR","EUR","NZD", "BRL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, dropDownList);
        spnParaMoeda.setAdapter(adapter);
        spnDaMoeda.setAdapter(adapter);

            btnConverter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (edtValorConversao.getText().toString().trim().length() > 0) {

                        RetrofitInterface retrofitInterface = RetrofitBuilder.getRetrofitInstance().create(RetrofitInterface.class);
                        Call<JsonObject> call = retrofitInterface.getExchangeCurrency(spnDaMoeda.getSelectedItem().toString());
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                JsonObject res = response.body();
                                JsonObject rates = res.getAsJsonObject("rates");
                                double currency = Double.parseDouble(edtValorConversao.getText().toString());
                                double multiplier = Double.parseDouble(rates.get(spnParaMoeda.getSelectedItem().toString()).toString());
                                double result = currency * multiplier;
                                String moeda = spnParaMoeda.getSelectedItem().toString();

                                DecimalFormat formatador = new DecimalFormat("0.00");
                                String valorFinal = formatador.format(result);


                                // Verificação para simbolização
                                if(moeda.equalsIgnoreCase("USD")){
                                    txvValorConvertido.setText("$ "+valorFinal);
                                }
                                else if(moeda.equalsIgnoreCase("INR")){
                                    txvValorConvertido.setText("₹ "+valorFinal);
                                }
                                else if(moeda.equalsIgnoreCase("EUR")){
                                    txvValorConvertido.setText("€ "+valorFinal);
                                }
                                else if(moeda.equalsIgnoreCase("NZD")){
                                    txvValorConvertido.setText("$ "+valorFinal);
                                }
                                else{
                                    txvValorConvertido.setText("R$ "+valorFinal);
                                }





                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {

                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Valor Inválido", Toast.LENGTH_SHORT).show();

                    }
                }

            });

    }
}