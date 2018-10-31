package com.semicolon.garage.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.semicolon.garage.R;
import com.semicolon.garage.models.UnReadeModel;
import com.semicolon.garage.models.UserModel;
import com.semicolon.garage.preferences.Preferences;
import com.semicolon.garage.tags.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class MyFireBaseMessagingInstanceId extends FirebaseMessagingService {
    Preferences preferences = Preferences.getInstance();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String,String> map = remoteMessage.getData();
        for (String key:remoteMessage.getData().keySet())
        {
            Log.e("key =",key);
            Log.e("value =",remoteMessage.getData().get(key));

        }

        ManageNotification(getUserModel(),getSession(),map);

    }

    private void ManageNotification(final UserModel userModel, final String session, final Map<String, String> map) {


        if (session.equals(Tags.session_login))
        {
            String curr_id = userModel.getUser_id();
            String to_id = map.get("to_user_id");
            if (curr_id.equals(to_id))
            {
                new Handler(Looper.getMainLooper())
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateNotification(userModel,session,map);

                            }
                        },100);
            }

        }
    }

    private void CreateNotification(UserModel userModel, String session, final Map<String, String> map) {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        final String sound_path = "android.resource://"+getPackageName()+"/"+ R.raw.not;

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {

            final int notifyID = 1;
            String CHANNEL_ID = "my_channel_01";
            CharSequence name ="my_channel_name";
            String notPath = "android.resource://"+getPackageName()+"/"+ R.raw.not;
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            mChannel.setShowBadge(true);
            mChannel.setSound(Uri.parse(notPath),new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build());

            final Notification.Builder notification = new Notification.Builder(this)
                    .setContentTitle(map.get("main_title"))
                    .setContentText(map.get("message_content"))
                    .setSmallIcon(R.mipmap.ic_launcher2)
                    .setChannelId(CHANNEL_ID);
            final NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);


            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    notification.setLargeIcon(bitmap);
                    mNotificationManager.notify(notifyID , notification.build());
                    EventBus.getDefault().post(new UnReadeModel());
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(this).load(Tags.IMAGE_URL+map.get("image")).into(target);

        }else
            {

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        builder.setLargeIcon(bitmap);
                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                        builder.setSound(Uri.parse(sound_path));
                        builder.setContentText(map.get("message_content"));
                        builder.setContentTitle(map.get("main_title"));
                        notificationManager.notify(1,builder.build());
                        EventBus.getDefault().post(new UnReadeModel());

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL)+map.get("image")).into(target);

            }
    }


    private UserModel getUserModel()
    {
        UserModel userModel =preferences.getUserData(this);
        return userModel;
    }

    private String getSession ()
    {
        String session = preferences.getSession(this);
        return session;
    }


}
