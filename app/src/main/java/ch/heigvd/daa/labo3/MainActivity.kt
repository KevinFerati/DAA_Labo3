package ch.heigvd.daa.labo3

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.EditText
import androidx.core.view.children
import ch.heigvd.daa.labo3.databinding.ActivityMainBinding
import ch.heigvd.iict.and.labo2.Person
import ch.heigvd.iict.and.labo2.Student
import ch.heigvd.iict.and.labo2.Worker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding;
    private var currentUser: Person? = null
    private const var DATE_FORMAT = "dd/MM/yyyy"
    private const val MIN_GRADUATION_YEAR = 1900
    private const val MAX_GRADUATION_YEAR = 2023
    private const val MIN_EXPERIENCE = 0
    private const val MAX_EXPERIENCE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initEvents()

    }

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
            showMessageDialog("Séléctionner d'abord le type de personne")
            return
        }

        var name: String = binding.mainBaseName.text.toString()
        if (name == "") {
            showMessageDialog("Saisir un nom")
            return
        }
        var firstname: String = binding.mainBaseFirstname.text.toString();
        if (firstname == "") {
            showMessageDialog("Saisir un prénom")
            return
        }

        var birthday_text = binding.mainBaseBirthdateInput.text.toString()
        if (birthday_text == "") {
            showMessageDialog("Séléctionner une date de naissance")
            return
        }
        var birthday: Calendar = Calendar.getInstance();
        birthday.time =
            SimpleDateFormat(DATE_FORMAT).parse(binding.mainBaseBirthdateInput.text.toString())

        var nationality: String = binding.mainBaseNationality.selectedItem.toString();

        var email: String = binding.additionalEmailInput.text.toString();
        if (email == "") {
            showMessageDialog("Saisir un email")
            return
        }
        if (!isValidEmail(email)) {
            showMessageDialog("Mauvais format d'email")
            return
        }

        var remark: String = binding.additionalEmailInput.text.toString();

        var person: Person = if (binding.mainBaseOccupationStudent.isChecked) {
            var school: String = binding.mainSpecificSchoolInput.text.toString()
            if (school == "") {
                showMessageDialog("Séléctionner une école")
                return
            }
            var graduationYear: Int
            try {
                graduationYear = binding.mainSpecificGraduationyearInput.text.toString().toInt()
                if (graduationYear < MIN_GRADUATION_YEAR || graduationYear > MAX_GRADUATION_YEAR) {
                    showMessageDialog("La date de graduation doit être comprise entre $MIN_GRADUATION_YEAR et $MAX_GRADUATION_YEAR")
                    return
                }
            } catch (e: Exception) {
                showMessageDialog("Mauvais format d'année")
                return
            }

            Student(
                name,
                firstname,
                birthday,
                nationality,
                school,
                graduationYear,
                email,
                remark
            )


        } else {
            var company: String = binding.mainSpecificCompagnyInput.text.toString();
            if (company == "") {
                showMessageDialog("Saisir une entreprise")
                return
            }

            var sector = binding.mainSpecificSectorInput.selectedItem.toString();

            var experience: Int
            try {
                experience = binding.mainSpecificExperienceInput.text.toString().toInt()
                if (experience < MIN_EXPERIENCE || experience > MAX_EXPERIENCE) {
                    showMessageDialog("Le nombre d'années d'expériences doit être compris entre $MIN_EXPERIENCE et $MAX_EXPERIENCE")
                    return
                }
            } catch (e: Exception) {
                showMessageDialog("Mauvais format d'année d'expérience")
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
    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}