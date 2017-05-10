# Firebase Remote Config Codelab
This codelab which will show you how to integrate [Firebase Remote Config](https://firebase.google.com/docs/remote-config/) into your app and use it for A/B testing. Then you will learn how to measure results of A/B testing in [Firebase Analytics](https://firebase.google.com/docs/analytics/).

The sample app is for hockey fans - it plays a goal sound after you press a button. Text on the button is A/B tested.

The codelab was prepared originally for Lauchpad Build Prague (May 2017) event. It took attendees about 40 minutes to complete.

## Prerequisites

  - latest Android Studio
  - connected phone or emulator
  
## 1. Make sure everything is setup correctly
  - Clone the code from this repo (or download ZIP)
  - Import project "Hockey Goal" into Android Studio
  - Run the app on your device/emulator to make sure everything works
    - If you get an error during running it, click "Sync with Gradle" icon
    
## 2. Setup Firebase Remote Config
  - Click `Tools->Firebase` in Android Studio
  - Select Remote Config from menu
  - Follow instructions "Connect your app to Firebase"
  - Follow instructions "Add Remote Config to your app"
    - Setup `mFirebaseRemoteConfig` in `MainActivity#onCreate`
  - Follow instructions "Set in-app default parameter values"
    - add one record with key "button_text" and value "GOAL" to the file `res/xml/remote_config_defaults.xml`
  - Folow instructions "Set parameter values on the server"
    - parameter key is "button_text", default value is "GOAL"
    - add three conditions:
      - name "goal!", random percentile, `<33.33%`, value "GOAL!"
      - name "goal!!", random percentile, `>=33.33% AND <66.66%`, value "GOAL!!"
      - name "goal!!!", random percentile, `>=66.66%`, value "GOAL!!!"
      - (don't forget to click 'Publish' button :)
  - Folow instructions "Fetch and activate values from the server"
    - add the fetch code from the instructions to `MainActivity#onCreate`
    - add following code before that to make sure you get fresh data in debug builds:
    
```
mFirebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                     .setDeveloperModeEnabled(BuildConfig.DEBUG)
                     .build());
int cacheExpiration = (BuildConfig.DEBUG)?  0 : 3600; // Cache is invalid after an hour in production
```
    
  - Folow instructions "Get parameter values to use in your app"
    - add the code to method `getButtonText`
  - Run the code on the phone
    - You should see changed text of the button second time you run the app (it's correct behavior, we don't want to change UI to the user on the fly)
    
## 3. Integrate it with Firebase Analytics
  - Click `Tools-Firebase` in Android Studio
  - Select Analytics and "Log Analytics event" from the menu
  - Follow "Add Analytics to your app"
    - Use Android Studio quick fix to add required permissions
  - Follow "Log events"
    - Log event when user clicks GOAL button
      - use "play" as value, null bundle
    - Log event when user clicks STOP button
      - use "stop" as value, null bundle
  - Run command `adb shell setprop debug.firebase.analytics.app google.codelab.hockeygoal`. It enables instant sending of analytics events to the console (otherwise there is 24 hour delay)
  - Go to Firebase Console, check out StreamView and DebugView - you should see some events there
  - Go to User Properties in Firebase Console/Analytics
  - Create new property called "button_text"
  - Add this code to `MainActivity#onCreate`:
    - `mFirebaseAnalytics.setUserProperty("button_text", mFirebaseRemoteConfig.getString("button_text"));`
  - Launch the app and make sure the property is set in DebugView
  - Now you could filter all Analytics reports based on user property and compare how users are behaving based on which button text they have. But data will be available only after 24 hours from SDK integration.
  
