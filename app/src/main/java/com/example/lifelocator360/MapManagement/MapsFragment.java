package com.example.lifelocator360.MapManagement;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.lifelocator360.NavigationDrawerManagement.NavigationDrawerActivity;
import com.example.lifelocator360.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static android.content.Context.LOCATION_SERVICE;
import static com.example.lifelocator360.NavigationDrawerManagement.NavigationDrawerActivity.DEF_ZOOM;
import static com.example.lifelocator360.NavigationDrawerManagement.NavigationDrawerActivity.photos;

public class MapsFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static boolean GPSActive;
    public static ArrayList<Marker> noteMarkers;
    public static ArrayList<Marker> contactMarkers;
    public static Marker newMarker;
    private Integer tmp = 0;


    public boolean isGPSActive() {
        return GPSActive;
    }

    public void setGPSActive(boolean GPS) {
        GPSActive = GPS;
    }

    public void getDeviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (locationPermissionGranted()) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {

                            Location currentLocation = (Location) task.getResult();

                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEF_ZOOM);
                            }

                        } else {
                           //current location is null
                        }
                    }
                });
            }
        } catch (SecurityException e) {
           e.printStackTrace();
        }
    }

    public static void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public boolean storagePermissionGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    public boolean locationPermissionGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

    private void setNoteMarkers() {
        for(int i = 0; i < NavigationDrawerActivity.notes.size(); ++i) {
            String validPosition = NavigationDrawerActivity.notes.get(i).getLongitude();
            String noteTitle;
            if(!validPosition.equals("NO_INTERNET") && !validPosition.equals("NO_ADDRESS") && !validPosition.equals("NO_RESULT")) {
                Double lat = Double.parseDouble(NavigationDrawerActivity.notes.get(i).getLatitude());
                Double lng =  Double.parseDouble(NavigationDrawerActivity.notes.get(i).getLongitude());

                if(NavigationDrawerActivity.notes.get(i).getName().isEmpty())
                    noteTitle = "NESSUN TITOLO";
                else
                    noteTitle = NavigationDrawerActivity.notes.get(i).getName();

                MapsFragment.newMarker = MapsFragment.mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title(noteTitle));

                MapsFragment.newMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.note_icon_map));
                MapsFragment.newMarker.setTag(i);

                MapsFragment.noteMarkers.add(MapsFragment.newMarker);
            }
        }
    }

    private void setContactMarkers() {
        for(int i = 0; i < NavigationDrawerActivity.contacts.size(); ++i) {
            String validPosition = NavigationDrawerActivity.contacts.get(i).getLongitude();
            String contactTitle;
            if(!validPosition.equals("NO_INTERNET") && !validPosition.equals("NO_ADDRESS") && !validPosition.equals("NO_RESULT")) {
                Double lat = Double.parseDouble(NavigationDrawerActivity.contacts.get(i).getLatitude());
                Double lng =  Double.parseDouble(NavigationDrawerActivity.contacts.get(i).getLongitude());

                if(NavigationDrawerActivity.contacts.get(i).getName().isEmpty() && NavigationDrawerActivity.contacts.get(i).getSurname().isEmpty())
                    contactTitle = "NESSUN NOME";
                else
                    contactTitle = NavigationDrawerActivity.contacts.get(i).getName()+" "+NavigationDrawerActivity.contacts.get(i).getSurname();

                MapsFragment.newMarker = MapsFragment.mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                        .title(contactTitle));

                MapsFragment.newMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.contact_icon_map));
                MapsFragment.newMarker.setTag(NavigationDrawerActivity.contacts.get(i).getId());

                MapsFragment.contactMarkers.add(MapsFragment.newMarker);
            }
        }
    }


    private Bitmap addMarkerBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }



    private void loadMarkerIcon(final Marker marker, File file) {
        Uri imageUri = Uri.fromFile(file);
        Glide.with(getActivity()).asBitmap().load(imageUri).apply(new RequestOptions().override(120, 120)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                resource =Bitmap.createBitmap(resource, (resource.getWidth() - 120) / 2,(resource.getHeight() - 120) / 2, 120, 120);
                resource =  addMarkerBorder(resource, 6);
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(resource);
                marker.setIcon(icon);
                tmp++;
               // Log.e("FATTO", "Fatto: " + tmp);

            }
        });
    }


    private void setPhotoMarkers() {

        ArrayList<Integer> vett = new ArrayList<Integer>();

        int newTag = 0; //I marker delle foto, per essere riconosciuti al click, avranno tag negativi

        for(File f : NavigationDrawerActivity.photos) {
            String filePath = f.getAbsolutePath();

            //Leggo i metadata del file
            try {
                ExifInterface exifInterface = new ExifInterface(filePath);
                float[] latlng = new float[2];
                exifInterface.getLatLong(latlng);

                if(latlng[0] != 0 || latlng[1] != 0) {
                    //Log.e("LATLNG", "Latitudine: " + latlng[0] + " Longitudine: " + latlng[1]);
                    MapsFragment.newMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(latlng[0], latlng[1])));
                    MapsFragment.newMarker.setTag(--newTag);
                    vett.add((Integer) newMarker.getTag());
                //  Log.e("TAG", "il tag e'" + newTag);
                    loadMarkerIcon(newMarker, f);
               } else {
                    --newTag;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        for(Integer i: vett) {
            Log.e("PROVA100", "i: " + i);
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Setto tutti i markers
        setPhotoMarkers();
        setNoteMarkers();
        setContactMarkers();

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        final LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (locationPermissionGranted() && manager.isProviderEnabled(manager.GPS_PROVIDER)) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            setGPSActive(true); // flag maintain before get location
            mMap.setMyLocationEnabled(true);
        } else if (locationPermissionGranted() && !manager.isProviderEnabled(manager.GPS_PROVIDER)) {
            new GpsUtils(getContext()).turnGPSOn(new GpsUtils.onGpsListener() {
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    setGPSActive(isGPSEnable);
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Inizializzo il vettore per i markers
        noteMarkers = new ArrayList<Marker>();
        contactMarkers = new ArrayList<Marker>();

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void showImage(Integer tag) {

        tag = -tag - 1; //Il tag in posizione -2, si riferisce all'elemento 1 del vettore

        Dialog builder = new Dialog(getContext());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setCancelable(true);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(getContext());
        imageView.setImageURI(Uri.fromFile(photos.get(tag)));
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer tag = (Integer) marker.getTag();
        if (tag < 0)  { //Ho fatto il click su una foto, la gestisco separatamente
           showImage(tag);
           //marker.setZIndex(1.0f);
           Log.e("TAG CLICCATO: "," " + tag);
        return true;
    } else
        return false;
    }
}


