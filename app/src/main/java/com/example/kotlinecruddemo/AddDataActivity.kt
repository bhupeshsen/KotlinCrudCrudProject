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
import kotlinx.android.synthetic.main.activity_adddata.*
import kotlinx.android.synthetic.main.activity_editedata.*
import kotlinx.android.synthetic.main.activity_editedata.closeActivity
import kotlinx.android.synthetic.main.activity_editedata.submitdata
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class AddDataActivity : AppCompatActivity() {
    internal lateinit var queue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adddata)
        queue= Volley.newRequestQueue(this)
        submitdata.setOnClickListener({v->
            senData()
        })
        closeActivity.setOnClickListener({
            v->
            finish()
        })

    }

    fun senData(){
        if
                (name.text.length==0){
           showToast("Enter Your Name")
        }else if(email.text.length==0){
            showToast("Enter Your Email")
        }else if(address.text.length==0){
            showToast("Enter Your Address")
        }else{
            saveData()
        }
    }



    fun saveData(){

        val stringRequest = object : StringRequest(Method.POST,
            getString(R.string.baseurl)+"adduser.php", Response.Listener { response ->
                Log.e("demo==>>",response.toString())
                try {


                    val jsonObject = JSONObject(response)


                    showToast(jsonObject.getString("msg"))

                    val i= Intent(this,MainActivity::class.java)
                    startActivity(i)
                    finish()


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
                params["name"] = name.text.toString()
                params["email"] = email.text.toString()
                params["address"] = address.text.toString()

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
