package com.example.kotlinecruddemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    internal lateinit var queue: RequestQueue
    internal lateinit var adapter: Userdataadapter

    internal var getUserDataList: ArrayList<UserModelData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        queue=Volley.newRequestQueue(this)

        swipeRefresh.setOnRefreshListener { setupRecyclerView() }
        setupRecyclerView()
        adddata.setOnClickListener({
            v->
            val i= Intent(v.context,AddDataActivity::class.java)
            v.context.startActivity(i)
        })
    }


    private fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        userrecyclerview.layoutManager = layoutManager
        //recyclerView.setLayoutManager(GridLayoutManager(this, 3))
        progressBar.setVisibility(View.VISIBLE)
         getData()


    }


    fun getData(){
        getUserDataList.clear()
        val stringRequest = object : StringRequest(Method.POST,
            getString(R.string.baseurl)+"getdata.php", Response.Listener { response ->
                Log.e("demo==>>",response.toString())
                try {
                    val jsonObject = JSONObject(response)

                    val dataArray = jsonObject.getJSONArray("data")
                    for (i in 0 until dataArray.length()) {
                        progressBar.setVisibility(View.GONE)
                        val dataobj = dataArray.getJSONObject(i)
                        val m=UserModelData(dataobj.getString("id"),dataobj.getString("Name"),dataobj.getString("email"),dataobj.getString("address"))
                        getUserDataList.add(m)
                    }
                    val adapter= Userdataadapter(this, getUserDataList)
                    userrecyclerview.adapter = adapter
                    swipeRefresh.isRefreshing = false


                } catch (e: Exception) {
                    Toast.makeText(this, "Exception error", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()

                    swipeRefresh.isRefreshing = false
                }
            }, Response.ErrorListener {

                swipeRefresh.isRefreshing = false
                Toast.makeText(this,
                    "Something is wrong",
                    Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
 /*               params["page"] = "1"
                params["exam_type"] ="m"*/

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
