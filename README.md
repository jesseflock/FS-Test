# FS-Test
Interview Project:
-Create your own branch.
-Add a new service to this app that inherits from the Test class.
-Have an action available in the service that will open the CameraActivity and place an image in it (any image you choose).
Once this app is installed you can start the service and run the action through adb with the following code:
adb shell am start-foreground-service com.flocksafety.android.validator/<SERVICE_NAME>
adb shell am broadcast -a <ACTION_NAME> -c <CATEGORY_NAME> com.flocksafety.android.validator The current service in there (MidAssemblyTest) can be used for basic reference.
