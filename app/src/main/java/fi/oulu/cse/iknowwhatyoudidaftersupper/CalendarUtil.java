package fi.oulu.cse.iknowwhatyoudidaftersupper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *  Utility for fetching calendar events
 */
public class CalendarUtil {
    private final static String TAG = "CalendarUtil";

    /*
     * Column to select in query
     */
    public static final String[] EVENT_PROJECTION = new String[] {
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events._ID,
            CalendarContract.Events.EVENT_LOCATION
    };

    // The indices for the projection array above.
    private static final int PROJECTION_DTSTART_INDEX = 0;
    private static final int PROJECTION_DTEND_INDEX = 1;
    private static final int PROJECTION_DESCR_INDEX = 2;
    private static final int PROJECTION_ID_INDEX = 3;
    private static final int PROJECTION_LOCATION_INDEX = 4;

    public static List<WeekViewEvent> getEvents(Context context, int year, int month) {
        ArrayList<WeekViewEvent> l = new ArrayList<>();
        Cursor cur = getEventsCursor(context, year, month);

        if (cur != null) {
            while (cur.moveToNext()) {

                long start = cur.getLong(PROJECTION_DTSTART_INDEX);
                long end = cur.getLong(PROJECTION_DTEND_INDEX);
                String description = cur.getString(PROJECTION_DESCR_INDEX);
                long id = cur.getLong(PROJECTION_ID_INDEX);
                String location = cur.getString(PROJECTION_LOCATION_INDEX);

                Calendar startCal = GregorianCalendar.getInstance();
                startCal.setTimeInMillis(start);

                Calendar endCal = GregorianCalendar.getInstance();
                endCal.setTimeInMillis(end);

                WeekViewEvent e = new WeekViewEvent(id, description, location, startCal, endCal);
                l.add(e);
            }
            cur.close();
        }

        return l;
    }

    private static long dateToMillis() {
        return 0;
    }

    /**
     * Query for any events happening right now
     * @return Cursor pointing to the events, null if none found
     */
    private static Cursor getEventsCursor(Context context, int year, int month) {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri  = CalendarContract.Events.CONTENT_URI;

        // Selection string to query for events either happening now or starting within the next 12 hours
        String selection = "(" + CalendarContract.Events.DTSTART + " BETWEEN ? and ?)";
        GregorianCalendar startDate = new GregorianCalendar(year, month, 1);
        String startTime = Long.toString(startDate.getTimeInMillis());

        GregorianCalendar endDate = new GregorianCalendar(year, month+1, 1);
        String endTime = Long.toString(endDate.getTimeInMillis());

        // Current time as arguments for the above selection string
        String[] selectionArgs = new String[] {
                startTime, endTime
        };

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) ==
                PackageManager.PERMISSION_GRANTED) {
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, CalendarContract.Events.DTSTART);
        }

        return cur;
    }

}
