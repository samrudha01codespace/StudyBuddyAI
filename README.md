# StudyBuddy-AI

![Studybuddy-AI](https://github.com/samrudha01codespace/StudyBuddyAI/assets/144599345/c5a9e444-df5c-415c-b550-e1684ec93070)


StudyBuddy-AI is powerful AI app for education , solving of image , grammer correction and all.

## Screenshots:

**Splash Screen:**

![3](https://github.com/samrudha01codespace/StudyBuddyAI/assets/144599345/16243f66-952d-47f0-bc98-8dd1738f21d6)

**Login Page:**

![2](https://github.com/samrudha01codespace/StudyBuddyAI/assets/144599345/7f18bda5-a5d0-4b2a-8d33-8efa025e46f7)

**Image Solver:**

![4](https://github.com/samrudha01codespace/StudyBuddyAI/assets/144599345/5fab645e-3b32-4360-8029-66aae8ea0888)

**Grammer Corrector:**

![1](https://github.com/samrudha01codespace/StudyBuddyAI/assets/144599345/ddbf1d24-a31a-4600-861e-3ea370d60b26)


## Features

1. **Image Upload and Query Solver**
   - Users can upload images such as posters, school study diagrams, etc., and ask queries related to them.
   - The app provides solutions and answers to user queries based on the uploaded images.

2. **Grammar Correction**
   - Users can use the grammar correction feature by speaking English sentences into the app.
   - The app corrects the grammar of the spoken sentences and displays the corrected version.
   - Marks are calculated based on the correctness of the grammar in the spoken sentence.

## Getting Started

To use StudyBuddy-AI, follow these steps:

1. **Installation**
   - Download and install StudyBuddy-AI from this link - A) "https://drive.google.com/file/d/1LqHvS6o_xvjIpeE_3mkvSceGrLsPBNs6/view?usp=sharing" or B) "https://fastupload.io/yDDIvvHPSIqbA6T/file".
   - Ensure that your device has a working microphone for the grammar correction feature.
     
2. **Using Image Upload and Query Solver**
   - Navigate to the "Image Upload" screen.
   - Upload the image you want to ask queries about.
   - Type or speak your query and submit it.
   - Receive solutions and answers to your queries.

3. **Using Grammar Correction**
   - Navigate to the "Grammar Correction" screen.
   - Click on the microphone button to start speaking.
   - Speak an English sentence that you want to correct.
   - Stop speaking, and the corrected sentence will be displayed along with marks calculation.
  
4. **Note:** Before installation , In Android Studio: Go to chatgptfile -> utils -> constants.kt -> add this api key "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" Note : This is an important to add API Key in the code.(This is required when you will run code , not in APK)

5. ## Getting start with code

    (dependencies)
    
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    implementation ("com.google.android.gms:play-services-vision:10.0.0+")

    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation ("com.google.mlkit:text-recognition:16.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")



   (IN App>scr>main>com/samrudhasolutions/bolo>utils>constants)

    
    const val BASE_URL="https://api.openai.com/v1/"
    const val CHATGPT_MODEL="gpt-3.5-turbo"
    const val OPENAI_API_KEY="write your api key"  <- write your api key
    var ANSBEFOREEDIT=""
    var ANSWERAFTEREDIT=""
  
6. **Video Link of Instruction of App** - "https://www.youtube.com/watch?v=MKcIfZG_Qew".

## Technologies Used

- Android Studio
- Kotlin for Android development
- Speech-to-text and grammar correction APIs

## Feedback and Support

For any feedback, suggestions, or support issues, please contact us at samrudhakshirsagar@gmail.com.
