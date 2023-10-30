package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import rconnect.retvens.technologies.databinding.FragmentContractDetailsChildBinding
import java.io.File


class ContractDetailsChildFragment : Fragment(), ContractPDFAdapter.OnViewListener {
    private lateinit var binding : FragmentContractDetailsChildBinding

    private lateinit var scaleGestureDetector: ScaleGestureDetector

    val pdfList = ArrayList<ContractPDFData>()
    lateinit var pdfAdapter: ContractPDFAdapter
    private var scaleFactor = 1.0f
//    private lateinit var pdfPreview: ImageView
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

        binding.selectCard.setOnClickListener {
            pickFileFromGallery()
        }
        binding.pdfRecycler.layoutManager = LinearLayoutManager(requireContext())

//        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())
//
//        binding.pdfPreview.setOnTouchListener { v, event ->
//            scaleGestureDetector.onTouchEvent(event)
//            true // Consume the touch event
//        }
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

        binding.pdfPageCount.text = "$currentPage/$pageCount"
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

                binding.pdfPageCount.text = "$currentPage/$pageCount"
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

                binding.pdfPageCount.text = "$currentPage/$pageCount"
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

}