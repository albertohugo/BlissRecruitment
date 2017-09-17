# Bliss Recruitment App

This Android app is intended to show the list of questions ,details and allow the user to share and send the answers.

# Get the source
	git clone https://github.com/albertohugo/BlissRecruitment.git

## Setup:
Open Bliss Recruitment project in Android Studio

## Compile
Build the Bliss Recruitment project on Android Studio

## Run
Run the blissrecruitment app from Android Studio 

## App Gradle Versions
 * Compile Sdk Version 26
 * Target Sdk Version 26
 * Build Tools Version 25.0.3
 * Min Sdk Version 15
 * Gradle version 3.3
 
## External Dependences
 * Retrofit 2.0.2
 * Picasso 2.5.0

 
## To test
 
 
**Open app with the question list (with or without filter) screen URL from outside the app**

	adb shell am start -W -a android.intent.action.VIEW -d "blissrecruitment://questions?question_filter=FILTER" hugo.alberto.blissrecruitment 

**Open app with the detail screen URL from outside the app**

	adb shell am start -W -a android.intent.action.VIEW -d "blissrecruitment://questions?question_id=ID" hugo.alberto.blissrecruitment 
