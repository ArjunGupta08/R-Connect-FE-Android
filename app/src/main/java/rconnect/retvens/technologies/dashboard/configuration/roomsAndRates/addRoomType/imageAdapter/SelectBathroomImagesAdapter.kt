package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import rconnect.retvens.technologies.R

class SelectBathroomImagesAdapter(val context: Context, private val itemList: ArrayList<SelectImagesDataClass>) : RecyclerView.Adapter<SelectBathroomImagesAdapter.ViewHolder>() {

    val VIEW_TYPE_DYNAMIC_ITEM = 1
    val VIEW_TYPE_STATIC_ITEM = 2

    private var onItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
    interface OnItemClickListener {
        fun onAddBathroomImage()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View
        if (viewType == VIEW_TYPE_DYNAMIC_ITEM) {
            view = inflater.inflate(R.layout.item_image, parent, false)
        } else if (viewType == VIEW_TYPE_STATIC_ITEM) {
            view = inflater.inflate(R.layout.item_add_image, parent, false)
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (position < itemList.size) {
            // Handle dynamic items
            val item = itemList[position]
            holder.image.setImageURI(item.image)

            holder.deleteImage.setOnClickListener {
                itemList.remove(item)
                notifyDataSetChanged()
            }
        } else if (position == itemList.size) {
            // Handle the static item
            holder.addImage.setOnClickListener {
                onItemClickListener?.onAddBathroomImage()
            }
        }

    }

    override fun getItemCount(): Int {
        return if (itemList.size in 0..9) itemList.size + 1 else 10
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.img)
        val deleteImage = itemView.findViewById<ImageView>(R.id.deleteImage)
        val addImage = itemView.findViewById<ConstraintLayout>(R.id.addImageView)
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == itemList.size) {
            VIEW_TYPE_STATIC_ITEM
        } else {
            VIEW_TYPE_DYNAMIC_ITEM
        }
    }
//        override fun getItemViewType(position: Int): Int {
//
//           return if (position === itemList.size) R.layout.item_add_image else R.layout.item_image
//
//        }

//    override fun getItemViewType(position: Int): Int {
//        // Check if the list size is 3 or 4 and handle the position accordingly
//        return if (itemList.size in 0..9) {
//            when (position) {
//                0 -> VIEW_TYPE_STATIC_ITEM
////                9 -> VIEW_TYPE_DYNAMIC_ITEM
//                else -> VIEW_TYPE_DYNAMIC_ITEM
//            }
//        } else {
//            when (position) {
//                0 -> VIEW_TYPE_STATIC_ITEM
//                else -> VIEW_TYPE_DYNAMIC_ITEM
//            }
//        }
//    }

}