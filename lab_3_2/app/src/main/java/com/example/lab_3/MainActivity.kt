package com.example.lab_3

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.lab_3.databinding.ActivityMainBinding
import java.io.File

private const val TAKE_SHOT_REQUEST_CODE: Int = 1
private const val SEND_PICTURE_REQUEST_CODE: Int = 2

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fileProvider: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sendSelfi.visibility = View.INVISIBLE
        eventHandler()
    }

    private fun eventHandler() {
        binding.takeShot.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            fileProvider = FileProvider.getUriForFile(
                this, "com.example.lab_3",
                File.createTempFile(
                    "selfie.jpg",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            startActivityForResult(takePictureIntent, TAKE_SHOT_REQUEST_CODE)
        }
        binding.sendSelfi.setOnClickListener {
            val sendPhotoIntent = Intent(Intent.ACTION_SEND)
            sendPhotoIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("hodovychenko.labs@gmail.com"))
            sendPhotoIntent.putExtra(Intent.EXTRA_SUBJECT, "КПП АИ-193 Закиров")
            sendPhotoIntent.putExtra(Intent.EXTRA_STREAM, fileProvider)
            sendPhotoIntent.type = "image/jpg"
            startActivityForResult(
                Intent.createChooser(sendPhotoIntent, "Send photo"),
                SEND_PICTURE_REQUEST_CODE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_SHOT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            binding.imageView.setImageURI(fileProvider)
            binding.sendSelfi.visibility = View.VISIBLE
        } else if (requestCode == SEND_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            binding.sendSelfi.visibility = View.INVISIBLE
        } else {
            Toast.makeText(this, "Unexpected error", Toast.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}