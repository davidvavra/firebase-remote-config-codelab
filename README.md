# firebase-remote-config-codelab
Codelab for Launchpad Build Prague about Firebase Remote Config, Android, Analytics and Ice Hockey

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
    - parameter key is "button text", default value is "GOAL"
    - add three conditions:
      - name "goal!", random percentile, `<=33.33%`, value "GOAL!"
      - name "goal!!", random percentile, `<33.33% AND <=66.66%`, value "GOAL!!"
      - name "goal!!!", random percentile, `<66.66%`, value "GOAL!!!"
  - Folow instructions "Fetch and activate values from the server"
    - add the fetch code from the instructions to `Activity#onCreate`
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
    

