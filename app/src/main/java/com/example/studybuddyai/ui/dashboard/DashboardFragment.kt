package com.example.studybuddyai.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.picsolver20.chatgptfiles.respository.ChatRepository
import com.example.studybuddyai.MainActivity2
import com.example.studybuddyai.R
import com.example.studybuddyai.algorithm.Algorithm
import com.example.studybuddyai.chatgptfiles.utils.ANSWERAFTEREDIT
import com.example.studybuddyai.chatgptfiles.utils.NetworkConnectivityObserver
import com.example.studybuddyai.chatgptfiles.utils.NetworkStatus
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.util.Locale

class DashboardFragment : Fragment() {
    private lateinit var ekharttd: ImageButton
    private lateinit var jghbjri: Button
    private lateinit var closeMic: ImageButton
    private lateinit var statusTextView: TextView
    private lateinit var statuscorrectedTextView: TextView
    private lateinit var generatingMarks: TextView
    private val networkConnectivityObserver: NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(requireContext())
    }


    private var audioRecord: AudioRecord? = null
    private var mediaPlayer: MediaPlayer? = null


    private val speechRecognizer: SpeechRecognizer by lazy {
        SpeechRecognizer.createSpeechRecognizer(requireContext())
    }

    private val speechRecognizerIntent: Intent by lazy {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }


    private fun move(destinationActivity: Class<*>) {
        val intent = Intent(requireContext(), destinationActivity)
        startActivity(intent)
    }


    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        ekharttd = view.findViewById(R.id.ekharttd)
        closeMic = view.findViewById(R.id.openMic)
        jghbjri = view.findViewById(R.id.jghbjri)
        generatingMarks = view.findViewById(R.id.generatingMarks)
        statuscorrectedTextView = view.findViewById(R.id.statusTextView)


        val snackbar =
            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "No Internet Connection",
                Snackbar.LENGTH_INDEFINITE
            ).setAction("Wifi")
            {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }
        networkConnectivityObserver.observe(requireActivity()) {
            when (it) {
                NetworkStatus.Available -> {
                    if (snackbar.isShown) {
                        snackbar.dismiss()
                    }
                }

                else -> {
                    snackbar.show()
                }
            }
        }

        statusTextView = view.findViewById(R.id.statusMicTextView)


        ekharttd = view.findViewById(R.id.ekharttd)

        jghbjri.setOnClickListener {
            move(MainActivity2::class.java)
        }

        ekharttd.setOnClickListener {
            convertAudioToText()
            ekharttd.setBackgroundResource(R.drawable.hululu1)
            initializeSpeechRecognizer()
        }


        return view
    }

    private fun initializeSpeechRecognizer() {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {
                //here write the code for visualizer while input as rms
                val amplitudes = FloatArray(1)

                amplitudes[0] = rmsdB

                Log.d("Visualizer", "RMS value: $rmsdB")

            }

            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                ekharttd.setBackgroundResource(R.drawable.hululu)
            }
            override fun onError(error: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onResults(results: Bundle?) {
                generatingMarks(results)
            }


            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    @SuppressLint("SetTextI18n")
    fun generatingMarks(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val recognizedText = matches[0]
            statusTextView.text = "Speech Recognized: $recognizedText"
            val myInstance = ChatRepository()
            lifecycleScope.launch {
                myInstance.sendToChatGPT(
                    "Correct the grammar",
                    recognizedText
                )  // work to do
                statuscorrectedTextView.text = "Corrected: $ANSWERAFTEREDIT"

                val algo = Algorithm()
                val hululu = algo.calculateMarks(recognizedText, ANSWERAFTEREDIT)
                generatingMarks.text = "${hululu}/10"

                val marksCategory = when {
                    hululu >= 7 -> "Good."
                    hululu >= 5 -> "Okay."
                    else -> "Need to do Practice."
                }

                // Display toast message with marks range category
                Toast.makeText(
                    requireContext(),
                    "Marks range category: $marksCategory",
                    Toast.LENGTH_SHORT
                ).show()

                // Save generated marks to Firebase Realtime Database
                val database = Firebase.database
                val userName = FirebaseAuth.getInstance().currentUser?.email
                val encodedEmail = userName?.replace(".", "_") ?: ""
                val marksRef = database.getReference().child(encodedEmail).push()
                val generatedMarks = hululu.toString()

                marksRef.setValue(generatedMarks).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(ContentValues.TAG, "Marks saved successfully")
                    } else {
                        Log.e(ContentValues.TAG, "Failed to save marks: ${task.exception}")
                    }
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MICROPHONE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start listening for speech
                    speechRecognizer.startListening(speechRecognizerIntent)
                    // Show toast message indicating microphone is activated
                    Toast.makeText(requireContext(), "Microphone activated", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission denied, show toast message
                    Toast.makeText(requireContext(), "Microphone permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun convertAudioToText() {
        // Check and request microphone permission if not granted
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                MICROPHONE_PERMISSION_REQUEST
            )
        } else {
            // Permission already granted, start listening for speech
            speechRecognizer.startListening(speechRecognizerIntent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        audioRecord?.release()
        mediaPlayer?.release()
        speechRecognizer.destroy()
    }

    companion object {

        private const val MICROPHONE_PERMISSION_REQUEST = 123
    }
}

