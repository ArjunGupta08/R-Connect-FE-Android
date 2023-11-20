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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.Api.configurationApi.SingleConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImageCategoryDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImagesCategoryAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectImagesDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectRoomImagesAdapter
import rconnect.retvens.technologies.databinding.FragmentAddImagesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.prepareFilePart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import www.sanju.motiontoast.MotionToast

class AddImagesFragment : Fragment(),
    ImagesCategoryAdapter.OnItemClickListener {
    private lateinit var binding : FragmentAddImagesBinding

    private var imageUri: Uri?= null
    private val PICK_IMAGE_REQUEST_CODE = 123
    private val MAX_IMAGE_LIMIT = 5
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



    private fun pickMultipleImages() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            if (data.clipData != null) {
                // Multiple images are selected
                val clipData = data.clipData
                val selectedImages = ArrayList<Uri>()

                for (i in 0 until clipData!!.itemCount) {
                    val imageUri = clipData.getItemAt(i).uri
                    selectedImages.add(imageUri)
                }

                // Process the selected images
                processSelectedImages(selectedImages)
            } else if (data.data != null) {
                // Single image is selected
                val imageUri = data.data
                processSelectedImages(listOfNotNull(imageUri))
            }
        }
    }

    private fun processSelectedImages(selectedImages: List<Uri>) {
        // Process each selected image
        for (imageUri in selectedImages) {
            processImageUri(imageUri)
        }
    }

    private fun processImageUri(imageUri: Uri) {
        try {
            // Your existing logic to handle imageUri
            // Check if there is an existing item at the specified position
            if (positions < selectedImagesList.size) {
                // Update the existing item at the specified position
                val existingItem = selectedImagesList[positions]
                existingItem.imageList.add(imageUri)
            } else {
                // Add a new item to the list
                val newImageCategory = ImageCategoryDataClass("room", arrayListOf(imageUri))
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




    override fun onAddRoomImage(position: Int) {
        pickMultipleImages()
        positions = position
        Log.e("postion",position.toString())
    }

    override fun setImages(imageCategoryDataClass: ImageCategoryDataClass) {
        Log.e("imageCategoryDataClass",imageCategoryDataClass.toString())
        uploadImages(imageCategoryDataClass)
    }

    private fun uploadImages(imageCategoryDataClass: ImageCategoryDataClass) {
        imageCategoryDataClass.imageList.forEach { imageUri ->

            val propertyImage = prepareFilePart(imageUri, "hotelImage", requireContext())
            val userId = UserSessionManager(requireContext()).getUserId().toString()
            val propertyId = UserSessionManager(requireContext()).getPropertyId().toString()

            Log.e("user",userId.toString())
            Log.e("property",propertyId.toString())

            val uploadRoomImage = OAuthClient<ChainConfiguration>(requireContext())
                .create(ChainConfiguration::class.java)
                .uploadPropertyImages( userId,propertyId ,createPartFromString(imageCategoryDataClass.imageType),propertyImage!!)

            uploadRoomImage.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()!!
                        Toast.makeText(requireContext(),responseData.message,Toast.LENGTH_SHORT).show()
                        Log.e("res", responseData?.message.toString())
                    } else {
                        Log.e("res", "Error: ${response.code().toString()}")
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    Log.e("res", "Failure: ${t.message}")
                }
            })
        }
    }

    // Helper method to create RequestBody from a string
    private fun createPartFromString(string: String): RequestBody {
        return string.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }


}