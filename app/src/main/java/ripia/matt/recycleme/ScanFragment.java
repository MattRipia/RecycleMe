package ripia.matt.recycleme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.integration.android.IntentIntegrator;

public class ScanFragment extends Fragment implements View.OnClickListener{

    private Button scanButton;
    private TextView itemName, itemBrand, itemBarcode, itemRecNumber;
    private Globals globals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = Globals.getInstance();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        scanButton = view.findViewById(R.id.scan_button);

        itemName = view.findViewById(R.id.item_name);
        itemBrand = view.findViewById(R.id.item_brand);
        itemBarcode = view.findViewById(R.id.item_barcode);
        itemRecNumber = view.findViewById(R.id.item_rec_number);

        if (globals.getCurrentItem().getBarcode() != null) {

            itemRecNumber.setText(String.valueOf(globals.getCurrentItem().getRecNumber()));
            itemName.setText(globals.getCurrentItem().getName());
            itemBrand.setText(globals.getCurrentItem().getBrand());
            itemBarcode.setText(globals.getCurrentItem().getBarcode());
        }

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
}


