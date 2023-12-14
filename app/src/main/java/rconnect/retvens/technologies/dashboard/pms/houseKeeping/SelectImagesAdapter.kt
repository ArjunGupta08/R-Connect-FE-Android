package rconnect.retvens.technologies.dashboard.pms.houseKeeping

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddImagesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.AddRoomTypeFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectImagesDataClass

class SelectImagesAdapter(val context: Context, private val itemList: ArrayList<SelectImagesDataClass>) : RecyclerView.Adapter<SelectImagesAdapter.ViewHolder>() {

    private var onItemClickListener: OnRemoveClickListener? = null
    fun setOnItemClickListener(listener: OnRemoveClickListener) {
        onItemClickListener = listener
    }
    interface OnRemoveClickListener {
        fun onRemoveImage(removedImageUri : Uri)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.image.setImageURI(itemList[position].image)

            holder.deleteImage.setOnClickListener {
                onItemClickListener?.onRemoveImage(itemList[position].image)
                notifyDataSetChanged()
            }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.img)
        val deleteImage = itemView.findViewById<ImageView>(R.id.deleteImage)
    }

}