package ripia.matt.recycleme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanFragment extends Fragment implements View.OnClickListener{

    private Button scanButton;
    private Globals globals;

    private ListView list;
    private ScanHistoryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = Globals.getInstance();
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        // clears the current item in a users history
        globals.getCurrentUser().getItems().clear();

        // TODO make it so the database only gets updated/synced once the user exits the program, not every time the scan fragment is opened
        // syncs the view with the database
        globals.getDatabase().syncItemHistory();

        // this adapter is added to the list so we can populate items from this and have a custom layout.
        adapter = new ScanHistoryAdapter(getContext(), globals.getCurrentUser().getItems());

        scanButton = view.findViewById(R.id.scan_button);
        list = view.findViewById(R.id.history_list);
        list.setAdapter(adapter);
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

        IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
        scanIntegrator.initiateScan();
    }
}