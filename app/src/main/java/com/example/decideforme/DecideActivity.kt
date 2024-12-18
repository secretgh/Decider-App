package com.example.decideforme

import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.decideforme.databinding.ActivityDecideBinding
import com.example.decideforme.databinding.DialogOptionsBinding
import kotlin.random.Random
import android.util.Pair as UtilPair

class DecideActivity : AppCompatActivity() {
    var options = ArrayList<String>()
    var lastOption = ""

    private lateinit var activityDecideBinding: ActivityDecideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityDecideBinding = ActivityDecideBinding.inflate(layoutInflater)

        setContentView(activityDecideBinding.root)

        //Retrieve set of options from preferences and convert to array
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        var savedOptions = sharedPref.getStringSet("options", null)
        lastOption = sharedPref.getString("lastOption", "")!!

        if(savedOptions != null){
            var temp = ArrayList<String>()
            temp.addAll(savedOptions)
            temp.sort()
            options = temp
        }else{

        }

        activityDecideBinding.btnDecide.setOnClickListener {
            if(options.isEmpty()) {
//                Toast.makeText(
//                    this,
//                    "Currently your options are empty, add more with the option menu below.",
//                    Toast.LENGTH_LONG
//                ).show()
                val view = DialogOptionsBinding.bind(LayoutInflater.from(this).inflate(R.layout.dialog_options, null))
                val builder = AlertDialog.Builder(this)
                builder.setView(view.root)
                val alert = builder.show()

                view.ivAccept.setOnClickListener {
                    //TODO: Add general options to list of food places and save them in local storage.
                    var genericOptions = arrayOf("Pizza", "Pasta", "Hamburger", "Soup", "Asian Cuisine", "Indian Cuisine", "Greek Cuisine", "Coffee")
                    options.addAll(genericOptions)
                    options.sort()
                    saveOptions()
                    Log.d("Dialog", options.toString())
                    alert.dismiss()
                }
                view.ivReject.setOnClickListener {
                    alert.dismiss()
                }

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

        activityDecideBinding.btnToListMenu.setOnClickListener {
            //Toast.makeText(this,"Currently a work in progress, this will take you to the list of items", Toast.LENGTH_LONG).show()
            val activityOptions = ActivityOptions.makeSceneTransitionAnimation(
                this,
                UtilPair.create(activityDecideBinding.btnToListMenu, "imageTransition")
            )
            val intent = Intent(this, ListActivity::class.java).apply {
                this.putStringArrayListExtra("options", options)
                startActivityForResult(this, 1, activityOptions.toBundle())
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
            if(resultCode == RESULT_OK && data != null){
                //get list, update list
                var newOptions = data.getStringArrayListExtra("options")
                if (newOptions != null) {
                    options = newOptions
                }
                saveOptions()
            }
        }
    }

    private fun saveOptions(){
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putStringSet("options", options.toSet())
            apply()
        }
    }

    private fun saveLastOption(){
        val sharedPref = getPreferences(MODE_PRIVATE) ?: return
        with(sharedPref.edit()){
            putString("lastOptions", lastOption)
            apply()
        }
    }
}