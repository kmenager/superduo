package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import barqsoft.footballscores.service.MyFetchService;

/**
 * Created by kevinmenager on 19/01/16.
 */
public class TodayWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, barqsoft.footballscores.widget.TodayWidgetService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        context.startService(new Intent(context, barqsoft.footballscores.widget.TodayWidgetService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (MyFetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            context.startService(new Intent(context, barqsoft.footballscores.widget.TodayWidgetService.class));
        }
    }
}
