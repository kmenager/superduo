package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.Utilies;

/**
 * Created by kevinmenager on 19/01/16.
 */
public class TodayWidgetService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TodayWidgetService() {
        super("TodayWidgetService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                barqsoft.footballscores.widget.TodayWidget.class));


        Log.d("KM", "start");
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date(System.currentTimeMillis());

        String[] fragmentdate = new String[1];
        fragmentdate[0] = mformat.format(currentDate);
        Cursor cursor = getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, " >= ?",
                fragmentdate, null);

        if (cursor == null) {
            return;
        }

        Log.d("KM", fragmentdate[0] + " not null " + cursor.getCount());
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }



        String teamHome = cursor.getString(ScoresAdapter.COL_HOME);
        String teamAway = cursor.getString(ScoresAdapter.COL_AWAY);
        String matchTime = cursor.getString(ScoresAdapter.COL_MATCHTIME);

        String score = Utilies.getScores(cursor.getInt(ScoresAdapter.COL_HOME_GOALS), cursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
        cursor.close();
        Log.d("KM", teamAway + " " + teamHome + " " + matchTime + " " + score);
        for (int appWidgetId : appWidgetIds) {
            int layoutId = R.layout.widget;

            RemoteViews views = new RemoteViews(getPackageName(), layoutId);

            // Add the data to the RemoteViews
            views.setImageViewResource(R.id.away_crest, Utilies.getTeamCrestByTeamName(
                    teamAway));
            views.setImageViewResource(R.id.home_crest, Utilies.getTeamCrestByTeamName(
                    teamHome));

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                //setRemoteContentDescription(views, description);
            }
            views.setTextViewText(R.id.away_name, teamAway);
            views.setTextViewText(R.id.home_name, teamHome);
            views.setTextViewText(R.id.score_textview, score);
            views.setTextViewText(R.id.data_textview, matchTime);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.linearContainer, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
