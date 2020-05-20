package com.example.kotlinecruddemo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_editedata.*
import kotlinx.android.synthetic.main.userlayout.view.*
import org.json.JSONObject
import android.os.Bundle




class Userdataadapter(val context: Context, val userlistdata: ArrayList<UserModelData>) : RecyclerView.Adapter<Userdataadapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.userlayout, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return  userlistdata.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val userlistdataData=userlistdata[position]
        holder.setData(userlistdataData,position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var currentUserlist: UserModelData? = null
        var currentPosition: Int = 0



        init {

            itemView.delete_data.setOnClickListener({
                v->
                deleteData(currentUserlist!!.userid)
                userlistdata.removeAt(currentPosition)
                notifyDataSetChanged()
            })
itemView.edite_data.setOnClickListener({v ->
    val i= Intent(context, EditeDataActivity::class.java)
    val countryData = userlistdata.get(currentPosition)

  i.putExtra("getlistData",countryData)
    context.startActivity(i)
})

        }
        fun setData(userlistdataData: UserModelData?, pos: Int) {


            userlistdataData?.let {
                itemView.myname.text = userlistdataData.username
                itemView.myemail.text = userlistdataData.useremail
                itemView.myaddress.text = userlistdataData.useraddress


            }
            this.currentUserlist = userlistdataData
            this.currentPosition = pos
        }
    }



    fun deleteData(uid:String){
        val stringRequest = object : StringRequest(Method.POST,
            "http://bhssolution.com/data/api/kotlinapi/delete.php", Response.Listener { response ->
                Log.e("demo==>>",response.toString())
                try {


                    val jsonObject = JSONObject(response)


                    Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show()




                } catch (e: Exception) {
                    Toast.makeText(context, "Exception error"+e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()


                }
            }, Response.ErrorListener {


                Toast.makeText(context,
                    "Something is wrong",
                    Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = uid


                return params

            }
            /* @Throws(AuthFailureError::class)
             override fun getHeaders(): Map<String, String> {
                 val params = HashMap<String, String>()


                 return params
             }*/
        }
        var queue: RequestQueue =Volley.newRequestQueue(context)
        queue.add(stringRequest)
    }
}

