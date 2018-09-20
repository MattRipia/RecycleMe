package ripia.matt.recycleme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanFragment extends Fragment implements View.OnClickListener{


    private Button scanButton;
    private TextView resultText;
    private static final int SCAN_ID = 49374;
    private Globals globals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = globals.getInstance();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        scanButton = view.findViewById(R.id.scan_button);
        resultText = view.findViewById(R.id.result_text);

        scanButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_button:
                startScan();
                break;
        }
    }

    // starts the scanner app and returns an item
    private void startScan() {
        //Start Scanning
        //TODO this does not work as this is a fragment not an activity
        IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
        scanIntegrator.initiateScan();
    }


    @Override
    // after a user logs in with either facebook or google, this method is called.
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // the activity that just completed was a scanner activity
        if (requestCode == SCAN_ID) {

            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            //successful scan, create a new item object to hold the barcode, then query the db to see if this exists
            // if an item exists, the recycling number is taken, if not then the user is asked to input this
            if (scanningResult != null) {
                globals.getCurrentItem().setBarcode(scanningResult.getContents());
                globals.getCurrentUser().setPoints(globals.getCurrentUser().getPoints() + 10);
                globals.getDatabase().checkItemInDatabase(globals.getCurrentItem());
                resultText.setText(scanningResult.getContents());
                Toast.makeText(getActivity(), "Scan success", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(getActivity(), "No scan data received!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}


