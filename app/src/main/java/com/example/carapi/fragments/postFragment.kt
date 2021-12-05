package com.example.carapi.fragments

import UploadRequestBody
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carapi.R
import kotlinx.android.synthetic.main.fragment_post.*
import android.content.ContentResolver
import android.provider.OpenableColumns
import android.widget.Toast
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.fragment_get.*
import kotlinx.android.synthetic.main.row_layout.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

const val REQUEST_CODE_PICK_IMAGE = 101
private var selectedImageUri: Uri? = null
class postFragment : Fragment() {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        postImageView.setOnClickListener {
            openImageChooser()
        }
        postButton.setOnClickListener {
            var name = nameText.text
            var madeby = madebyText.text
            if (name.isEmpty()||madeby.isEmpty()){
                nameText.error="Name Required"
                madebyText.error ="MadeBy required"
                return@setOnClickListener
            }else{
                uploadImage()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
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
                    postImageView.setImageURI(selectedImageUri)
                }
            }
        }
    }
    fun ContentResolver.getFileName(fileUri: Uri): String {
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
    private fun uploadImage(){
        if (selectedImageUri == null) {

           var call = CarApi().postDataNoImage(
                  name = nameText.text.toString(),
                  madeby = madebyText.text.toString()
            )

            call.enqueue(object : Callback<RequestBody> {
                override fun onFailure(call: Call<RequestBody>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(
                    call: Call<RequestBody>,

                    response: Response<RequestBody>
                )
                {
                    if (response.isSuccessful){
                        response.body()?.let {
                        }
                    }
                }
            })
            Toast.makeText(context,"Posted",Toast.LENGTH_SHORT).show()
        }else{
            var contentResolver= requireActivity().contentResolver
            var cacheDir = getActivity()?.getCacheDir()
            val parcelFileDescriptor =
                contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

            val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
            val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            val body = UploadRequestBody(file, "image")

            CarApi().postData(
                MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    body
                ),
                RequestBody.create(MediaType.parse("multipart/form-data"), nameText.text.toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"), madebyText.text.toString())


            ).enqueue(object : Callback<RequestBody> {

                override fun onFailure(call: Call<RequestBody>, t: Throwable) {
                     t.printStackTrace()
                }

                override fun onResponse(
                    call: Call<RequestBody>,
                    response: Response<RequestBody>
                ) {
                    response.body()?.let {
                    }
                       }
            })
            Toast.makeText(context,"Posted", Toast.LENGTH_SHORT).show()
        }

    }

}