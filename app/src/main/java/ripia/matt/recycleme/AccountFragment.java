package ripia.matt.recycleme;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class AccountFragment extends Fragment implements View.OnClickListener {

    private Button zoneButton;
    private TextView profileText;
    private User currentUser = new User();
    private Item currentItem = new Item();
    private Database database = new Database();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentUser = database.checkUserInDatabase(FirebaseAuth.getInstance().getCurrentUser(), currentUser);
        currentItem = new Item();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        zoneButton = view.findViewById(R.id.zone_button);
        profileText = view.findViewById(R.id.user_detail_text);

        zoneButton.setOnClickListener(this);

        update();
        return view;

    }

    private void update() {


        profileText.setText("User          - " + currentUser.getName() + "\n"
                + "Points       - " + currentUser.getPoints() + "\n"
                + "Address    - " + currentUser.getAddress() + "\n"
                + "Last Scanned - " + currentItem.getBarcode());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.zone_button:
                Toast.makeText(getActivity(), "Zone Button Hit!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
