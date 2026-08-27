MotionSense is an Android fitness app built with Kotlin, Jetpack Compose, and Firebase. It tracks steps, walking activity, and cadence while allowing users to save and review workout sessions.

How It Works
Authentication: Firebase Authentication handles account creation, login, password reset, and logout.
Step Tracking: Android's TYPE_STEP_DETECTOR sensor detects each step and updates the current step count.
SPM: Cadence is calculated using the timing between detected steps and displayed as steps per minute.
Activity Mode: The app displays Walking when steps are detected and switches to Idle when movement stops.
Sessions: Users can start and end sessions. Session data is saved to Firestore under the logged-in user's account.
History: Saved sessions are loaded from Firestore and displayed on the History screen.
Profile: Displays the email of the currently logged-in Firebase user.

Main Technologies used:
Kotlin · Jetpack Compose · Firebase Authentication · Firebase Firestore · Android Sensors · ViewModel · Coroutines

Basic Flow:
Login → Start Session → Track Steps/SPM → End Session → Save to Firestore → View History
