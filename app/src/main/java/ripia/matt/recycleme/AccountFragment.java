package ripia.matt.recycleme;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class AccountFragment extends Fragment implements View.OnClickListener{

    private Button zoneButton, addressButton;
    private TextView profileText;

    private LocationManager locationManager;
    private double latitude, longitude;
    private Globals globals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globals = globals.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        zoneButton = view.findViewById(R.id.zone_button);
        addressButton = view.findViewById(R.id.address_button);
        profileText = view.findViewById(R.id.user_detail_text);

        addressButton.setOnClickListener(this);
        zoneButton.setOnClickListener(this);

        update();
        return view;
    }

    private void update() {

        profileText.setText("User          - " + globals.getCurrentUser().getName() + "\n"
                + "Points       - " + globals.getCurrentUser().getPoints() + "\n"
                + "Address    - " + globals.getCurrentUser().getAddress() + "\n"
                + "Last Scanned - " + globals.getCurrentItem().getBarcode());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone_button:
                Toast.makeText(getActivity(), "Zone Button Hit!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.address_button:
                Toast.makeText(getActivity(), "Location Button Hit!", Toast.LENGTH_SHORT).show();

                try {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    getCurrentCoordinates();
                    getCurrentLocation();

                }catch(Exception e)
                {
                    Log.d("LocationButton", " error - " + e.getMessage());
                    buildAlertMessageNoGps();
                }

                update();
                break;
        }
    }

    // sets the current location of 'currentuser'
    private void getCurrentLocation() {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude,  1);

            // hardcoded lat/long test for matts address!
            //addresses = geocoder.getFromLocation(-36.901954, 174.699831,  1);

            String streetNo = addresses.get(0).getFeatureName();
            String streetName = addresses.get(0).getThoroughfare();
            String postCode = addresses.get(0).getPostalCode();

            globals.getCurrentUser().setAddress(streetNo + " "  + streetName + ", "  + postCode);

        } catch (Exception e) {
            Log.d("getCurrentLocation", "error -" + e.getMessage());
        }
    }

    // sets the coordinates of 'currentUser'
    private void getCurrentCoordinates() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
        {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location providerLocation = locationManager.getLastKnownLocation(LocationManager. PASSIVE_PROVIDER);

            // if the gps locator found a current location, then set the co-ordinates
            // this method tries to get the gps coordinates in 3 different ways
            // if none of these ways produces a latitude or longitude then a toast message is shown.
            if (networkLocation != null)
            {
                latitude = networkLocation.getLatitude();
                longitude = networkLocation.getLongitude();
                Log.d("networkLocation used", " ");
            }
            else  if (gpsLocation != null)
            {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();
                Log.d("gpsLocation used", " ");
            }
            else  if (providerLocation != null)
            {
                latitude = providerLocation.getLatitude();
                longitude = providerLocation.getLongitude();
                Log.d("providerLocation used", "");
            }
            else
            {
                Toast.makeText(getActivity(),"Unable to Trace your location",Toast.LENGTH_SHORT).show();
                Log.d("getCurrentCoordinates", "Trace your location - problem at getCurrentCoordinates method");
            }
        }
    }

    // shows an alert if a gps location cannot be obtained
    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
