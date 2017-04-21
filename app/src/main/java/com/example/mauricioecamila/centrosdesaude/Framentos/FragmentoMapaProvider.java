package com.example.mauricioecamila.centrosdesaude.Framentos;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

//Usando a classe SupportMapFragment o mapa se torna um fragmento, tornando possível inserí-lo em outros activitys
public class FragmentoMapaProvider extends SupportMapFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, android.location.LocationListener {

    private GoogleMap mMap;
    private static String TAG = "GPS";
    private LatLng latlong;
    private String tituloMarcador;

    //Objeto responsável para trabalhar com o provider
    private LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMapAsync(this);

        Bundle parametros = getArguments();
        if(parametros != null) {
            double lat, lng;
            lat = Double.parseDouble(parametros.getString("paramLatitude").toString());
            lng = Double.parseDouble(parametros.getString("paramLongitude").toString());
            latlong = new LatLng(lat, lng);
            tituloMarcador = parametros.getString("paramTituloMarcador");
        }
    }

    //Execute sempre que estiver visível
    @Override
    public void onResume() {
        super.onResume();
        //Ativa o GPS
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        //Aqui você recupera a posição do GPS (segundo parâmetro é a quantos milessegundos irá recuperar a posição,
        // o terceiro a quantos metros)
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,3, (android.location.LocationListener) this);
    }

    //Execute algo quando estiver em pausa
    @Override
    public void onPause() {
        super.onPause();
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Necessário fazer dentro de um try por causa da permissão do setMyLocation
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            mMap = googleMap;

            //Coloca o zoom disponível no mapa
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
        }catch (SecurityException ex)
        {
            Log.e(TAG, "Error", ex);
        }
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-33.87365, 151.20689);

        //Marcador no mapa e coloca um título no marcador
        MarkerOptions marker = new MarkerOptions();
        marker.position(latlong);
        marker.title(tituloMarcador);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.addMarker(marker);
        //Esse método move a camera para o local definido na coordenada
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong));
        //Zoom de de nível 14
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        //Barra do google maps
        mMap.getUiSettings().setMapToolbarEnabled(true);
    }
    
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(getActivity(), "Posição Alterada", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        //Toast.makeText(getActivity(), "O status do Provider foi Alterado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "GPS Habilitado", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "GPS Desabilitado", Toast.LENGTH_SHORT).show();
    }

    // A cada clique pode mostrar algo na tela
    @Override
    public void onMapClick(LatLng latLng) {

    }
}
