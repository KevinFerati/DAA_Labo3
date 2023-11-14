/**
 * Kevin Ferati, Malo Romano & Flavio Sovilla
 *
 * 14.11.2023
 *
 * Labo 3 - DAA
 *
 * This class represents the main activity of the Android application.
 * It includes the implementation of a user interface for inputting and saving person details.
 */

package ch.heigvd.daa.labo3

import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.children
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.iict.and.labo2.Person
import ch.heigvd.iict.and.labo2.Person.Companion.exampleStudent
import ch.heigvd.iict.and.labo2.Student
import ch.heigvd.iict.and.labo2.Worker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Constants
 */
const val DATE_FORMAT = "dd/MM/yyyy"
const val MIN_GRADUATION_YEAR = 1900
const val MAX_GRADUATION_YEAR = 2024
const val MIN_EXPERIENCE = 0
const val MAX_EXPERIENCE = 100

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    /**
     * When the activity is created
     * Initialize events, binds the view, spinners and fill the form with a default person
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initEvents()

        binding.mainBaseNationality.adapter = DefaultValueSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, resources.getString(R.string.nationality_empty), resources.getStringArray(R.array.nationalities))
        binding.mainSpecificSectorInput.adapter = DefaultValueSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, resources.getString(R.string.sectors_empty), resources.getStringArray(R.array.sectors))

        fillFormWithPerson(exampleStudent)
    }




    /**
     * Init Activty events
     */
    private fun initEvents() {
        binding.mainBaseOccupationGroup.setOnCheckedChangeListener { _, checkId ->
            if (studentSelected(checkId)) {
                binding.mainSpecificStudentGrouping.visibility = View.VISIBLE
                binding.mainSpecificEmployeeGrouping.visibility = View.GONE
            } else if (employeeSelected(checkId)) {
                binding.mainSpecificStudentGrouping.visibility = View.GONE
                binding.mainSpecificEmployeeGrouping.visibility = View.VISIBLE
            }
        }

        /**
         * Date picker button click event binding
         */
        binding.mainBaseBirthdateInput.setOnClickListener {
            showDatePickerDialog()
        }

        /**
         * Save button click event binding
         */
        binding.btnOk.setOnClickListener {
            saveUser()
        }

        /**
         * Cancel button click event binding
         */
        binding.btnCancel.setOnClickListener {
            for (view in binding.mainLayout.children) {
                if (view is EditText) {
                    view.text.clear()
                }
                if (view is Spinner){
                    view.setSelection(0)
                }
            }
            binding.mainBaseOccupationStudent.isChecked = true
        }
    }



    /**
     * Mock a user save : check inputs, create a new object and log it
     */
    private fun saveUser() {
        if (!binding.mainBaseOccupationStudent.isChecked && !binding.mainBaseOccupationWorker.isChecked) {
            showMessageDialog(resources.getString(R.string.error_select_person_type))
            return
        }

        val name: String = binding.mainBaseName.text.toString()
        if (name == "") {
            showMessageDialog(resources.getString(R.string.error_empty_fields))
            return
        }
        val firstname: String = binding.mainBaseFirstname.text.toString()
        if (firstname == "") {
            showMessageDialog(resources.getString(R.string.error_empty_fields))
            return
        }

        val birthdayText = binding.mainBaseBirthdateInput.text.toString()
        if (birthdayText == "") {
            showMessageDialog(resources.getString(R.string.error_invalid_birthdate))
            return
        }
        val birthday: Calendar = Calendar.getInstance()
        birthday.time =
            SimpleDateFormat(DATE_FORMAT).parse(binding.mainBaseBirthdateInput.text.toString())!!

        val nationality: String = binding.mainBaseNationality.selectedItem.toString()

        val email: String = binding.additionalEmailInput.text.toString()
        if (email == "") {
            showMessageDialog(resources.getString(R.string.error_select_email))
            return
        }
        if (!isValidEmail(email)) {
            showMessageDialog(resources.getString(R.string.error_invalid_email))
            return
        }

        val remark: String = binding.additionalEmailInput.text.toString()

        val person: Person = if (binding.mainBaseOccupationStudent.isChecked) {
            val university: String = binding.mainSpecificUniversityInput.text.toString()
            if (university == "") {
                showMessageDialog(resources.getString(R.string.error_select_university))
                return
            }
            val graduationYear: Int
            try {
                graduationYear = binding.mainSpecificGraduationyearInput.text.toString().toInt()
                if (graduationYear < MIN_GRADUATION_YEAR || graduationYear > MAX_GRADUATION_YEAR) {
                    showMessageDialog(resources.getString(R.string.error_invalid_graduation_year, MIN_GRADUATION_YEAR, MAX_GRADUATION_YEAR))
                    return
                }
            } catch (e: Exception) {
                showMessageDialog(resources.getString(R.string.error_invalid_graduation_year))
                return
            }

            Student(
                name,
                firstname,
                birthday,
                nationality,
                university,
                graduationYear,
                email,
                remark
            )


        } else {
            val company: String = binding.mainSpecificCompagnyInput.text.toString()
            if (company == "") {
                showMessageDialog(resources.getString(R.string.error_select_company))
                return
            }

            val sector = binding.mainSpecificSectorInput.selectedItem.toString()

            val experience: Int
            try {
                experience = binding.mainSpecificExperienceInput.text.toString().toInt()
                if (experience < MIN_EXPERIENCE || experience > MAX_EXPERIENCE) {
                    showMessageDialog(resources.getString(R.string.error_invalid_experience, MIN_EXPERIENCE, MAX_EXPERIENCE))
                    return
                }
            } catch (e: Exception) {
                showMessageDialog(resources.getString(R.string.error_invalid_experience, MIN_EXPERIENCE, MAX_EXPERIENCE))
                return
            }

            Worker(
                name,
                firstname,
                birthday,
                nationality,
                company,
                sector,
                experience,
                email,
                remark
            )

        }

        Log.i("New person created", person.toString())

    }

    /**
     * Fill the form with a Person object
     */
    private fun fillFormWithPerson(person: Person) {
        binding.mainBaseName.setText(person.name)
        binding.mainBaseFirstname.setText(person.firstName)

        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        binding.mainBaseBirthdateInput.setText(dateFormat.format(person.birthDay.time))
        binding.additionalEmailInput.setText(person.email)
        binding.additionalRemarksInput.setText(person.remark)
        binding.mainBaseNationality.setSelection(getIndex(binding.mainBaseNationality, person.nationality))
        if (person is Student) {
            binding.mainBaseOccupationStudent.isChecked = true
            binding.mainSpecificUniversityInput.setText(person.university)
            binding.mainSpecificGraduationyearInput.setText(person.graduationYear.toString())
        } else if (person is Worker) {
            binding.mainBaseOccupationWorker.isChecked = true
            binding.mainSpecificCompagnyInput.setText(person.company)
            binding.mainSpecificSectorInput.setSelection(getIndex(binding.mainSpecificSectorInput, person.sector))
            binding.mainSpecificExperienceInput.setText(person.experienceYear.toString())
        }
    }

    /**
     * Utility function to get the index of an item in a Spinner
     */
    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0 // Default to the first item if not found
    }


    /**
     * Display the date picker dialog
     */
    private fun showDatePickerDialog() {

        val currentDate = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)

                // Format the date depending on the region
                val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                // Update the input with the date
                binding.mainBaseBirthdateInput.setText(formattedDate)
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Annuler") { dialog, _ ->
            dialog.dismiss()
        }

        datePickerDialog.show()
    }

    /**
     * Check if the student option is selected
     */
    private fun studentSelected(checkId: Int): Boolean {
        return checkId == R.id.main_base_occupation_student
    }

    /**
     * Check if the employee option is selected
     */
    private fun employeeSelected(checkId: Int): Boolean {
        return checkId == R.id.main_base_occupation_worker
    }

    /**
     * Show a messagebox to the user
     */
    private fun showMessageDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ ->}
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * Check if an email is valid, using regex
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}