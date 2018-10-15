package ripia.matt.recycleme;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LeaderboardAdaptor extends ArrayAdapter {

    private ArrayList<User> leaders;
    private final Activity context;

    public LeaderboardAdaptor(Activity context, ArrayList<User> leaders){

        super(context,R.layout.leaderboard_layout , leaders);
        this.leaders = leaders;
        this.context = context;
    }

    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.leaderboard_layout, null, true);

        //Referencing the objects in leaderboard_layout.xml
        ImageView image = rowView.findViewById(R.id.leader_icon);
        TextView userName = rowView.findViewById(R.id.user_name);
        TextView userPoints = rowView.findViewById(R.id.user_points);

        if(position < 3) {
            int imageResource = 0;
            switch (position) {
                case 0: imageResource = R.drawable.lb_gold; break;
                case 1: imageResource = R.drawable.lb_silver; break;
                case 2: imageResource = R.drawable.lb_bronze; break;
            }
            image.setImageResource(imageResource);
        }

        //Populate fields
        userName.setText(leaders.get(position).getName());
        userPoints.setText(String.valueOf(leaders.get(position).getPoints()));

        return rowView;
    }
}
