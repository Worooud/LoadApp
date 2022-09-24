package com.udacity

import android.Manifest
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var status :String="Fail"
    private var fileName=""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createChannel(
            getString(R.string.channel_id),
            getString(R.string.notification_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))


        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.glideRB -> {URL= GlideUrl;fileName=glideRB.text.toString()}
                R.id.loadAppRB -> {URL=LoadAppUrl;fileName=loadAppRB.text.toString()}
                else -> { URL=RetrofitUrl;fileName= retrofitRB.text.toString()}
            }
        }

        custom_button.setOnClickListener {

            when (radioGroup.checkedRadioButtonId) {
                View.NO_ID ->
                    Toast.makeText(this, getString(R.string.select_the_file), Toast.LENGTH_SHORT).show()
                else -> {

                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        custom_button.buttonState = ButtonState.Loading
                        download()
                    } else {
                        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            PermissionInfo.PROTECTION_DANGEROUS);
                    }

                }

            }

        }





    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val action = intent.action

            custom_button.buttonState = ButtonState.Completed


            if (downloadID == id) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    val query = DownloadManager.Query()
                    query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
                    val manager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val cursor: Cursor = manager.query(query)
                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val fileStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            if (fileStatus == DownloadManager.STATUS_SUCCESSFUL) {
                                status=getString(R.string.success)

                                sendNotification()
                            } else {
                                status=getString(R.string.fail)
                                sendNotification()
                            }
                        }
                    }
                }
            }

        }
    }


    private fun download() {

        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)


        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.



    }


    private fun sendNotification(){
       notificationManager= ContextCompat.getSystemService(applicationContext,  NotificationManager::class.java
       ) as NotificationManager
        notificationManager.sendNotification(status,fileName,applicationContext)
    }

    companion object {
        private var URL = ""
        private const val CHANNEL_ID = "channelId"

      val  GlideUrl ="https://github.com/bumptech/glide/archive/refs/heads/master"
      val  LoadAppUrl="https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
      val  RetrofitUrl="https://github.com/square/retrofit/archive/refs/heads/master.zip"


    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(true)
                }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download File"


            val notificationManager = getSystemService(
                NotificationManager::class.java
            )

            notificationManager.createNotificationChannel(notificationChannel)


        }



    }


}
