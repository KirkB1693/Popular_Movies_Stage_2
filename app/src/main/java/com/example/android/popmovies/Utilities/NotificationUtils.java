package com.example.android.popmovies.Utilities;

public class NotificationUtils {



/*
    */
/*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 3004 is in no way significant.
     *//*

    private static final int MOVIE_NOTIFICATION_ID = 3004;

    */
/**
     * Constructs and displays a notification for the newly updated weather for today.
     *
     * @param context Context used to query our ContentProvider and use various Utility methods
     *//*

    public static void notifyUserOfNewMovies(Context context) {

        */
/* Build the URI for today's weather in order to show up to date data in notification
        Uri todaysWeatherUri = WeatherContract.WeatherEntry
                .buildWeatherUriWithDate(SunshineDateUtils.normalizeDate(System.currentTimeMillis()));*//*


        */
/*
         * The MAIN_FORECAST_PROJECTION array passed in as the second parameter is defined in our WeatherContract
         * class and is used to limit the columns returned in our cursor.

        Cursor todayWeatherCursor = context.getContentResolver().query(
                todaysWeatherUri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null);*//*




            Resources resources = context.getResources();


            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = "New MoviesModel Available";

                       */
/*
             * NotificationCompat Builder is a very convenient way to build backward-compatible
             * notifications. In order to use it, we provide a context and specify a color for the
             * notification, a couple of different icons, the title for the notification, and
             * finally the text of the notification, which in our case in a summary of today's
             * forecast.
             *//*

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            */
/*
             * This Intent will be triggered when the user clicks the notification. In our case,
             * we want to open PopMovies to the MainActivity to display the newly updated movies available.
             *//*

            Intent intent = new Intent(context, MainActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            */
/* MOVIE_NOTIFICATION_ID allows you to update or cancel the notification later on *//*

            notificationManager.notify(MOVIE_NOTIFICATION_ID, notificationBuilder.build());

            */
/*
             * Since we just showed a notification, save the current time. That way, we can check
             * next time the weather is refreshed if we should show another notification.
             *//*

            MoviePreferences.saveLastNotificationTime(context, System.currentTimeMillis());

    }

*/


}
