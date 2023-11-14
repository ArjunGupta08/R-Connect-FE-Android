package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImageCategoryDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImagesCategoryAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectImagesDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectRoomImagesAdapter
import rconnect.retvens.technologies.databinding.FragmentAddImagesBinding

class AddImagesFragment : Fragment(),
    ImagesCategoryAdapter.OnItemClickListener {
    private lateinit var binding : FragmentAddImagesBinding

    private var imageUri: Uri?= null
    private var PICK_IMAGE_REQUEST_CODE : Int = 0
    private var selectedImagesList:ArrayList<ImageCategoryDataClass> = ArrayList()
    var isImgCategoryLayoutVisible = true
    private  var positions: Int = 0

    lateinit var imagesCategoryAdapter: ImagesCategoryAdapter
    lateinit var selectRoomImagesAdapter: SelectRoomImagesAdapter

    var uriList = ArrayList<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddImagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imagesRecycler.layoutManager = LinearLayoutManager(requireContext())


        binding.addImgCategory.setOnClickListener {
            // Create a new category item with a unique identifier (you can adjust this based on your requirements)
           imagesCategoryAdapter.addEmptyItem("room")
        }



        imagesCategoryAdapter = ImagesCategoryAdapter(requireContext(), selectedImagesList)
        binding.imagesRecycler.adapter = imagesCategoryAdapter

        imagesCategoryAdapter.setOnItemClickListener(this)
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null){
            imageUri = data.data!!
            if (imageUri != null) {
                try {
                    // Check if there is an existing item at the specified position
                    if (positions < selectedImagesList.size) {
                        // Update the existing item at the specified position
                        val existingItem = selectedImagesList[positions]
                        existingItem.imageList.add(imageUri!!)
                    } else {
                        // Add a new item to the list
                        val newImageCategory = ImageCategoryDataClass("room", arrayListOf(imageUri!!))
                        selectedImagesList.add(newImageCategory)
                    }

                    // Log the contents of selectedImagesList
                    Log.e("list", selectedImagesList.toString())

                    // Notify the adapter that the data at the specified position has changed
                    imagesCategoryAdapter.notifyItemChanged(positions)
                    isImgCategoryLayoutVisible = !isImgCategoryLayoutVisible

                } catch (e: RuntimeException) {
                    Log.d("cropperOnPersonal", e.toString())
                } catch (e: ClassCastException) {
                    Log.d("cropperOnPersonal", e.toString())
                }
            }
        }
    }




    override fun onAddRoomImage(position: Int) {
        openGallery()
        positions = position
        Log.e("postion",position.toString())
    }


}