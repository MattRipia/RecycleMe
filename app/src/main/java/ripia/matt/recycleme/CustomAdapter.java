package ripia.matt.recycleme;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Item> implements View.OnClickListener {

    private ArrayList<Item> list;
    private int lastPosition = -1;
    //private Context mContext;

    private static class ViewHolder{

        TextView name;
        TextView barcode;
        TextView brand;
        TextView recNumber;
    }

    public CustomAdapter(@NonNull Context context, ArrayList<Item> items) {
        super(context, R.layout.adapter_view_layout, items);
        this.list = items;
        //this.mContext = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the item for this position
        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        // stores all our view objects and references
        ViewHolder viewHolder;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();

            // new inflator
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(R.layout.adapter_view_layout, parent, false);

            // converts the id's of the text views in the adapter_view_layout to a view holder.
            // We can change the fields using the view holder.
            viewHolder.name = convertView.findViewById(R.id.list_item_name);
            viewHolder.barcode = convertView.findViewById(R.id.list_item_barcode);
            viewHolder.brand = convertView.findViewById(R.id.list_item_brand);
            viewHolder.recNumber = convertView.findViewById(R.id.list_item_rec);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;

        // sets the text of the next item in the list
        viewHolder.name.setText(item.getName());
        viewHolder.barcode.setText(item.getBarcode());
        viewHolder.brand.setText(item.getBrand());
        viewHolder.recNumber.setText(String.valueOf(item.getRecNumber()));

        // sets the colour of the text based off the recycling number
        if(item.getRecNumber() > 2)
        {
            viewHolder.name.setTextColor(0xffef7f7f);
            viewHolder.brand.setTextColor(0xffef7f7f);
            viewHolder.barcode.setTextColor(0xffef7f7f);
            viewHolder.recNumber.setTextColor(0xffef7f7f);
        }
        else
        {
            viewHolder.name.setTextColor(0xff86f994);
            viewHolder.brand.setTextColor(0xff86f994);
            viewHolder.barcode.setTextColor(0xff86f994);
            viewHolder.recNumber.setTextColor(0xff86f994);
        }

        // returns the view
        return convertView;
    }
}
