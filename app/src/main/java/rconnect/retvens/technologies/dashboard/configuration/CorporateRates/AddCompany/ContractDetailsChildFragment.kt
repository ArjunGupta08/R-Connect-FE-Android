package rconnect.retvens.technologies.dashboard.configuration.CorporateRates.AddCompany

import android.R.attr.data
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import rconnect.retvens.technologies.databinding.FragmentContractDetailsChildBinding
import java.io.File


class ContractDetailsChildFragment : Fragment() {
    private lateinit var binding : FragmentContractDetailsChildBinding

//    private lateinit var pdfPreview: ImageView
    private var pdfRenderer: PdfRenderer? = null
    private var pdfUri : Uri? = null

    @SuppressLint("Range")
    private val galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {

//                Log.d("Result", )
                    // Get the Uri of the selected file
                    val pdfUri: Uri = result.data?.data!!
                    val uriString = pdfUri.toString()
                    val myFile = File(uriString)
                    val path = myFile.absolutePath
                    var displayName: String? = null

                binding.selectCard.visibility = View.GONE
                binding.pdfCard.visibility = View.VISIBLE
                openPdfRenderer(pdfUri)

                    if (uriString.startsWith("content://")) {
                        var cursor: Cursor? = null
                        try {
                            cursor = requireContext().contentResolver.query(pdfUri, null, null, null, null)
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                binding.pdfName.text = displayName
                            }
                        } finally {
                            cursor!!.close()
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.name
                        binding.pdfName.text = displayName
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

        binding.selectPDF.setOnClickListener {
            pickFileFromGallery()
        }

        binding.deletePdf.setOnClickListener {
            pdfUri = null
            binding.pdfPreview.setImageBitmap(null)
            binding.selectCard.visibility = View.VISIBLE
            binding.pdfCard.visibility = View.GONE
        }
    }

    private fun pickFileFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "application/pdf"
        galleryResult.launch(intent)
    }

    private fun openPdfRenderer(uri: Uri) {
//        pdfRenderer?.close() // Close the previous PDF renderer, if any.

        val parcelFileDescriptor = requireContext().contentResolver.openFileDescriptor(uri, "r")
        val pdfRenderer = PdfRenderer(parcelFileDescriptor!!)

        // Assume that PDF has only one page.
        val page = pdfRenderer.openPage(0)

        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        binding.pdfPreview.setImageBitmap(bitmap)

        page.close()
        pdfRenderer.close()

        this.pdfRenderer = pdfRenderer
    }

}