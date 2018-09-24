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

        globals = Globals.getInstance();
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
                Toast.makeText(getActivity(), "Zone - Coming Soon", Toast.LENGTH_SHORT).show();
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

    // sets the current location of 'currentuser' when the user clicks the 'get location' button,
    // this location is used when checking the recycling zone of that user.
    private void getCurrentLocation() {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude,  1);
            String streetNo = addresses.get(0).getFeatureName();
            String streetName = addresses.get(0).getThoroughfare();
            String postCode = addresses.get(0).getPostalCode();

            // checks the strings above and ensures that an address is returned correctly
            boolean validAddress = checkLocationDetails(streetNo, streetName, postCode);

            // if an address is valid, add this to the current users address
            if(validAddress)
            {
                globals.getCurrentUser().setAddress(streetNo + " "  + streetName + ", "  + postCode);
            }
            else
            {
                // a valid address was not found, create a log and toast message
                Toast.makeText(getContext(),"Invalid address, unable to update", Toast.LENGTH_LONG).show();
                Log.d("Location Error", "bad address " + streetNo + ", " + streetName + ", "  + postCode);
            }

        } catch (Exception e) {
            Log.d("getCurrentLocation", "error -" + e.getMessage());
        }
    }

    /* This method ensures that the location received from 'GetCurrentLocation' is formatted
    *  correctly with no null values or repeating variables, as in some cases 'NULL' is
    *  returned or the postal code is the same as the street number which is incorrect.
    *  This method needs to be public for testing purposes, need to be changed on release.
    */
    public boolean checkLocationDetails(String streetNo, String streetName, String postCode) {

        try{
            boolean validAddress = true;
            int intPostCode;

            // duplicated variables
            if (streetNo.equals(postCode) || streetNo.equals(streetName) || streetName.equals(postCode)) {
                validAddress = false;
                return validAddress;
            }

            // empty variables
            if (streetNo.isEmpty() || streetName.isEmpty() || postCode.isEmpty() || streetNo.equals(" ") || streetName.equals(" ") || postCode.equals(" ")) {
                validAddress = false;
                return validAddress;
            }

            // null variables
            if (streetNo.equals("null") || streetName.equals("null") || postCode.equals("null")) {
                validAddress = false;
                return validAddress;
            }

            // post code isn't a number / Exception gets caught
            try {
                intPostCode = Integer.parseInt(postCode);

            } catch (Exception e) {
                validAddress = false;
                return validAddress;
            }

            // post code isnt 4 characters
            if (postCode.length() != 4) {
                validAddress = false;
                return validAddress;
            }

            return validAddress;

        // if any null values get parsed into the method, catch these and return false
        }catch(NullPointerException e)
        {
            return false;
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
            if (gpsLocation != null)
            {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();
                Log.d("gpsLocation used", " ");
            }
            else if (networkLocation != null)
            {
                latitude = networkLocation.getLatitude();
                longitude = networkLocation.getLongitude();
                Log.d("networkLocation used", " ");
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
    private void buildAlertMessageNoGps() {

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
