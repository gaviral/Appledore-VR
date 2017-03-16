/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vroneinc.vrone;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

/**
 * This shows how to change the camera position for the map.
 */
public class CameraDemoActivity extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    /**
     * The amount by which to scroll the camera. Note that this amount is in raw pixels, not dp
     * (density-independent pixels).
     */
    private static final int SCROLL_BY_PX = 100;

    static final LatLng VRCONTROLLERGPS = new LatLng(-33.87365, 151.20689);

    static final CameraPosition VRCONTROLLERCAMERA =
            new CameraPosition.Builder().target(VRCONTROLLERGPS)
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();

    private Marker vrControllerMarker;
    private CameraPosition vrControllerCameraPos;
    private GoogleMap mMap;
    private CheckBox mTrafficCheckbox;
    private CheckBox mBuildingsCheckbox;

    // firebase reference
    private DatabaseReference gpsDBReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_demo);
        vrControllerCameraPos = VRCONTROLLERCAMERA;

        Spinner spinner = (Spinner) findViewById(R.id.layers_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.layers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mTrafficCheckbox = (CheckBox) findViewById(R.id.traffic);
        mBuildingsCheckbox = (CheckBox) findViewById(R.id.buildings);

        setUpMapIfNeeded();

        // Initialize firebase reference
        FirebaseApp.initializeApp(this);
        gpsDBReference = FirebaseDatabase.getInstance().getReference("Users/test_id/Controller/location");
        gpsDBReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Update the controller's GPS location whenever we get data
                GPSCoord coord = dataSnapshot.getValue(GPSCoord.class);
                changeVRControllerGPS(coord.longitude, coord.latitude);
                Log.d("ControllerGPS", "Value is: " + coord);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ControllerGPS", "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        if (mMap != null) {
            updateTraffic();
            setMyLocation();
            updateBuildings();
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            mMap = googleMap;
                            setUpMap();
                        }
                    });
        }
    }

    private void setUpMap() {
        // We will provide our own zoom controls.
        mMap.getUiSettings().setZoomControlsEnabled(false);
        // Show Analog Stick
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));

        //Enable My Location to be shown
        setMyLocation();

        //Add Analog Stick Marker
        addMarkersToMap();
    }

    /**
     * When the map is not ready the CameraUpdateFactory cannot be used. This should be called on
     * all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void changeVRControllerGPS(double longitude, double latitude) {
        vrControllerCameraPos = new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                .zoom(15.5f)
                .bearing(0)
                .tilt(25)
                .build();

        vrControllerMarker.setPosition(new LatLng(latitude, longitude));
    }

    /**
     * Called when the Animate To "Go To Analog Stick" button is clicked.
     */
    public void onGoToVrController(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.newCameraPosition(vrControllerCameraPos), new CancelableCallback() {
            @Override
            public void onFinish() {
                Toast.makeText(getBaseContext(), "Animation to Analog Stick complete", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Animation to Analog Stick canceled", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

       /**
     * Called when the zoom in button (the one with the +) is clicked.
     */
    public void onZoomIn(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.zoomIn());
    }

    /**
     * Called when the zoom out button (the one with the -) is clicked.
     */
    public void onZoomOut(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.zoomOut());
    }

    /**
     * Called when the tilt more button (the one with the /) is clicked.
     */
    public void onTiltMore(View view) {
        CameraPosition currentCameraPosition = mMap.getCameraPosition();
        float currentTilt = currentCameraPosition.tilt;
        float newTilt = currentTilt + 10;

        newTilt = (newTilt > 90) ? 90 : newTilt;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Called when the tilt less button (the one with the \) is clicked.
     */
    public void onTiltLess(View view) {
        CameraPosition currentCameraPosition = mMap.getCameraPosition();

        float currentTilt = currentCameraPosition.tilt;

        float newTilt = currentTilt - 10;
        newTilt = (newTilt > 0) ? newTilt : 0;

        CameraPosition cameraPosition = new CameraPosition.Builder(currentCameraPosition)
                .tilt(newTilt).build();

        changeCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Called when the left arrow button is clicked. This causes the camera to move to the left
     */
    public void onScrollLeft(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_PX, 0));
    }

    /**
     * Called when the right arrow button is clicked. This causes the camera to move to the right.
     */
    public void onScrollRight(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_PX, 0));
    }

    /**
     * Called when the up arrow button is clicked. The causes the camera to move up.
     */
    public void onScrollUp(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_PX));
    }

    /**
     * Called when the down arrow button is clicked. This causes the camera to move down.
     */
    public void onScrollDown(View view) {
        if (!checkReady()) {
            return;
        }

        changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_PX));
    }

    private void changeCamera(CameraUpdate update) {
        changeCamera(update, null);
    }

    /**
     * Change the camera position by moving or animating the camera depending on the state of the
     * animate toggle button.
     */
    private void changeCamera(CameraUpdate update, CancelableCallback callback) {
        mMap.moveCamera(update);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        setLayer((String) parent.getItemAtPosition(position));
    }

    private void setLayer(String layerName) {
        if (!checkReady()) {
            return;
        }
        if (layerName.equals(getString(R.string.normal))) {
            mMap.setMapType(MAP_TYPE_NORMAL);
        } else if (layerName.equals(getString(R.string.hybrid))) {
            mMap.setMapType(MAP_TYPE_HYBRID);
        } else if (layerName.equals(getString(R.string.satellite))) {
            mMap.setMapType(MAP_TYPE_SATELLITE);
        } else if (layerName.equals(getString(R.string.terrain))) {
            mMap.setMapType(MAP_TYPE_TERRAIN);
        } else {
            Log.i("LDA", "Error setting layer with name " + layerName);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing.
    }

    /**
     * Called when the Traffic checkbox is clicked.
     */
    public void onTrafficToggled(View view) {
        updateTraffic();
    }

    private void updateTraffic() {
        if (!checkReady()) {
            return;
        }
        mMap.setTrafficEnabled(mTrafficCheckbox.isChecked());
    }

    private void setMyLocation() {
        if (!checkReady()) {
            return;
        }

        try {
            mMap.setMyLocationEnabled(true);
        } catch(SecurityException e) {
            Log.d("CameraDemoActivity", "Don't have permission to enable my location");
        }
    }

    /**
     * Called when the Buildings checkbox is clicked.
     */
    public void onBuildingsToggled(View view) {
        updateBuildings();
    }

    private void updateBuildings() {
        if (!checkReady()) {
            return;
        }
        mMap.setBuildingsEnabled(mBuildingsCheckbox.isChecked());
    }

    private void addMarkersToMap() {

        // Uses a custom icon with the info window popping out of the center of the icon
        vrControllerMarker = mMap.addMarker(new MarkerOptions()
                .position(VRCONTROLLERGPS)
                .title("VR Controller")
                .snippet("User ID: 123456")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .infoWindowAnchor(0.5f, 0.5f));
    }

}
