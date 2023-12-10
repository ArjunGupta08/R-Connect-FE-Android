package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListPopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import rconnect.retvens.technologies.Api.CorporatesApi
import rconnect.retvens.technologies.Api.OAuthClient
import rconnect.retvens.technologies.Api.RetrofitObject
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment.RateType
import rconnect.retvens.technologies.dashboard.configuration.CorporateRates.CorporatesPartnersFragment
import rconnect.retvens.technologies.databinding.FragmentContractDetailsChildBinding
import rconnect.retvens.technologies.utils.UserSessionManager
import rconnect.retvens.technologies.utils.bottomSlideInAnimation
import rconnect.retvens.technologies.utils.prepareFilePart
import rconnect.retvens.technologies.utils.preparePdfPart
import rconnect.retvens.technologies.utils.rightInAnimation
import rconnect.retvens.technologies.utils.shakeAnimation
import rconnect.retvens.technologies.utils.showProgressDialog
import rconnect.retvens.technologies.utils.utilCreateDatePickerDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Date


class ContractDetailsChildFragment(val companyId:String) : Fragment(), ContractPDFAdapter.OnViewListener {
    private lateinit var binding : FragmentContractDetailsChildBinding
    var startDate: Date? = null
    var endDate: Date? = null
    lateinit var startDatePickerDialog: DatePickerDialog
    lateinit var endDatePickerDialog: DatePickerDialog
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private lateinit var progressDialog: Dialog

    val pdfList = ArrayList<ContractPDFData>()
    lateinit var pdfAdapter: ContractPDFAdapter
    private var scaleFactor = 1.0f
//    private lateinit var pdfPreview: ImageView
    private lateinit var roboto : Typeface
    private lateinit var robotoMedium : Typeface
    private var pdfRenderer: PdfRenderer? = null
    private var pdfUri : Uri? = null

    @SuppressLint("Range")
    private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

                    // Get the Uri of the selected file
                    val pdfUri: Uri = result.data?.data!!
                    val uriString = pdfUri.toString()
                    val myFile = File(uriString)
                    val path = myFile.absolutePath
                    var displayName: String? = null

                openPdfRenderer(pdfUri)
                    if (uriString.startsWith("content://")) {
                        var cursor: Cursor? = null
                        try {
                            cursor = requireContext().contentResolver.query(pdfUri, null, null, null, null)
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                                binding.pdfName.text = displayName
                                pdfList.add(ContractPDFData(displayName, pdfUri))
                            }
                        } finally {
                            cursor!!.close()
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.name
                        pdfList.add(ContractPDFData(displayName, pdfUri))
//                        binding.pdfName.text = displayName
                    }
                }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContractDetailsChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        roboto = ResourcesCompat.getFont(requireContext(), R.font.roboto)!!
        robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)!!

        binding.selectCard.setOnClickListener {
            pickFileFromGallery()
        }

        startDatePickerDialog =
            utilCreateDatePickerDialog(requireContext(), binding.checkIn) { date ->
                startDate = date
            }
        endDatePickerDialog =
            utilCreateDatePickerDialog(requireContext(), binding.checkOut) { date ->
                endDate = date
            }

        binding.CheckInLayout.setEndIconOnClickListener {
            endDate = null

            // Set the minimum date for the start date picker to be the current date
            startDatePickerDialog.datePicker.minDate = System.currentTimeMillis()
            startDatePickerDialog.show()
        }
        binding.CheckOutLayout.setEndIconOnClickListener {
            if (startDate != null) {
                endDatePickerDialog.datePicker.minDate = startDate!!.time
                endDatePickerDialog.show()
            }
        }

        binding.pdfRecycler.layoutManager = LinearLayoutManager(requireContext())

//        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
//
//        binding.pdfPreview.setOnTouchListener { v, event ->
//            scaleGestureDetector.onTouchEvent(event)
//            true // Consume the touch event
//        }

        val save = requireActivity().findViewById<CardView>(R.id.continueBtn)
        save.setOnClickListener {
            if (binding.checkIn.text!!.isEmpty()){
                shakeAnimation(binding.CheckInLayout,requireContext())
            }else if (binding.checkOut.text!!.isEmpty()){
                shakeAnimation(binding.CheckOutLayout,requireContext())
            }else if (pdfList.size == 0){
                shakeAnimation(binding.selectCard,requireContext())
            }else{
                progressDialog = showProgressDialog(requireContext())
                sendData()
            }

        }
    }

    private fun sendData() {
        if (pdfList.size == 1) {
            val contractPdf1 = preparePdfPart(pdfList[0].pdfUri, "contractPdf", requireContext())
            val sendData =
                OAuthClient<CorporatesApi>(requireContext()).create(CorporatesApi::class.java)
                    .addContract(
                        UserSessionManager(requireContext()).getUserId().toString(),
                        companyId,
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            binding.contactTerms.text.toString()
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            binding.checkIn.text.toString()
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            binding.checkOut.text.toString()
                        ),
                        contractPdf1!!
                    )

            sendData.enqueue(object : Callback<CompanyResponse?> {
                override fun onResponse(
                    call: Call<CompanyResponse?>,
                    response: Response<CompanyResponse?>
                ) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        val response = response.body()!!
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
                        changeChildFragment(CorporatePlanFragment(companyId))
                        val corporateRatePlanFag =
                            requireActivity().findViewById<TextView>(R.id.ratePlanCard)
                        corporateRatePlanFag.textSize = 18.0f
                        corporateRatePlanFag.typeface = robotoMedium
                        corporateRatePlanFag.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.secondary
                            )
                        )
                        val contractDetailsFag = requireActivity().findViewById<TextView>(R.id.contractDetailsFag)
                        contractDetailsFag.textSize = 14.0f
                        contractDetailsFag.typeface = roboto

                        val companyDetailsFrag =
                            requireActivity().findViewById<TextView>(R.id.companyDetailsFrag)
                        companyDetailsFrag.textSize = 14.0f
                        companyDetailsFrag.typeface = roboto

                        changeChildFragment(CorporatePlanFragment(companyId))

                        val addCompanyFragContainer =
                            requireActivity().findViewById<FrameLayout>(R.id.addCompanyFragContainer)

                        rightInAnimation(addCompanyFragContainer, requireContext())

                        companyDetailsFrag.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.corner_top_grey_background
                            )
                        )

                        contractDetailsFag.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.corner_top_grey_background
                            )
                        )
                        corporateRatePlanFag.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.corner_top_white_background
                            )
                        )
                    } else {
                        progressDialog.dismiss()
                        Log.e("error", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<CompanyResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.e("error", t.message.toString())
                }
            })
        } else if (pdfList.size == 2) {
            val contractPdf1 = preparePdfPart(pdfList[0].pdfUri, "contractPdf", requireContext())
            val contractPdf2 = preparePdfPart(pdfList[1].pdfUri, "contractPdf", requireContext())

            val sendData =
                OAuthClient<CorporatesApi>(requireContext()).create(CorporatesApi::class.java)
                    .addContract2(
                        UserSessionManager(requireContext()).getUserId().toString(),
                        companyId,
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            binding.contactTerms.text.toString()
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            binding.checkIn.text.toString()
                        ),
                        RequestBody.create(
                            "multipart/form-data".toMediaTypeOrNull(),
                            binding.checkOut.text.toString()
                        ),
                        contractPdf1!!,
                        contractPdf2!!
                    )

            sendData.enqueue(object : Callback<CompanyResponse?> {
                override fun onResponse(
                    call: Call<CompanyResponse?>,
                    response: Response<CompanyResponse?>
                ) {
                    if (response.isSuccessful) {
                        progressDialog.dismiss()
                        val response = response.body()!!
                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                            .show()
//                        replaceFragment(CorporatesPartnersFragment())
                        val corporateRatePlanFag =
                            requireActivity().findViewById<TextView>(R.id.ratePlanCard)
                        corporateRatePlanFag.textSize = 18.0f
                        corporateRatePlanFag.typeface = robotoMedium
                        corporateRatePlanFag.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.secondary
                            )
                        )
                        val contractDetailsFag = requireActivity().findViewById<TextView>(R.id.contractDetailsFag)
                        contractDetailsFag.textSize = 14.0f
                        contractDetailsFag.typeface = roboto

                        val companyDetailsFrag =
                            requireActivity().findViewById<TextView>(R.id.companyDetailsFrag)
                        companyDetailsFrag.textSize = 14.0f
                        companyDetailsFrag.typeface = roboto

                        changeChildFragment(CorporatePlanFragment(companyId))

                        val addCompanyFragContainer =
                            requireActivity().findViewById<FrameLayout>(R.id.addCompanyFragContainer)

                        rightInAnimation(addCompanyFragContainer, requireContext())

                        companyDetailsFrag.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.corner_top_grey_background
                            )
                        )

                        contractDetailsFag.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.corner_top_grey_background
                            )
                        )
                        corporateRatePlanFag.setBackgroundDrawable(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.corner_top_white_background
                            )
                        )
                    } else {
                        progressDialog.dismiss()
                        Log.e("error", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<CompanyResponse?>, t: Throwable) {
                    progressDialog.dismiss()
                    Log.e("error", t.message.toString())
                }
            })
        }





    }

    private fun pickFileFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "application/pdf"
        galleryResult.launch(intent)
    }

    private fun openPdfRenderer(uri: Uri) {

        pdfAdapter = ContractPDFAdapter(pdfList, requireContext())
        binding.pdfRecycler.adapter = pdfAdapter
        pdfAdapter.setOnClickListener(this)
        pdfAdapter.notifyDataSetChanged()

        binding.counterCard.isVisible = true

        var currentPage = 1
//        pdfRenderer?.close() // Close the previous PDF renderer, if any.

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r")
        val pdfRenderer = PdfRenderer(parcelFileDescriptor!!)

        // Get the total number of pages in the PDF
        val pageCount = pdfRenderer.pageCount

        // Assume that PDF has only one page.
        val page = pdfRenderer.openPage(0)

        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        binding.pdfPageCount.text = "$currentPage/${pageCount-1}"
        binding.pdfPreview.setImageBitmap(bitmap)

        page.close()
//        pdfRenderer.close()

        this.pdfRenderer = pdfRenderer

        binding.previousPage.setOnClickListener {

            if (currentPage >  1) {
                currentPage--

                val page = pdfRenderer.openPage(if (currentPage == 1){0}else{currentPage})

                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                binding.pdfPageCount.text = "$currentPage/${pageCount-1}"
                binding.pdfPreview.setImageBitmap(bitmap)
                page.close()
            }
        }
        binding.nextPage.setOnClickListener {

            if (currentPage < pageCount-1) {
                currentPage++

                val page = pdfRenderer.openPage(currentPage)

                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                binding.pdfPageCount.text = "$currentPage/${pageCount-1}"
                binding.pdfPreview.setImageBitmap(bitmap)
                page.close()
            }
        }
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(1.0f, 5.0f) // Limit zoom to a reasonable range

            // Apply the scale to the ImageView
            binding.pdfPreview.scaleX = scaleFactor
            binding.pdfPreview.scaleY = scaleFactor
            return true
        }
    }

    override fun onViewPdf(uri: Uri) {
        openPdfRenderer(uri)
    }

    override fun onDeletePdf() {
//        if (pdfList.isEmpty()){
            binding.counterCard.isVisible = false
//        }
        binding.pdfPreview.setImageBitmap(null)
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment !=null){
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.dashboardFragmentContainer,fragment)
            transaction.commit()
        }


    }

    fun changeChildFragment( fragment: Fragment){
        val childFragment: Fragment = fragment
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.addCompanyFragContainer,childFragment)
        transaction.commit()
    }

}