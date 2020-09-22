package com.decideforme

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {
    var options = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        //Get list of options from decideActivity
        options = intent.getStringArrayListExtra("options") as ArrayList<String>
        var listAdapter = ListAdpater(options)

        //Update adapter for recyclerview
        rvOptions.adapter = listAdapter
        rvOptions.layoutManager = LinearLayoutManager(this)
        rvOptions.setHasFixedSize(true)

        btnToDecide.setOnClickListener {
            //Update list
            var returnData = Intent()
            returnData.putStringArrayListExtra("options",options)

            setResult(Activity.RESULT_OK, returnData)
            //End activity
            finish()
        }

        btnEnter.setOnClickListener {
            val title = etOption.text.toString()
            if(title.isNotBlank() && !options.contains(title)){
                options.add(title)
                etOption.text.clear()
                options.sort()
                listAdapter.notifyDataSetChanged()
            }
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up)
    }
}
