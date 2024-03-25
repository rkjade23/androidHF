package hu.bme.aut.android.diaryapp.fragments


import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.opengl.ETC1.isValid
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.fragment.app.DialogFragment
import coil.load
import com.github.mikephil.charting.renderer.scatter.SquareShapeRenderer
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import hu.bme.aut.android.diaryapp.data.EntryItem
import hu.bme.aut.android.diaryapp.databinding.DialogNewEntryItemBinding
import hu.bme.aut.android.diaryapp.R
import java.util.*

class NewEntryItemDialogFragment : DialogFragment() {

    interface NewEntryItemDialogListener {
        fun onEntryItemCreated(newItem: EntryItem)
        fun onEntryEdited(newItem: EntryItem)
    }

    private var editing = false

    private lateinit var listener: NewEntryItemDialogListener

    private lateinit var binding: DialogNewEntryItemBinding

    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewEntryItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewEntryItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        binding = DialogNewEntryItemBinding.inflate(LayoutInflater.from(context))
        binding.spMood.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.mood_items)
        )

        binding.btPhoto.setOnClickListener{

            cameraCheckPermission()
        }

        binding.btGallery.setOnClickListener{
            galleryCheckPermission()
        }

        editing = false

        if(!Objects.isNull(arguments)){
            editing = true
            binding.etTitle.setText(arguments?.getString("title"))
            binding.etDate.setText(arguments?.getString("date"))
            binding.etEntry.setText(arguments?.getString("entry"))
            binding.spMood.setSelection(arguments?.getInt("mood") ?: 0)
            binding.imageView.setImageBitmap((arguments?.getParcelable(("image"))))
        }



        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_entry)
            .setView(binding.root)
            .setPositiveButton(R.string.button_ok) { dialogInterface, i ->
                if (isValid()) {
                    if (editing) {
                        listener.onEntryEdited(getEntryItem(arguments?.getLong("id")))
                    } else
                        listener.onEntryItemCreated(getEntryItem())
                }
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }

    fun createNew(){ editing = false }

    private fun isValid() = binding.etTitle.text.isNotEmpty()

    private fun getEntryItem(id: Long? = null) = EntryItem(
        id = id,
        title = binding.etTitle.text.toString(),
        entry = binding.etEntry.text.toString(),
        mood = EntryItem.Mood.getByOrdinal(binding.spMood.selectedItemPosition)
            ?: EntryItem.Mood.AVERAGE,
        date = binding.etDate.text.toString(),
        image = binding.imageView.drawToBitmap()
    )

    private fun galleryCheckPermission(){
        Dexter.withContext(activity).withPermission(
            android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object: PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    gallery()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(activity, "No Permission", Toast.LENGTH_SHORT).show()                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    Toast.makeText(activity, "No Permission", Toast.LENGTH_SHORT).show()
                }

            }
        ).onSameThread().check()
    }

    private fun gallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun cameraCheckPermission(){
        Dexter.withContext(activity)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA).withListener(

                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?){
                            report?.let{
                                if (report.areAllPermissionsGranted()){
                                    camera()
                                }
                            }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        Toast.makeText(activity, "No Permission", Toast.LENGTH_SHORT).show()
                    }


            }
            ).onSameThread().check()
    }

    private fun camera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            when(requestCode){
                CAMERA_REQUEST_CODE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.imageView.load(bitmap){
                        crossfade(true)
                        crossfade(1000)
                    }
                }

                GALLERY_REQUEST_CODE ->{
                    binding.imageView.load(data?.data){
                        crossfade(true)
                        crossfade(1000)
                    }
                }
            }
        }
    }


    companion object {
        const val TAG = "NewEntryItemDialogFragment"
    }
}