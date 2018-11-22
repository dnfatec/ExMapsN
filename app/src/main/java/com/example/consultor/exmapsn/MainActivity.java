package com.example.consultor.exmapsn;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public GoogleMap mapa;
    public LatLng localizacao = new LatLng(-23.951137, -46.339025);
    private Button btMinhaPosicao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.nossoMapa);
        //atribui o mapa criado no layout ao tipo "MapFragment"
        mapFragment.getMapAsync(MainActivity.this);
        btMinhaPosicao = (Button) findViewById(R.id.btnPosicao);
        metodoBotao();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        //seta o mapa
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //seta o tipo de mapa
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(localizacao, 18);
        //atribui a geolocalizacao para o objeto update com altura 18
        mapa.animateCamera(cameraUpdate);
        //atribui a geolocalizacao no mapa
        mapa.addMarker(new MarkerOptions().position(localizacao).title("SFC !"));
        //atribui um "marker" com um texto especifico naquela localizacao

    }


    private void metodoBotao() {
        btMinhaPosicao = (Button) findViewById(R.id.btnPosicao);
        btMinhaPosicao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ativarServicoLocalizacao();
            }
        });
    }


    @SuppressLint("MissingPermission")
    //retira a necessidade de permissao para pegar a posicao atual
    private void ativarServicoLocalizacao()
    {
        //este metodo sera utilizado para ativar o servico de locacalizacao
        //sempre que houver uma mudanca de posicao do GPS este servico sera iniciado
        //pegando as coordenadas locais
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //pega o servico
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                   atualizarCoordenadasGPSMapas(location);
                   //caso houver mudanca na localizacao chama o metodo acima
                    //que atualiza no mapa
                }

                public void onStatusChanged(String provider, int status, Bundle extras) { }

                public void onProviderEnabled(String provider) { }

                public void onProviderDisabled(String provider) { }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
            Toast.makeText(this, "Erro ao atualizar GPS", Toast.LENGTH_SHORT).show();
        }

    }


    @SuppressLint("MissingPermission")
    public void atualizarCoordenadasGPSMapas(Location location)
    {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        //conforme a geolocalizacao encontrada
        localizacao = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(localizacao,19);
        mapa.animateCamera(cameraUpdate);
        mapa.setMyLocationEnabled(true);
        //seta o meu local
        mapa.addMarker(new MarkerOptions().position(localizacao).title("Diga oi !!!!"));
        //atribui um marcador
    }
}
