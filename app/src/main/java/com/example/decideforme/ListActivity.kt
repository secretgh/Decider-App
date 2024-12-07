package com.example.decideforme

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.decideforme.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    var options = ArrayList<String>()
    private lateinit var activityListBinding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityListBinding = ActivityListBinding.inflate(layoutInflater)
        val view = activityListBinding.root
        setContentView(view)

        //Get list of options from decideActivity
        options = intent.getStringArrayListExtra("options") as ArrayList<String>
        var listAdapter = ListAdpater(options, activityListBinding.etOption)
        Log.d("Options", options.toString())
        //Update adapter for recyclerview
        activityListBinding.rvOptions.adapter = listAdapter
        activityListBinding.rvOptions.layoutManager = LinearLayoutManager(this)
        activityListBinding.rvOptions.setHasFixedSize(true)


        activityListBinding.btnToDecide.setOnClickListener {
            //Update list
            var returnData = Intent()
            returnData.putStringArrayListExtra("options",options)

            setResult(Activity.RESULT_OK, returnData)
            //End activity
            finish()
        }

        activityListBinding.btnEnter.setOnClickListener {
            val title = activityListBinding.etOption.text.toString()
            if(title.isNotBlank() && !options.contains(title)){
                options.add(title)
                activityListBinding.etOption.text.clear()
                options.sort()
                listAdapter.notifyDataSetChanged()
            }
        }

        activityListBinding.etOption.setOnKeyListener( View.OnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP){
                val title = activityListBinding.etOption.text.toString()
                if(title.isNotBlank() && !options.contains(title)){
                    options.add(title)
                    activityListBinding.etOption.text.clear()
                    options.sort()
                    listAdapter.notifyDataSetChanged()
                }
                return@OnKeyListener true
            }
            false
        })
    }

    override fun finish() {
        super.finish()
    }
}
