package ch.heigvd.daa.labo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.core.view.children
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.iict.and.labo2.Person

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private var currentUser: Person? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initSpinnersWithDefaultValues();
        initEvents()

    }

    private fun initSpinnersWithDefaultValues() {
        binding.mainSpecificSectorInput.adapter = DefaultValueArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            getString(R.string.sectors_empty),
            resources.getStringArray(R.array.sectors))

        binding.mainBaseNationalityInput.adapter = DefaultValueArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            getString(R.string.nationality_empty),
            resources.getStringArray(R.array.nationalities))
    }

    private fun initEvents() {
        binding.mainBaseOccupationGroup.setOnCheckedChangeListener { _, checkId ->
            if (studentSelected(checkId)) {
                binding.mainSpecificStudentGrouping.visibility = View.VISIBLE
                binding.mainSpecificEmployeeGrouping.visibility = View.GONE
            } else if(employeeSelected(checkId)) {
                binding.mainSpecificStudentGrouping.visibility = View.GONE
                binding.mainSpecificEmployeeGrouping.visibility = View.VISIBLE
            }
        }


        binding.mainBaseBirthdateIcon.setOnClickListener {

        }

        binding.btnOk.setOnClickListener {
            val d = binding.mainBaseNationalityInput.selectedItem as String;
            Log.d("test", d)

        }

        binding.btnCancel.setOnClickListener {
            for (view in binding.root.children) {
                if (view is EditText) {
                    view.text.clear()
                }
            }


            binding.mainBaseOccupationGroup.clearCheck()

        }
    }

    private fun saveUser() {
        TODO("Not yet implemented")
    }


    private fun studentSelected(checkId: Int): Boolean {
        return checkId == R.id.main_base_occupation_student
    }

    private fun employeeSelected(checkId: Int): Boolean {
        return checkId == R.id.main_base_occupation_worker
    }


}