package ripia.matt.recycleme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment is used to gather unknown item details from the user
 */
public class ItemFormFragment extends Fragment implements View.OnClickListener{

    private Button accept, cancel;
    private TextView itemBarcodeText;
    private EditText itemNameInput, itemBrandInput, itemRecInput;
    private Globals globals;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = Globals.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_form, container, false);

        accept = view.findViewById(R.id.accept_button);
        cancel = view.findViewById(R.id.cancel_button);

        itemBarcodeText = view.findViewById(R.id.item_barcode_text);

        itemNameInput = view.findViewById(R.id.item_name_input);
        itemBrandInput = view.findViewById(R.id.item_brand_input);
        itemRecInput = view.findViewById(R.id.item_rec_input);
        itemBarcodeText.setText("Item Barcode: " + globals.getCurrentItem().getBarcode());

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.accept_button:
                setItemDetails();
                break;
            case R.id.cancel_button:
                Toast.makeText(getActivity(), "Cancel Pressed", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ScanFragment(), "scan_fragment").commit();
                break;
        }
    }

    public void setItemDetails() {

        // a user has input a valid recycling number, save this number into the currentItem and update the record in the database
        boolean verifiedItem = checkItemDetails(itemRecInput.getText().toString());

        if(verifiedItem)
        {
            globals.getCurrentItem().setName(itemNameInput.getText().toString());
            globals.getCurrentItem().setBrand(itemBrandInput.getText().toString());
            globals.getCurrentItem().setRecNumber(Integer.parseInt(itemRecInput.getText().toString()));

            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ScanFragment(), "scan_fragment").commit();

            // adds the current item into the database, with the used added recycling number.
            globals.getDatabase().addItem();
            Toast.makeText(getActivity(), "Thank you for helping!", Toast.LENGTH_SHORT).show();
        }
        // a user has input a non-valid recycling number, show a toast text explaining this
        else
        {
            Toast.makeText(getActivity(), "That number is not a valid recycling number", Toast.LENGTH_SHORT).show();
        }
    }

    // this method checks that the recycling number input was correct
    // range: 1 - 8
    public boolean checkItemDetails(String checkString) {

        int inputInt = 0;
        Boolean valid = false;

        try {
            inputInt = Integer.parseInt(checkString);

        }catch(Exception e)
        {
            inputInt = 0;
        }

        if(inputInt > 0 && inputInt < 9)
        {
            valid = true;
        }

        return valid;
    }
}
