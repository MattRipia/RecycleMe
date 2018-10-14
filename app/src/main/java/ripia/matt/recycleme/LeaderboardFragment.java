package ripia.matt.recycleme;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * This fragment is used to show the contact us screen to the user.
 *
 */

public class LeaderboardFragment extends Fragment {

    private Globals globals;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globals = Globals.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        LeaderboardAdaptor adapter = new LeaderboardAdaptor(getActivity(), globals.getLeaders());

        listView = view.findViewById(R.id.leader_list);
        listView.setAdapter(adapter);

        return view;
    }
}
