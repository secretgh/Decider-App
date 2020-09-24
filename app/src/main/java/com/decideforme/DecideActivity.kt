package com.decideforme

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_decide.*
import kotlin.random.Random

class DecideActivity : AppCompatActivity() {
    var options = ArrayList<String>()
    var lastOption = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decide)

        //Retrieve set of options from preferences and convert to array
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        var savedOptions = sharedPref.getStringSet("options", null)
        lastOption = sharedPref.getString("lastOption", "")!!

        if(savedOptions != null){
            var temp = ArrayList<String>()
            temp.addAll(savedOptions)
            temp.sort()
            options = temp
        }

        btnDecide.setOnClickListener {
            if(options.isEmpty()) {
                Toast.makeText(
                    this,
                    "Currently your options are empty, add more with the option menu below.",
                    Toast.LENGTH_LONG
                ).show()
            }else{
                val intent = Intent(this, ResultActivity::class.java).apply {
                    var result = Random.nextInt(options.size)

                    if(options[result] == lastOption){
                        while(options[result] == lastOption){
                            result = Random.nextInt(options.size)
                        }
                    }

                    lastOption = options[result]
                    saveLastOption()

                    this.putExtra("result", options[result])
                    startActivityForResult(this, 2)
                }
            }
        }

        btnToListMenu.setOnClickListener {
            //Toast.makeText(this,"Currently a work in progress, this will take you to the list of items", Toast.LENGTH_LONG).show()
            val intent = Intent(this, ListActivity::class.java).apply {
                this.putStringArrayListExtra("options", options)
                startActivityForResult(this, 1)
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        saveOptions()
        saveLastOption()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK && data != null){
                //get list, update list
                var newOptions = data.getStringArrayListExtra("options")
                options = newOptions
                saveOptions()
            }
        }
    }

    private fun saveOptions(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putStringSet("options", options.toSet())
            apply()
        }
    }

    private fun saveLastOption(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString("lastOptions", lastOption)
            apply()
        }
    }
}