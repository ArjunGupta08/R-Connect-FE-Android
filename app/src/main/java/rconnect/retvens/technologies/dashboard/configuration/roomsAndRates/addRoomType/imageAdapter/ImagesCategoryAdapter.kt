package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MultipartBody
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment
import rconnect.retvens.technologies.utils.prepareFilePart

class ImagesCategoryAdapter(val context: Context, private val itemList: ArrayList<ImageCategoryDataClass>) : RecyclerView.Adapter<ImagesCategoryAdapter.ViewHolder>(),
    SelectRoomImagesAdapter.OnItemClickListener {

    private var imageList:ArrayList<Uri> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null
    private var isAdd = false

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    interface OnItemClickListener {
        fun onAddRoomImage(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_img_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.textH.text = "Enter Category Name"

        holder.imagesRecycler.layoutManager = GridLayoutManager(context, 6)


        holder.selectImagesAdapter = SelectRoomImagesAdapter(context, item.imageList,position)
        holder.imagesRecycler.adapter = holder.selectImagesAdapter
        holder.selectImagesAdapter.setOnItemClickListener(this)

        holder.selectImagesAdapter.notifyDataSetChanged()

        holder.addImage.setOnClickListener {
            onItemClickListener?.onAddRoomImage(position)
        }


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
        val addImage = itemView.findViewById<TextView>(R.id.addImages)
        lateinit var selectImagesAdapter: SelectRoomImagesAdapter

    }

    fun updateData(newData: ArrayList<ImageCategoryDataClass>) {
        itemList.clear()
        Log.e("newData",newData.toString())
        itemList.addAll(newData)
        notifyDataSetChanged()
    }

    fun addEmptyItem(imageType: String) {
        val emptyItem = ImageCategoryDataClass(imageType)
        itemList.add(emptyItem)
        notifyItemInserted(itemList.size - 1) // Notify the adapter that a new item has been inserted
    }

    override fun onAddRoomImage() {

    }
}