package com.example.popularmovies

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.popularmovies.activity.MainActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyTestSuite {
    @Test
    fun testActivity (){
        Log.i("Tag","Helloe")
        //val scenario = launchActivity<MainActivity>
    }
}