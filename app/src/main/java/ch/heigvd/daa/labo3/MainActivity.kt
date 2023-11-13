package ch.heigvd.daa.labo3

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import androidx.core.view.children
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.iict.and.labo2.Person
import ch.heigvd.iict.and.labo2.Person.Companion.exampleStudent
import ch.heigvd.iict.and.labo2.Student
import ch.heigvd.iict.and.labo2.Worker
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "dd/MM/yyyy"
const val MIN_GRADUATION_YEAR = 1900
const val MAX_GRADUATION_YEAR = 2024
const val MIN_EXPERIENCE = 0
const val MAX_EXPERIENCE = 100

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;

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

        binding.mainBaseBirthdateInput.setOnClickListener {
            showDatePickerDialog()
        }

        binding.mainBaseBirthdateIcon.setOnClickListener {

        }

        binding.btnOk.setOnClickListener {
            saveUser()
        }

        binding.btnCancel.setOnClickListener {
            for (view in binding.root.children) {
                if (view is EditText) {
                    view.text.clear()
                }
            }
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

        var name: String = binding.mainBaseName.text.toString()
        if (name == "") {
            showMessageDialog(resources.getString(R.string.error_empty_fields))
            return
        }
        var firstname: String = binding.mainBaseFirstname.text.toString();
        if (firstname == "") {
            showMessageDialog(resources.getString(R.string.error_empty_fields))
            return
        }

        var birthday_text = binding.mainBaseBirthdateInput.text.toString()
        if (birthday_text == "") {
            showMessageDialog(resources.getString(R.string.error_invalid_birthdate))
            return
        }
        var birthday: Calendar = Calendar.getInstance();
        birthday.time =
            SimpleDateFormat(DATE_FORMAT).parse(binding.mainBaseBirthdateInput.text.toString())

        var nationality: String = binding.mainBaseNationality.selectedItem.toString();

        var email: String = binding.additionalEmailInput.text.toString();
        if (email == "") {
            showMessageDialog(resources.getString(R.string.error_select_email))
            return
        }
        if (!isValidEmail(email)) {
            showMessageDialog(resources.getString(R.string.error_invalid_email))
            return
        }

        var remark: String = binding.additionalEmailInput.text.toString();

        var person: Person = if (binding.mainBaseOccupationStudent.isChecked) {
            var university: String = binding.mainSpecificUniversityInput.text.toString()
            if (university == "") {
                showMessageDialog(resources.getString(R.string.error_select_university))
                return
            }
            var graduationYear: Int
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
            var company: String = binding.mainSpecificCompagnyInput.text.toString();
            if (company == "") {
                showMessageDialog(resources.getString(R.string.error_select_company))
                return
            }

            var sector = binding.mainSpecificSectorInput.selectedItem.toString();

            var experience: Int
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
        val calendarView = CalendarView(this)
        val currentDate = Calendar.getInstance()
        calendarView.date = currentDate.timeInMillis

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(calendarView)
            .setPositiveButton("OK") { dialog, _ ->
                var selectedDate: CharSequence = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
                    .format(Date(calendarView.date))
                // Mettez à jour votre EditText avec la date sélectionnée
                val birthdateInput = findViewById<EditText>(R.id.main_base_birthdate_input)
                birthdateInput.setText(selectedDate)
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
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
        builder.setPositiveButton("OK") { _, _ ->
            // Action à effectuer lorsque l'utilisateur appuie sur le bouton "OK"
        }
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