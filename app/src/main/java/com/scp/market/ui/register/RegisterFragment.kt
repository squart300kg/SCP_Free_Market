package com.scp.market.ui.register

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.scp.market.R
import com.scp.market.databinding.FragmentRegisterBinding
import com.scp.market.model.Category
import com.scp.market.model.registerProduct.request.RegisterProductRequest
import com.scp.market.state.NetworkState
import com.scp.market.ui.MainActivity
import com.scp.market.ui.custom.CameraGalleryBottomDialog
import com.scp.market.ui.custom.CameraGalleryBottomDialog.Companion.ACTION_REQUEST_GALLERY
import com.scp.market.ui.custom.CameraGalleryBottomDialog.Companion.ACTION_REQUEST_IMAGE_CROP
import com.scp.market.ui.custom.CameraGalleryBottomDialog.Companion.PICK_FROM_CAMERA
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RegisterFragment : Fragment() {

    // TODO 공급책임자, 책임자 연락처 처리할 것

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModel()

    private var cameraGalleryBottomDialog: CameraGalleryBottomDialog? = null

    private var category_code: String? = null
    private var category_name: String? = null

    private val imageURIList = arrayListOf<Uri>()
    private val imageDownloadURLList = arrayListOf<String>()

    private var isCameraMode = false
    private var isGalleryMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureObservables()
//        registerViewModel.getCategories()
        initCategories(com.scp.market.Application.instance?.categotyList!!)

        binding.btnRegister.setOnClickListener {

            // 상품 업로드 순서
            // 1. 이미지를 Firebase의 Storage에 올린다. 그 후, URL을 반환 받는다.
            // 2. 반환받은 URL을 사용하여 imwebAPI를 사용하여 상품을 업로드 한다.
            // 3. 전송 버튼을 클릭 후, 파이어스토어에 이미지 업로드가 시작되었을 때, 다이얼로그를 띄워준다.
            createProgressBar()

            Log.i("imageURLLIst : ", imageURIList.toString())
            if (isCameraMode) {
                uploadImageToFireStoreForCamera(category_name.toString(), imageURIList[0].toString())
            }
            if (isGalleryMode) {

                uploadImageToFireStoreForGallery(category_name.toString(), getRealPathFromURI(imageURIList))

            }


        }
        binding.btnRegisterImage.setOnClickListener {
            cameraGalleryBottomDialog =
                    CameraGalleryBottomDialog(this, requireActivity(), CameraGalleryBottomDialog.ONE_BY_ONE)
            cameraGalleryBottomDialog?.show(
                    requireActivity().supportFragmentManager,
                    "cameraGalleryDialog"
            )
        }


    }

    private fun getFilePathForN(uri: Uri, context: Context): String? {
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        /*
     * Get the column indexes of the data in the Cursor,
     *     * move to the first row in the Cursor, get the data,
     *     * and display it.
     * */
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = java.lang.Long.toString(returnCursor.getLong(sizeIndex))
        val file = File(context.filesDir, name)
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            var read = 0
            val maxBufferSize = 1 * 1024 * 1024
            val bytesAvailable = inputStream!!.available()

            //int bufferSize = 1024;
            val bufferSize = Math.min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            inputStream.close()
            outputStream.close()
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: java.lang.Exception) {
            Log.e("Exception", e.message!!)
        }
        return file.path
    }

    private fun getRealPathFromURI(contentURIList: List<Uri>): List<String>? {

        // TODO deprecated된 요소 변경할 것
        var cursor: Cursor? = null
        var fileUriList = ArrayList<String>()
        var fileUri: String? = null

        for ( contentURI in contentURIList ) {

            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = activity?.contentResolver?.query(contentURI!!, proj, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()

            if (columnIndex != null) {
                fileUri = cursor?.getString(columnIndex)
                Log.i("getRealPathFromURI", fileUri.toString())
                if (fileUri == null) {
                    Toast.makeText(activity, "유효하지 않은 이미지가 선택되었습니다.", Toast.LENGTH_SHORT).show()
                    return null
                }
            } else {
                Log.i("columIndex is numm", "null")
            }
            fileUriList.add(fileUri.toString())
        }

        Log.i("getRealPathFromURI", fileUriList.toString())
        return fileUriList
    }



    private fun uploadImageToFireStoreForGallery(category: String, fileURIList: List<String>?) {

        // TODO 안드로이드 10 저장소 관련 대응할 것. 것
        //  매니페스트 android:requestLegacyExternalStorage="true"지울 것
//        var file = Uri.fromFile(File(fileURIList))

        val storage = Firebase.storage

        // Create a storage reference from our app
        val storageRef = storage.reference
        Log.i("fileURI0", fileURIList.toString())
        var roopIndex = 0
        var downloadImageURLResponseIndex = 0
        while ( roopIndex < fileURIList?.size!!) {

            var fileURI = fileURIList[roopIndex]

            Log.i("fileURI1", fileURI)
            var file = Uri.fromFile(File(fileURI))
            Log.i("fileURI2", file.toString())
            // Create a reference to 'images/mountains.jpg'
            val timeStamp = SimpleDateFormat("HHmmss", Locale.ROOT).format(Date())
            val imagesRef = storageRef.child("${category}/${timeStamp}_${file.lastPathSegment}")

            val uploadTask = imagesRef.putFile(file)
            uploadTask.addOnFailureListener {

                Toast.makeText(activity, "이미지를 서버에 업로드하는데 실패했습니다.", Toast.LENGTH_LONG).show()
                dismissProgressBar()

            }

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }

                imagesRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    Log.i("downLoadUri결과값 : ", downloadUri.toString())
                    imageDownloadURLList.add(downloadUri.toString())

                    downloadImageURLResponseIndex++
                    if (downloadImageURLResponseIndex == fileURIList.size) {
                        Log.i("상풍삽입2, ", "상품삽입 index : $roopIndex, fileSize : ${fileURIList.size}")

                        registerViewModel.registerProduct(
                                RegisterProductRequest(
                                        listOf(category_code), // 상품 카테고리
                                        imageDownloadURLList, // 상품 썸네일
                                        binding.edt02.text.toString(), // 상품 이름
                                        "근육이 무럭무럭 심플콘텐츠 - 하드코딩", // 상품 간단 설명
                                        binding.edt03.text.toString(), // 상품 상세설명
                                        binding.edt05.text.toString().toDouble(), // 할인가격
                                        binding.edt04.text.toString().toDouble(), // 원래가격
                                        binding.edt09.toString() // 공급지
                                )
                        )
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }
            roopIndex++

        }

    }

    private fun uploadImageToFireStoreForCamera(category: String, fileUrl: String) {

        // TODO 안드로이드 10 저장소 관련 대응할 것. 것
        //  매니페스트 android:requestLegacyExternalStorage="true"지울 것

        val storage = Firebase.storage

        // Create a storage reference from our app
        val storageRef = storage.reference


            var file = Uri.fromFile(File(fileUrl))
            Log.i("fileURI1", file.toString())
            // Create a reference to 'images/mountains.jpg'
            val timeStamp = SimpleDateFormat("HHmmss", Locale.ROOT).format(Date())
            val imagesRef = storageRef.child("${category}/${timeStamp}_${file.lastPathSegment}")

            val uploadTask = imagesRef.putFile(file)
            uploadTask.addOnFailureListener {

                Toast.makeText(activity, "이미지를 서버에 업로드하는데 실패했습니다.", Toast.LENGTH_LONG).show()
                dismissProgressBar()

            }

            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }

                imagesRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    Log.i("downLoadUri결과값 : ", downloadUri.toString())
                    imageDownloadURLList.add(downloadUri.toString())

                        Log.i("상풍삽입2, ", "상품삽입")

                        registerViewModel.registerProduct(
                                RegisterProductRequest(
                                        listOf(category_code), // 상품 카테고리
                                        imageDownloadURLList, // 상품 썸네일
                                        binding.edt02.text.toString(), // 상품 이름
                                        "근육이 무럭무럭 심플콘텐츠 - 하드코딩", // 상품 간단 설명
                                        binding.edt03.text.toString(), // 상품 상세설명
                                        binding.edt05.text.toString().toDouble(), // 할인가격
                                        binding.edt04.text.toString().toDouble(), // 원래가격
                                        binding.edt09.toString() // 공급지
                                )
                        )

                } else {
                    // Handle failures
                    // ...
                }
            }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            Log.i("갔다옴", "0")
            when (requestCode) {
                PICK_FROM_CAMERA -> {
                    // TODO 카메라를 통한 이미지 처리 해결할 것 & 리팩토링 필요

                    var bitmap = resizeBitmap(requireNotNull(activity), cameraGalleryBottomDialog?.getImageTempUri()!!, 200)
                    binding.btnRegisterImage.setImageBitmap(bitmap)

//                    imageURIList.add(cameraGalleryBottomDialog?.getImageUri()!!)
                    var bitmapFile = bitmapToFile(activity?.let { resizeBitmap(it, cameraGalleryBottomDialog?.getImageTempUri()!!, 200) }!!)
                    Log.i("bitmapFileUrl_F", bitmapFile?.toUri().toString())
                    Log.i("bitmapFileUrl_C", bitmapFile?.path.toString())
                    imageURIList.add(requireNotNull(bitmapFile?.path?.toUri()))
                    binding.imageCount.text = "선택된 사진 : 1장"
                    isCameraMode = true
                    isGalleryMode = false
                }

                ACTION_REQUEST_GALLERY -> {
                    Log.i("갔다옴", "2")
                    data?.let {
                        Log.i("갔다옴", "3")

                        // 만약 사진을 하나만 선택했다면, clipData 가 아닌 data 로 URI 만 보내준다
                        // 복수의 사진은 clipData 로 들어온다
                        when {

                            it.clipData!!.itemCount!! >= 1 -> {
                                Log.i("갔다옴", "4")
                                if (it.clipData!!.itemCount >= 11) {
                                    Toast.makeText(activity, "사진 선택은 1 ~ 10장까지만 가능합니다.", Toast.LENGTH_LONG).show()
                                    return
                                }


                                val clip = it.clipData
                                val size = clip?.itemCount

                                for (i in 0 until size!!) {

                                    // 동영상이 있으면 안됨
                                    if (clip.getItemAt(i).uri.toString().contains("video")) {
                                        Toast.makeText(activity, "비디오는 선택하실 수 없습니다.", Toast.LENGTH_LONG).show()
                                        return
                                    }
                                    val item = clip.getItemAt(i).uri
                                    Log.i("결과 URI", item.toString())
                                    imageURIList.add(item)
                                }
                                binding.btnRegisterImage.setImageBitmap(activity?.let { resizeBitmap(it, imageURIList[0], 200) })
                                binding.imageCount.text = "선택된 사진 : ${size}장"
                            }
                            else -> {}
                        }
                    }
                    isCameraMode = false
                    isGalleryMode = true
                }

                ACTION_REQUEST_IMAGE_CROP -> {
                    Log.i("갔다옴", "5")}
            }
        }

    }

    private fun getImageContentUri(fileName: File, context: Context): Uri {
        var fileUri = Uri.parse( fileName.toString() )
        var filePath = fileUri.path
        Log.i("filePath : ", filePath.toString())
        var cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        null, "_data = '" + filePath + "'", null, null)

        cursor?.moveToNext()

        var id = cursor?.getLong(cursor.getColumnIndex("_id"))
        var uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, requireNotNull(id))

        return uri
    }

    private fun bitmapToFile(bitmap: Bitmap): File? {
        var fileOutputStream: FileOutputStream? = null
        var bitmapFile: File? = null

        try {

            val timeStamp = SimpleDateFormat("HHmmss", Locale.ROOT).format(Date())
            val imageFileName = "scp" + timeStamp + "_"

            val file = File(Environment.getExternalStorageDirectory().toString() + "/scp/")
            if (!file.exists()) {
                file.mkdir()
            }

            Log.i("bitmapToFilePath1", file.path)

            // TODO 카메라, 앨범으로 저장장소 접근시 대응 필요


            bitmapFile = File(file, "scp" + SimpleDateFormat("HHmmss", Locale.ROOT).format(Date()) + ".jpg")
            Log.i("bitmapToFilePath2", bitmapFile.path)
            Log.i("bitmapToFilePath2_abs", bitmapFile.absolutePath)
            fileOutputStream = FileOutputStream(bitmapFile)
            val resizeBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true)
            resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
            MediaScannerConnection.scanFile(activity, arrayOf(bitmapFile.absolutePath), null, object : MediaScannerConnection.MediaScannerConnectionClient {
                override fun onMediaScannerConnected() {

                }

                override fun onScanCompleted(path: String?, uri: Uri?) {
                    Toast.makeText(activity, "file saved", Toast.LENGTH_LONG).show()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush()
                    fileOutputStream.close()
                } catch (e: Exception) {
                }

            }
        }
        return bitmapFile
    }

    private fun resizeBitmap(activity: Activity, uri: Uri, resize: Int): Bitmap? {
        var resizeBitmap: Bitmap? = null

        val options = BitmapFactory.Options()

        try {
            BitmapFactory.decodeStream(activity.contentResolver.openInputStream(uri), null, options)

            var width = options.outWidth
            var height = options.outHeight
            var sampleSize = 1

            while (true) {
                if (width / 2 < resize || height / 2 < resize) break

                width /= 2
                height /= 2
                sampleSize *= 2
            }

            options.inSampleSize = sampleSize
            var bitmap = BitmapFactory.decodeStream(activity.contentResolver.openInputStream(uri), null, options)
            resizeBitmap = bitmap
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return resizeBitmap
    }

    private fun configureObservables() {
        registerViewModel.registerNetworkState.observe(this, Observer { networkState ->
            when(networkState) {
                NetworkState.RUNNING -> {}
                NetworkState.SUCCESS -> {

                    Toast.makeText(activity, "상품 등록을 완료하였습니다!", Toast.LENGTH_LONG).show()
                    dismissProgressBar()
                    (activity as MainActivity).navigationBar.selectedItemId = R.id.menu01

                }
                NetworkState.FAILED -> {

                    Toast.makeText(activity, "상품 등록에 실패하였씁니다.", Toast.LENGTH_LONG).show()
                    dismissProgressBar()

                }
            }
        })
//        registerViewModel.categoryNetWorkState.observe(this, Observer { networkState ->
//            when(networkState) {
//                NetworkState.RUNNING -> {}
//                NetworkState.SUCCESS -> {
//
//                    initCategories(registerViewModel.categories.value!!)
//
//                }
//                NetworkState.FAILED -> {}
//            }
//        })
    }

    private fun initCategories(categoryList: List<Category>) {

        val items = ArrayList<String>()

        for ( category in categoryList ) {
            items.add(category.name!!)
        }


        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, items)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.setOnTouchListener { _, _ ->
            val view = activity?.currentFocus
            if (view != null) {
                val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            false
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {

                category_code = categoryList[position].code
                category_name = categoryList[position].name

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

    }

    private fun createProgressBar() {
        binding.rootView.setBackgroundResource(R.color.very_light_gray)
        binding.progressBar.visibility = View.VISIBLE
        activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun dismissProgressBar() {
        binding.rootView.setBackgroundResource(R.color.white)
        binding.progressBar.visibility = View.GONE
        activity?.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        )
    }



}