package com.example.projeto_cm.ui.Map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projeto_cm.R;
import com.example.projeto_cm.ui.Requests.RequestsFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;
    private FusedLocationProviderClient flpc;
    private LatLng markerLatlng;
    private boolean locationPermissionsGranted;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final String TAG = "Map Activity";
    private static final float DEFAULT_ZOOM = 15f;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        checkPermissions();

        Button selectBtn = (Button) view.findViewById(R.id.select_location_button);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (markerLatlng == null)
                    Toast.makeText(getContext(), "Nenhum local selecionado", Toast.LENGTH_LONG).show();
                else {
                    Bundle bundleLatlng = new Bundle();
                    try {
                        Geocoder geocoder = new Geocoder(getContext().getApplicationContext(), Locale.getDefault());
                        List<Address> list = geocoder.getFromLocation(markerLatlng.latitude, markerLatlng.longitude, 1);
                        if(list.isEmpty()) {
                            bundleLatlng.putString("LAT_LNG", "Location not provided");
                        }
                        else if(list.size() > 0){
                            bundleLatlng.putString("LAT_LNG", list.get(0).getAddressLine(0));
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                    RequestsFragment reqFragment = new RequestsFragment();
                    reqFragment.setArguments(bundleLatlng);
                    ft.replace(R.id.nav_host_fragment, reqFragment);
                    ft.commit();
                }

            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Selecione um lugar", Toast.LENGTH_LONG).show();

        map = googleMap;

        if (locationPermissionsGranted) {
            getCurrentLocation();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            setMarkerOnCurrentLocation();
        }
    }

    private void getCurrentLocation() {
        flpc = LocationServices.getFusedLocationProviderClient(getContext());
        try {
            if (locationPermissionsGranted) {
                Task location = flpc.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Found location");
                            Location currentLocation = (Location) task.getResult();
                            if(currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }
                        } else {
                            Log.d(TAG, "Cannot find current location");
                            Toast.makeText(getContext(), "Cannot find current location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "Security Exception: " + e.getMessage());
        }
    }

    private void setMarkerOnCurrentLocation() {
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {
                markerLatlng = latlng;
                map.clear();
                moveCamera(latlng, DEFAULT_ZOOM);
                moveMarker(latlng, latlng.latitude + " : " + latlng.longitude);

            }
        });
    }

    private void moveCamera(LatLng latlng, float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));
    }

    private void moveMarker(LatLng latlng, String title) {
        map.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(latlng).title(title));
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPermissionsGranted = true;
                SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            } else
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
        } else
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                            locationPermissionsGranted = false;
                            return;
                        }
                    }
                    locationPermissionsGranted = true;
                }
            }
        }
    }
}