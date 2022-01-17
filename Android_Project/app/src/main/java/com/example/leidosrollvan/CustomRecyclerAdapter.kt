package com.example.leidosrollvan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CustomRecyclerAdapter(private val businessList : ArrayList<Business>,
private val businessIdList : ArrayList<String>) : RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_list_item,
        parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = businessList[position]
        val currentID = businessIdList[position]

        val mRef = FirebaseDatabase.getInstance().getReference("Business Images")
        mRef.child(currentID).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val uri = snapshot.getValue(BusinessImage::class.java)!!.mImageUrl

                    holder.businessName.text = currentItem.businessName
                    holder.businessContact.text = currentItem.businessMobile
                    Picasso.with(holder.businessImage.context).load(uri).into(holder.businessImage)
                }else {
                    holder.businessName.text = currentItem.businessName
                    holder.businessContact.text = currentItem.businessMobile
                    holder.businessImage.setImageResource(R.drawable.ic_baseline_image_not_supported_24)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Image retrieval error", error.message)
            }
        })
    }

    override fun getItemCount(): Int {
        return businessList.size
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val businessName : TextView = itemView.findViewById(R.id.nameRecyclerItem)
        val businessContact : TextView = itemView.findViewById(R.id.contactRecyclerItem)
        val businessImage : ImageView = itemView.findViewById(R.id.imageRecyclerItem)

    }
}