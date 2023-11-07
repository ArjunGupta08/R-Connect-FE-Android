package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MultipartBody
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment
import rconnect.retvens.technologies.utils.prepareFilePart

class ImagesCategoryAdapter(val context: Context, private val itemList: ArrayList<ImageCategoryDataClass>) : RecyclerView.Adapter<ImagesCategoryAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.textH.text = item.imageType

        holder.imagesRecycler.layoutManager = GridLayoutManager(context, 6)

//        holder.imagePart.add(prepareFilePart(item.image[position], "hotelLogo", context)!!)

        holder.selectImagesAdapter = SelectRoomImagesAdapter(context, item.image)
//        holder.selectImagesAdapter.setOnItemClickListener(this)
        holder.imagesRecycler.adapter = holder.selectImagesAdapter
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textH = itemView.findViewById<TextView>(R.id.enterH)
        val imagesRecycler = itemView.findViewById<RecyclerView>(R.id.imagesRecycler)

        lateinit var selectImagesAdapter: SelectRoomImagesAdapter

    }
}