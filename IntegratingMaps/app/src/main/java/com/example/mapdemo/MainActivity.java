/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * The main activity of the API library demo gallery.
 * <p>
 * The main layout lists the demonstrated features, with buttons to launch them.
 */
public final class MainActivity extends Activity {


    protected void onCreate (Bundle  savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ArrayList<String> myStringArray = new ArrayList<String>();

        myStringArray.add("Demo Camera Functions");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray);

        ListView listView = (ListView) findViewById(R.id.listView1);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemClickListener(MyListClickHandlerFunction);
    }

    public AdapterView.OnItemClickListener MyListClickHandlerFunction  = new AdapterView.OnItemClickListener()
    {
        public void onItemClick( AdapterView<?> parent, View v, int position, long id)
        {
            if (position == 0)
                startActivity(new Intent(getApplication(), CameraDemoActivity.class));
        }
    };
}
