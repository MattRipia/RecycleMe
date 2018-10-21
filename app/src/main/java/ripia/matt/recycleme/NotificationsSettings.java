package ripia.matt.recycleme;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.util.Calendar;

public class NotificationsSettings implements DatePickerDialog.OnDateSetListener{

    Context context;

    public NotificationsSettings(Context context)
    {
       this.context = context;
    }


    @Override
    public void onDateSet(DatePicker view, int DAY_OF_WEEK, int month, int year) {



        Toast.makeText(context, "You will be notified fortnightly \n"+"from the date selected."
                                     , Toast.LENGTH_LONG).show();

    }


}
