package com.example.roomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.example.roomdatabase.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)

        binding.insertData.setOnClickListener {

            writeData()
        }

        binding.btnreadData.setOnClickListener {

            readData()

        }

        binding.deleteData.setOnClickListener {

            GlobalScope.launch {

                appDb.studentDao().deleteAll()
            }
        }

        binding.btnUpdate.setOnClickListener {

            updateDate()
        }

    }

    private fun updateDate(){
        val firstName = binding.etfirstName.text.toString()
        val lastName = binding.etlastName.text.toString()
        val rollNumber = binding.etidNumber.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && rollNumber.isNotEmpty()){

            GlobalScope.launch(Dispatchers.IO){

                appDb.studentDao().update(firstName, lastName, rollNumber.toInt())
            }

            binding.etfirstName.text.clear()
            binding.etlastName.text.clear()
            binding.etidNumber.text.clear()

            Toast.makeText(this@MainActivity,"Successfully Updated", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity,"Please enter your information", Toast.LENGTH_SHORT).show()

        }

    }

    private fun writeData(){
        val firstName = binding.etfirstName.text.toString()
        val lastName = binding.etlastName.text.toString()
        val rollNumber = binding.etidNumber.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && rollNumber.isNotEmpty()){

            val student = Student(
                null, firstName, lastName, rollNumber.toInt()
            )
            GlobalScope.launch(Dispatchers.IO){
                appDb.studentDao().insert(student)
            }
            binding.etfirstName.text.clear();
            binding.etlastName.text.clear();
            binding.etidNumber.text.clear()

            Toast.makeText(this@MainActivity,"Successfully Updated", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity,"Please enter your information", Toast.LENGTH_SHORT).show()

        }

    }

    private suspend fun displayData(student: Student){

        withContext(Dispatchers.Main){
            binding.tvFirstName.text = student.firstName
            binding.tvLastName.text = student.lastName
            binding.tvRollNumber.text = student.rollNo.toString()
        }
    }
    private fun readData(){

        val rollNo = binding.rollRead.text.toString()
        if(rollNo.isNotEmpty()) {
            lateinit var student: Student
            GlobalScope.launch {

                student = appDb.studentDao().findByRoll(rollNo.toInt())
                displayData(student)

            }
        }
    }
}