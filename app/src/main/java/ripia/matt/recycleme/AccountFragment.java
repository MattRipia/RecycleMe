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
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import ripia.matt.recycleme.Globals;
import ripia.matt.recycleme.MapFunctionality;
import ripia.matt.recycleme.R;

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
    public void onResume() {
        super.onResume();
        update();
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
                    Intent intent = new Intent(getActivity(), MapFunctionality.class);
                    startActivity(intent);

                }catch(Exception e)
                {
                    Log.d("LocationButton", " error - " + e.getMessage());

                }

                update();
                break;
        }
    }

}

