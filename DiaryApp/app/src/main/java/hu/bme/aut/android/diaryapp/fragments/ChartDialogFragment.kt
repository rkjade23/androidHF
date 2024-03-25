package hu.bme.aut.android.diaryapp.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.opengl.ETC1.isValid
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import hu.bme.aut.android.diaryapp.R
import hu.bme.aut.android.diaryapp.adapter.EntryAdapter
import hu.bme.aut.android.diaryapp.data.EntryItem
import hu.bme.aut.android.diaryapp.data.EntryListDatabase
import hu.bme.aut.android.diaryapp.databinding.DialogFragmentChartBinding
import hu.bme.aut.android.diaryapp.databinding.DialogNewEntryItemBinding
import kotlin.concurrent.thread


class ChartDialogFragment: DialogFragment() {

    private lateinit var binding: DialogFragmentChartBinding

    private var database = activity?.let { EntryListDatabase.getDatabase(it.applicationContext) }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogFragmentChartBinding.inflate(LayoutInflater.from(context))

        loadMoodChart()
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.mood_chart)
            .setView(binding.root)
            .setNegativeButton(R.string.button_cancel, null)
            .create()
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

       // loadMoodChart();
    }

     fun getData(mood: EntryItem.Mood): Int{
         var db = 0
        val items = database?.entryItemDao()?.getAll()
         if (items != null) {
             for (item in items){
                    if (item.mood == mood) db++
             }
         }
        return db
    }


    private fun loadMoodChart(){
        val entries = listOf(
            PieEntry(getData(EntryItem.Mood.GOOD).toFloat(), "Good"),
            PieEntry(getData(EntryItem.Mood.AVERAGE).toFloat(), "Average"),
            PieEntry(getData(EntryItem.Mood.BAD).toFloat(), "Bad")
        )

        val dataSet = PieDataSet(entries, "Mood")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = PieData(dataSet)
        binding.chart.data = data
        binding.chart.invalidate()
    }




    companion object {
        const val TAG = "ChartDialogFragment"
    }
}