package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)



        var fileName=intent.getStringExtra("fileName")
        if (fileName != null) { fileNameTV.text=fileName}

        var status=intent.getStringExtra("status")
        if (status != null) {
            if(status.equals(getString(R.string.success))){
                statusTV.text=status
            }else{
                statusTV.text=status
                statusTV.setTextColor(Color.RED)
            }
        }


        fab.setOnClickListener{
            finish()
        }

    }




}


