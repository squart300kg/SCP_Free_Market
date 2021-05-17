package com.scp.market.ui.custom

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.FileProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.scp.market.BuildConfig
import com.scp.market.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class CameraGalleryBottomDialog(
    private val fragment: Fragment?,
    private val activity: Activity,
    private val ratio: Int
) : BottomSheetDialogFragment(), View.OnClickListener  {

    private var cameraImageFile: File? = null
    private var imageUri: Uri? = null

    private val galleryPermissionListener = object : PermissionListener {


            override fun onPermissionGranted() {
            pickFromGallery()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            // permission denied
        }
    }



    private val cameraPermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            pickFromCamera()
        }


        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            // permission denied
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_camera_gallery_bottom_dialog, container, false)

        view.findViewById<LinearLayout>(R.id.cameraView).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.galleryView).setOnClickListener(this)

        return view
    }


    companion object {
        const val PICK_FROM_CAMERA = 1
        const val PICK_FROM_GALLERY = 2
        const val PICK_FROM_CAMERA_TWO_BY_ONE = 3

        // image crop
        const val ACTION_REQUEST_GALLERY = 99
        const val ACTION_REQUEST_IMAGE_CROP = 98
        const val ACTION_REQUEST_GALLERY_TWO_BY_ONE = 97
        const val ACTION_REQUEST_IMAGE_CROP_TWO_BY_ONE = 96

        // type
        const val ONE_BY_ONE = 11
        const val TWO_BY_ONE = 12
    }

    private fun pickFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            cameraImageFile = createImageFile()
            Log.i("imageFileLog", cameraImageFile.toString())
        } catch (e: IOException) {
            Toast.makeText(activity, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        if (cameraImageFile != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val photoUri =
                        FileProvider.getUriForFile(
                                activity,
                                "${BuildConfig.APPLICATION_ID}.provider",
                                cameraImageFile!!
                        )
                imageUri = photoUri

                Log.i("photoURLLOG", photoUri.toString())
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

                if (ratio == TWO_BY_ONE) {
                    if (fragment != null) {
                        fragment.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA_TWO_BY_ONE
                        )
                    } else {
                        activity.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA
                        )

                    }
                } else {
                    if(fragment != null) {
                        fragment.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA
                        )
                        Toast.makeText(activity, "사진촬영1", Toast.LENGTH_LONG).show()
                    } else {
                        activity.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA
                        )
                        Toast.makeText(activity, "사진촬영2", Toast.LENGTH_LONG).show()
                    }
                    Toast.makeText(activity, "사진촬영3", Toast.LENGTH_LONG).show()
                }
            } else {
                val photoUri = Uri.fromFile(cameraImageFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                if (ratio == TWO_BY_ONE) {
                    if(fragment != null) {
                        fragment.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA_TWO_BY_ONE
                        )
                    } else {
                        activity.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA_TWO_BY_ONE
                        )
                    }
                } else {
                    if(fragment != null) {
                        fragment.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA
                        )
                    } else {
                        activity.startActivityForResult(
                                intent,
                                PICK_FROM_CAMERA
                        )
                    }
                }
            }
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        if (ratio == TWO_BY_ONE) {
            if(fragment != null) {
                fragment.startActivityForResult(
                        intent,
                        ACTION_REQUEST_GALLERY_TWO_BY_ONE
                )
            } else {
                activity.startActivityForResult(
                        intent,
                        ACTION_REQUEST_GALLERY_TWO_BY_ONE
                )
            }
        } else {
            if(fragment != null) {
                fragment.startActivityForResult(
                        Intent.createChooser(intent, "Chooser Title"),
                        ACTION_REQUEST_GALLERY
                )
            } else {
                activity.startActivityForResult(
                        Intent.createChooser(intent, "Chooser Title"),
                        ACTION_REQUEST_GALLERY
                )
            }
        }
    }

    /**
     * 폴더 및 파일 만들기
     */
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // 이미지 파일 이름 ( scp_{시간}_ )
        val timeStamp = SimpleDateFormat("HHmmss", Locale.ROOT).format(Date())
        val imageFileName = "scp" + timeStamp + "_"

        // 이미지가 저장될 폴더 이름 ( scp )
        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/scp/")
        if (!storageDir.exists()) {
            storageDir.mkdirs()
        }

        // 파일 생성
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }



    override fun onClick(view: View?) {
        if (view?.id == R.id.cameraView) {
            TedPermission.with(context)
                .setPermissionListener(cameraPermissionListener)
                .setDeniedMessage("카메라에 접근하기 위해서 권한 설정이 필요합니다.\n\n권한 설정을 해주세요. [설정] > [권한]")
                .setPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .check()

        } else if (view?.id == R.id.galleryView) {
            TedPermission.with(context)
                .setPermissionListener(galleryPermissionListener)
                .setDeniedMessage("갤러리에 접근하기 위해서 권한 설정이 필요합니다.\n\n권한 설정을 해주세요. [설정] > [권한]")
                .setPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .check()
        }

        dismiss()
    }

    fun startCrop(data: Intent?): String? {
        val photoUri = data?.data
        var cursor: Cursor? = null

        val proj = arrayOf(MediaStore.Images.Media.DATA)

        cursor = if (fragment == null && activity != null) {
            activity.contentResolver?.query(photoUri!!, proj, null, null, null)
        } else {
            fragment?.context?.contentResolver?.query(photoUri!!, proj, null, null, null)
        }

        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

        cursor?.moveToFirst()

        if (columnIndex != null) {
            val fileUri = cursor?.getString(columnIndex)
            return fileUri
        }
        return null

    }

    fun setImage(): Bitmap
            = BitmapFactory.decodeFile(cameraImageFile?.absolutePath)

    fun getImageUri(bitmapImageFile: File): Uri
            = FileProvider.getUriForFile(
                    activity,
                    "${BuildConfig.APPLICATION_ID}.provider",
                    bitmapImageFile!!
            )

    fun getImageTempUri() =
             imageUri!!

}