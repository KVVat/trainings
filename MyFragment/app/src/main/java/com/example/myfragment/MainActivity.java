package com.example.myfragment;

import android.os.Bundle;

import com.example.myfragment.ui.main.MainFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            FragmentManager fManager = getSupportFragmentManager();
            FragmentTransaction fTransaction = fManager.beginTransaction();
            fTransaction.addToBackStack(null);
            int count=0;
            fTransaction.replace(R.id.container,MainFragment.newInstance(count));
            fTransaction.commit();
        }
    }
}
