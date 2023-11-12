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
            } else if(employeeSelected(checkId)) {
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


    private fun saveUser() {
        if(!binding.mainBaseOccupationStudent.isChecked && !binding.mainBaseOccupationWorker.isChecked){
            showMessageDialog(this, "Séléctionner d'abord le type de personne")
            return
        }

        var name : String = binding.mainBaseName.text.toString()
        if(name == ""){
            showMessageDialog(this, "Saisir un nom")
            return
        }
        var firstname : String = binding.mainBaseFirstname.text.toString();
        if(firstname == ""){
            showMessageDialog(this, "Saisir un prénom")
            return
        }

        var birthday_text = binding.mainBaseBirthdateInput.text.toString()
        if(birthday_text == ""){
            showMessageDialog(this, "Séléctionner une date de naissance")
            return
        }
        var birthday : Calendar = Calendar.getInstance();
        birthday.time = SimpleDateFormat("dd/MM/yyyy").parse(binding.mainBaseBirthdateInput.text.toString())

        var nationality : String = binding.mainBaseNationality.selectedItem.toString();

        var email : String = binding.additionalEmailInput.text.toString();
        if(email == ""){
            showMessageDialog(this, "Saisir un email")
            return
        }
        if(!isValidEmail(email)){
            showMessageDialog(this, "Mauvais format d'email")
            return
        }

        var remark : String = binding.additionalEmailInput.text.toString();

        var person: Person

        if(binding.mainBaseOccupationStudent.isChecked){
            var school: String = binding.mainSpecificSchoolInput.text.toString()
            if(school == ""){
                showMessageDialog(this, "Séléctionner une école")
                return
            }
            var graduationYear: Int
            try {
                graduationYear = binding.mainSpecificGraduationyearInput.text.toString().toInt()
                if(graduationYear<1900 || graduationYear > 2023){
                    showMessageDialog(this, "La date de graduation doit être comprise entre 1900 et 2023")
                    return
                }
            }catch(e: Exception){
                showMessageDialog(this, "Mauvais format d'année")
                return
            }

            person = Student(name, firstname, birthday, nationality, school, graduationYear, email, remark)


        }
        else{
            var company:String = binding.mainSpecificCompagnyInput.text.toString();
            if(company == ""){
                showMessageDialog(this, "Saisir une entreprise")
                return
            }

            var sector = binding.mainSpecificSectorInput.selectedItem.toString();

            var experience : Int
            try {
                experience = binding.mainSpecificExperienceInput.text.toString().toInt()
                if(experience<0 || experience > 100){
                    showMessageDialog(this, "Le nombre d'années d'expériences doit être compris entre 0 et 100")
                    return
                }
            }
            catch(e: Exception){
                showMessageDialog(this, "Mauvais format d'année d'expérience")
                return
            }

            person = Worker(name, firstname, birthday, nationality, company, sector, experience, email, remark)

        }

        Log.i("New person created", person.toString())

    }

    private fun showDatePickerDialog() {
        val calendarView = CalendarView(this)
        val currentDate = Calendar.getInstance()
        calendarView.date = currentDate.timeInMillis

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(calendarView)
            .setPositiveButton("OK") { dialog, _ ->
                var selectedDate : CharSequence = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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

    private fun studentSelected(checkId: Int): Boolean {
        return checkId == R.id.main_base_occupation_student
    }

    private fun employeeSelected(checkId: Int): Boolean {
        return checkId == R.id.main_base_occupation_worker
    }

    private fun showMessageDialog(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ ->
            // Action à effectuer lorsque l'utilisateur appuie sur le bouton "OK"
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }
}