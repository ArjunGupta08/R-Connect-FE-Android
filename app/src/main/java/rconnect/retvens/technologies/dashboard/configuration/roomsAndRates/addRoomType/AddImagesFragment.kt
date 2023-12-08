package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.configurationApi.ChainConfiguration
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.configuration.properties.ViewPropertiesFragment
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImageCategoryDataClass
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImagesCategoryAdapter
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.ImagesDataResponse
import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.addRoomType.imageAdapter.SelectRoomImagesAdapter
import rconnect.retvens.technologies.databinding.FragmentAddImagesBinding
import rconnect.retvens.technologies.onboarding.ResponseData
import rconnect.retvens.technologies.utils.Const
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException

class AddImagesFragment(val idForImg : String, val isRoom : Boolean) : Fragment(),
    ImagesCategoryAdapter.OnItemClickListener {
    private lateinit var binding : FragmentAddImagesBinding

    private var imagesUri: ArrayList<Uri> = ArrayList()
    private val PICK_IMAGE_REQUEST_CODE = 123
    private val MAX_IMAGE_LIMIT = 5
    private var selectedImagesList:ArrayList<ImageCategoryDataClass> = ArrayList()
    private var responseImageList:ArrayList<ImagesDataResponse> = ArrayList()
    var isImgCategoryLayoutVisible = true
    private  var positions: Int = 0
    private var categories:String = ""

    lateinit var imagesCategoryAdapter: ImagesCategoryAdapter
    lateinit var selectRoomImagesAdapter: SelectRoomImagesAdapter

    private lateinit var progressDialog : Dialog

    var uriList = ArrayList<Uri>()

    private lateinit var continueBtnRoom : CardView

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

        continueBtnRoom = requireActivity().findViewById<CardView>(R.id.continueBtn)

        try {
            val addressAndContact = requireActivity().findViewById<TextView>(R.id.addressAndContacts)
            addressAndContact.setOnClickListener {
                shakeAnimation(binding.addImgCategory,requireContext())
            }

            val propertyProfile =  requireActivity().findViewById<TextView>(R.id.propertyProfile)
            propertyProfile.setOnClickListener {
                shakeAnimation(binding.addImgCategory,requireContext())
            }
        }catch (e:NullPointerException){
            Log.e("error",e.message.toString())
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
        imagesUri.addAll(selectedImages)
        uploadPropImages()
    }

    override fun onAddRoomImage(position: Int, category: String) {
        pickMultipleImages()
        positions = position
        categories = category
        Log.e("position",position.toString())
    }

    override fun setImages(imageCategoryDataClass: ImageCategoryDataClass) {
        Log.e("imageCategoryDataClass",imageCategoryDataClass.toString())
        if (isRoom!!){
            if (imageCategoryDataClass.imageList.size != 0)
            {
                uploadRoomImages(imageCategoryDataClass)
            }
        } else {
            if (imageCategoryDataClass.imageList.size != 0)
            {
                uploadPropImages()
            }

        }
    }

    private fun uploadPropImages() {
        progressDialog = showProgressDialog(requireContext())

        val userId = UserSessionManager(requireContext()).getUserId().toString()

        Log.e("user", userId)
        Log.e("property", idForImg.toString())

        val totalImages = imagesUri.size
        var uploadedImages = 0

        imagesUri.forEach { imageUri ->
            val propertyImage = prepareFilePart(imageUri, "hotelImage", requireContext())

            val uploadRoomImage = OAuthClient<ChainConfiguration>(requireContext())
                .create(ChainConfiguration::class.java)
                .uploadPropertyImages(
                    userId,
                    idForImg!!,
                    createPartFromString(categories),
                    propertyImage!!
                )
            uploadRoomImage.enqueue(object : Callback<ImagesDataResponse?> {
                override fun onResponse(
                    call: Call<ImagesDataResponse?>,
                    response: Response<ImagesDataResponse?>
                ) {
                    progressDialog.dismiss()  // Dismiss the dialog here to handle both success and failure cases

                    if (response.isSuccessful) {
                        val responseData = response.body()!!
                        responseImageList.add(responseData)
                        Log.e("res", responseData?.message.toString())
                    } else {
                        Log.e("res", "Error: ${response.code().toString()}")
                    }

                    uploadedImages++
                    if (uploadedImages == totalImages) {

                        responseImageList.forEach {
                            var imageList:ArrayList<String> = ArrayList()
                            imageList.add(it.data.image)
                            selectedImagesList.add(ImageCategoryDataClass(it.data.imageId,it.data.imageTag,imageList))
                            imagesCategoryAdapter.notifyItemChanged(positions)
                        }

                        // Set your click listener outside the loop
                        continueBtnRoom.setOnClickListener {
                            Toast.makeText(requireContext(), "Images Uploaded Successfully", Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()  // Dismiss the dialog here as well

                            Const.isAddingNewProperty = false

                            val childFragment: Fragment = ViewPropertiesFragment()
                            val transaction = requireActivity().supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.dashboardFragmentContainer, childFragment)
                            transaction.commit()
                        }
                    }
                }

                override fun onFailure(call: Call<ImagesDataResponse?>, t: Throwable) {
                    Log.e("res", "Failure: ${t.message}")
                }
            })
        }
    }


    private fun uploadRoomImages(imageCategoryDataClass: ImageCategoryDataClass) {
        progressDialog = showProgressDialog(requireContext())

        imagesUri.forEach { imageUri ->

            val propertyImage = prepareFilePart(imageUri, "roomImage", requireContext())
            val userId = UserSessionManager(requireContext()).getUserId().toString()
//            val propertyId = UserSessionManager(requireContext()).getPropertyId().toString()

            Log.e("user",userId.toString())
            Log.e("rooms",idForImg.toString())

            val uploadRoomImage = OAuthClient<ChainConfiguration>(requireContext())
                .create(ChainConfiguration::class.java)
                .uploadRoomsImages(userId,idForImg!! ,createPartFromString(categories),propertyImage!!)

            uploadRoomImage.enqueue(object : Callback<ResponseData?> {
                override fun onResponse(
                    call: Call<ResponseData?>,
                    response: Response<ResponseData?>
                ) {
                    progressDialog.dismiss()
                    if (response.isSuccessful) {
                        val responseData = response.body()!!
                        Toast.makeText(requireContext(),responseData.message,Toast.LENGTH_SHORT).show()
                        Log.e("res", responseData?.message.toString())

                        continueBtnRoom.setOnClickListener {
//                                Const.isAddingNewRoom = false

                                val childFragment: Fragment = ChargesAndRatesFragment(idForImg)
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.createRoomFragContainer,childFragment)
                                transaction.commit()
                        }

                    } else {
                        Log.e("res", "Error: ${response.code().toString()}")
                        progressDialog.dismiss()
                    }
                }

                override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                    Log.e("res", "Failure: ${t.message}")
                    progressDialog.dismiss()
                }
            })
        }
    }

    // Helper method to create RequestBody from a string
    private fun createPartFromString(string: String): RequestBody {
        return string.toRequestBody("multipart/form-data".toMediaTypeOrNull())
    }


}