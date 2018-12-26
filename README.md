## CustomCamera [![API](https://img.shields.io/badge/API-15%2B-red.svg)](https://android-arsenal.com/api?level=15) [![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-CustomCamera-green.svg?style=flat )]( https://android-arsenal.com/details/1/7064 ) [![Say Thanks!](https://img.shields.io/badge/Say%20Thanks-!-1EAEDB.svg)](https://saythanks.io/to/GitEliteNovice) [![Twitter URL](https://img.shields.io/twitter/url/https/twitter.com/fold_left.svg?style=social&label=Follow%20%40elite_novice)](https://twitter.com/elite_novice)


A Custom camera Demo App. In this Demo we can take pictures and record videos too.


## Why would anyone want to use this?
If someone wants to upload pictures or videos from its own app to main the quality of app content. And if you want to add a limit to user , that user should only able to record video up to certain length.



## Pros 
1. Its handles orientations by its own. For both image and Video.
2. Both Video and image captute in one view.

## Cons
1. For now it doesn't support all the devices.

## I Need support from you guys , if you find bugs in this example please let me know. You can also contibute in this project.

# Basic Usage

This is where we start recoding video , when button long pressed .
  
     private View.OnLongClickListener recordHoldListener = new View.OnLongClickListener() {

       @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onLongClick(View pView) {
            // Do something when your hold starts here.

            Log.d("things called","onLongPressed");
            if (!startRecordingcalled){
                startRecordingVideo();
                linearTimer.startTimer();
            }


            isSpeakButtonLongPressed = true;
            return true;
        }
    };
    
This is where we stop recoding video ,we release button 
    
    private View.OnTouchListener recordTouchListener = new View.OnTouchListener() {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onTouch(View pView, MotionEvent pEvent) {
            pView.onTouchEvent(pEvent);
            // We're only interested in when the button is released.
            if (pEvent.getAction() == MotionEvent.ACTION_UP) {
                // We're only interested in anything if our speak button is currently pressed.
                if (isSpeakButtonLongPressed) {

                    Log.d("things called","onTouch");
                    stopRecordingVideo();
                    linearTimer.pauseTimer();
                    linearTimer.resetTimer();
                    startRecordingcalled=false;
                    // Do something when the button is released.
                    isSpeakButtonLongPressed = false;
                }
            }
            return false;
        }
    };
    
 We can change duration of recording by ...
        
        long duration = 30 * 1000
        
 We can a adjusting the fps range and fixing the dark preview by adding this ..
     
     mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, getRange());
     
 getRange is the method where we do our custom setting to adjust range as per our requirement.
 
     private Range<Integer> getRange() {
        CameraManager mCameraManager = (CameraManager)getActivity().getSystemService(Context.CAMERA_SERVICE);
        CameraCharacteristics chars = null;
        try {
            chars = mCameraManager.getCameraCharacteristics(mCameraId);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Range<Integer>[] ranges = chars.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);

        Range<Integer> result = null;

        for (Range<Integer> range : ranges) {
            int upper = range.getUpper();

            // 10 - min range upper for my needs
            if (upper >= 10) {
                if (result == null || upper < result.getUpper().intValue()) {
                    result = range;
                }
            }
        }
        return result;
    }

 We can change the color of progress of recoding circle from xml by these 2 
            
         timer:initialColor="@color/white"
         timer:progressColor="@color/red"
            

        <io.github.krtkush.lineartimer.LinearTimerView
            android:id="@+id/linearTimer"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            timer:radius="18dp"

            timer:strokeWidth="2dp"
            timer:startingPoint="270"
            timer:initialColor="@color/white"
            timer:progressColor="@color/red"
            android:layout_alignParentTop="true"
            android:layout_centerHorizontal="true" />
            
## Screenshots
![camera_prev1](https://user-images.githubusercontent.com/15318984/43355909-b9f91cee-9283-11e8-9f86-a245b22d3df5.png)

![camera_prev2](https://user-images.githubusercontent.com/15318984/43355910-ba34d8b0-9283-11e8-987a-8e457c8878dd.png)

# Still in development, lots of things will be add on to this.
1. Auto rotate icons.
2. Gallery like instagram
3. New features
## So stay tuned.... 

Connect With Me
-----------

Aryan Dhankar (Elite Novice)
I love making new friends, please feel free to connect with me.

<a href="https://plus.google.com/u/0/+AryanDhankar">
  <img alt="Connect me on Google+" src="/art/gplus.png" />
</a>
<a href="https://www.facebook.com/aryan.dhankar.3">
  <img alt="Connect me on Facebook" src="/art/fb.png" width="64" height="64" />
</a>
<a href="https://www.linkedin.com/in/aryan-dhankar-961b50117/">
  <img alt="Connect me on LinkedIn" src="/art/linkedin.png" />
</a>


Question / Contact Me / Hire Me
---------------------
Please feel free to ping me at **aryandhankar11@gmail.com**.
