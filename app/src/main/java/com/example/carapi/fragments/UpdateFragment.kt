package com.example.carapi.fragments

import UploadRequestBody
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.carapi.R
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.fragment_delete.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.madebyText
import kotlinx.android.synthetic.main.fragment_post.nameText
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
private var job : Job?=null
private var selectedImageUri: Uri? = null
class UpdateFragment : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updateImageView.setOnClickListener {
            openImageChooser()
        }
        updateButton.setOnClickListener {
            val name = nameText.text
            val madeby = madebyText.text
            val id = editTextUpdateId .text
            if (name.isEmpty()||madeby.isEmpty()||id.isEmpty()){

                nameText.error="Name Required"
                madebyText.error ="MadeBy required"
                return@setOnClickListener
            }else{
                updateData()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    updateImageView.setImageURI(selectedImageUri)
                }
            }
        }
    }
    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }
    private fun updateData(){
        if (selectedImageUri == null) {
            val idd = editTextUpdateId.text.toString().toInt()
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = CarApi().updateDataNoImage(
                    id =idd,
                    name = nameText.text.toString(),
                    madeby = madebyText.text.toString()
                )
                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        Toast.makeText(context,"Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }else{
            val contentResolver= requireActivity().contentResolver
            val cacheDir = getActivity()?.getCacheDir()
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val body = UploadRequestBody(file, "image")
            val idd = editTextUpdateId.text.toString().toInt()
            job = CoroutineScope(Dispatchers.IO).launch {
                val response = CarApi().updateData(
                    id =idd,
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        body
                    ),
                    RequestBody.create(MediaType.parse("multipart/form-data"), nameText.text.toString()),
                    RequestBody.create(MediaType.parse("multipart/form-data"), madebyText.text.toString())
                )
                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        Toast.makeText(context,"Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}