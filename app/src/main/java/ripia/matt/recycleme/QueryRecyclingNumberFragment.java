package ripia.matt.recycleme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;

public class QueryRecyclingNumberFragment extends Fragment implements View.OnClickListener{

    private Button accept, cancel;
    private TextView doesntExistText, barcode;
    private EditText input;
    private Globals globals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = Globals.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_query_recycling_number, container, false);

        accept = view.findViewById(R.id.accept_button);
        cancel = view.findViewById(R.id.accept_button);

        doesntExistText = view.findViewById(R.id.doesnt_exist_text);
        barcode = view.findViewById(R.id.barcode_text);
        barcode.setText("Barcode: " + globals.getCurrentItem().getBarcode());

        input = view.findViewById(R.id.number_input);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_button:
                // checks that the input has a valid number in it, then sets the current items recycling number
                if(Integer.parseInt(input.getText().toString()) > 0 && Integer.parseInt(input.getText().toString()) < 9)
                {
                    globals.getCurrentItem().setRecyclingNumber(Integer.parseInt(input.getText().toString()));
                    Toast.makeText(getActivity(), "Thank you for helping!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity(), "That number is not a valid recycing number", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel_button:
                Toast.makeText(getActivity(), "Cancel Pressed", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
