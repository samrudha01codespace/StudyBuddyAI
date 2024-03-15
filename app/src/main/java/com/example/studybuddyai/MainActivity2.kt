package com.example.studybuddyai

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity2 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var marksAdapter: MarksAdapter
    private lateinit var marksList: MutableList<Marks>
    private lateinit var currentUser: String
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        recyclerView = findViewById(
            R.id.recyclerViewHistory
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        marksList = mutableListOf()
        marksAdapter = MarksAdapter(marksList)
        recyclerView.adapter = marksAdapter

        currentUser = FirebaseAuth.getInstance().currentUser?.email?.replace(".", "_") ?: ""
        database = FirebaseDatabase.getInstance().getReference().child(currentUser)
        retrieveHistory()
    }

    private fun retrieveHistory() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the list before adding new items
                marksList.clear()

                // Iterate through the dataSnapshot to get the marks data
                for (marksSnapshot in dataSnapshot.children) {
                    val marksValue = marksSnapshot.getValue(String::class.java)
                    marksValue?.let {
                        val id = marksSnapshot.key ?: "" // Get the key as the id
                        val marks = Marks(
                            id,
                            it,
                            marksValue
                        ) // Create an instance of Marks with the marks value
                        marksList.add(marks) // Add the instance to the list
                    }
                }
                marksAdapter.notifyDataSetChanged() // Notify adapter about data change
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.e(TAG, "Failed to read marks data", databaseError.toException())
            }
        })
    }


    companion object {
        private const val TAG = "MainActivity2"
    }
}
