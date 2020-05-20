package com.example.kotlinecruddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_editedata.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.ArrayList
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.activity_adddata.*
import kotlinx.android.synthetic.main.activity_editedata.closeActivity
import kotlinx.android.synthetic.main.activity_editedata.submitdata


class EditeDataActivity : AppCompatActivity() {

    internal lateinit var queue: RequestQueue
    internal lateinit var g: Intent;
    internal lateinit var  userdata: UserModelData
    internal var getUserDataList: ArrayList<UserModelData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editedata)
        g=intent
        queue= Volley.newRequestQueue(this)
        submitdata.setOnClickListener({v->
            senData()
        })
//        Log.e("currentlist",g.getStringExtra("getlistData"))
        closeActivity.setOnClickListener({
            v->
            finish()
        })

        userdata = g.getSerializableExtra("getlistData") as UserModelData

        Log.e("currentlist->",userdata.toString())
        ename.setText(userdata.username)
        eemail.setText(userdata.useremail)
        eaddress.setText(userdata.useraddress)
    }

    fun senData(){
        if(ename.text.length==0){
           showToast("Enter Your Name")
        }else if(eemail.text.length==0){
            showToast("Enter Your Email")
        }else if(eaddress.text.length==0){
            showToast("Enter Your Address")
        }else{
            saveData()
        }
    }



    fun saveData(){

        val stringRequest = object : StringRequest(Method.POST,
            getString(R.string.baseurl)+"updateuser.php", Response.Listener { response ->
                Log.e("demo==>>",response.toString())
                try {


                    val jsonObject = JSONObject(response)



                    
                    showToast(jsonObject.getString("msg"))


                    val i= Intent(this,MainActivity::class.java)
                     startActivity(i)

                } catch (e: Exception) {
                    Toast.makeText(this, "Exception error"+e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()


                }
            }, Response.ErrorListener {


                Toast.makeText(this,
                    "Something is wrong",
                    Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = userdata.userid
                params["name"] = ename.text.toString()
                params["email"] = eemail.text.toString()
                params["address"] = eaddress.text.toString()

                return params

            }
            /* @Throws(AuthFailureError::class)
             override fun getHeaders(): Map<String, String> {
                 val params = HashMap<String, String>()


                 return params
             }*/
        }
        queue.add(stringRequest)

    }

}
