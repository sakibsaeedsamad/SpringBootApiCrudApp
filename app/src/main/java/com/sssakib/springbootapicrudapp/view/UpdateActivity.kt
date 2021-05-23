package com.sssakib.springbootapicrudapp.view

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.sssakib.springbootapicrudapp.R
import com.sssakib.springbootapicrudapp.model.User
import com.sssakib.springbootapicrudapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_update.*
import kotlinx.android.synthetic.main.activity_update.locationUpdateSpinner
import kotlinx.android.synthetic.main.activity_update.view.*
import java.io.ByteArrayOutputStream
import java.util.*

class UpdateActivity : AppCompatActivity() {



    lateinit var viewModel: UserViewModel
    lateinit var locationString: String


    var uId = 0
    var uName: String? = null
    var uAge: String? = null
    var uPhone: String? = null
    var uGender: String? = null
    var uLocation: String? = null
    var uImage: String? = null

    var tempAge = 0
    lateinit var age: String

    override fun onCreate(savedInstanceState: Bundle?) {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_YEAR)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        val intent = intent
        uId = intent.extras!!.getInt("id")
        uName = intent.extras!!.getString("name")
        uAge = intent.extras!!.getString("age")
        uPhone = intent.extras!!.getString("phone")
        uGender = intent.extras!!.getString("gender")
        uLocation = intent.extras!!.getString("location")
        uImage = intent.extras!!.getString("image")

        nameUpdateET.setText(uName).toString()
        phoneUpdateET.setText(uPhone).toString()
        updateAgeTV.setText("Your age is: " + uAge)
        profileUpdateImageView.setImageBitmap(convertStringToBitmap(uImage))


//access the items of the list
        val location = resources.getStringArray(R.array.locationAarray)
// Create an ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, location)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
// Apply the adapter to the spinner
        locationUpdateSpinner.adapter = adapter
        if (uLocation != null) {
            val spinnerPosition: Int = adapter.getPosition(uLocation)
            locationUpdateSpinner.setSelection(spinnerPosition)
        }
        locationUpdateSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                locationString = location[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        updateUserButton.setOnClickListener(View.OnClickListener {
            updateUser();
        })
        takeImageUpdateButton.setOnClickListener(View.OnClickListener {
            requestCameraPermission()
        })
        uploadImageUpdateButton.setOnClickListener(View.OnClickListener {
            requestStoragePermission()
        })


        updateBdatePickBTN.setOnClickListener {
            val dpkr= DatePickerDialog(this, DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->

                tempAge= (year - mYear)
                age = tempAge.toString()
                updateAgeTV.setText("Your age is: "+age)
                uAge=age

            }, year,month,day)

            dpkr.show()

        }

        if(uGender == "Male"){
            maleUpdateRadioButton.isChecked = true
        }
        if(uGender == "Female"){
            femaleUpdateRadioButton.isChecked = true
        }

        radioGroupUpdate.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener() { radioGroup: RadioGroup, checkedId: Int ->


            when(checkedId){
                R.id.maleUpdateRadioButton -> uGender="Male"
                R.id.femaleUpdateRadioButton -> uGender="Female"
            }
        });




    }

    private fun updateUser() {
        var name = nameUpdateET.getText().toString().trim()
        var phone = phoneUpdateET.getText().toString().trim()
        tempAge= uAge!!.toInt()

        var isAgree = agreeToUpdateCheckbox.isChecked

        if (name.length <= 2) {
            Toast.makeText(
                this,
                "Please enter your name.",
                Toast.LENGTH_LONG
            ).show()
        }
        if (phone.length < 11 || phone.length > 11) {
            Toast.makeText(
                this,
                "Please give 11 digit number...",
                Toast.LENGTH_LONG
            ).show()
        }
        if (uGender!!.isEmpty()) {
            Toast.makeText(
                this,
                "Please select gender",
                Toast.LENGTH_LONG
            ).show()
        }

        if (uImage.isNullOrEmpty()) {
            Toast.makeText(
                this,
                "Please take or upload a photo",
                Toast.LENGTH_LONG
            ).show()
        }
        if (tempAge==0 || tempAge <= 17) {

            Toast.makeText(
                this,
                "Under aged user!",
                Toast.LENGTH_LONG
            ).show()
        }
        if (!isAgree) {

            Toast.makeText(
                this@UpdateActivity,
                "Please agree to Update!",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val user = User(uId,name, phone, uAge, uGender, locationString, uImage)
            viewModel.updateUserInfo(user)



            val intent =Intent(this,MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(
                this,
                "User Updated!",
                Toast.LENGTH_LONG
            ).show()

        }


    }

    private fun requestStoragePermission() {
        Dexter.withActivity(this)
            .withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    openGallary()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun requestCameraPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // permission is granted
                    openCamera()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")
        builder.setPositiveButton(
            "GOTO SETTINGS",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                openSettings()
            })
        builder.setNegativeButton(
            "Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 7)
    }

    private fun openGallary() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            val captureImage = data!!.extras!!["data"] as Bitmap?

            profileUpdateImageView!!.setImageBitmap(captureImage)

            uImage = captureImage?.let { convertBitmapToString(it) }
        }

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            val captureImage = data?.data
            val mBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, captureImage)

            profileUpdateImageView!!.setImageBitmap(mBitmap)

            uImage = captureImage?.let { convertBitmapToString(mBitmap) }
        }
    }

    fun convertStringToBitmap(string: String?): Bitmap {
        val byteArray =
            Base64.decode(string, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    }

    fun convertBitmapToString(bitmap: Bitmap): String? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


}

