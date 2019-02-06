<<<<<<< HEAD
package com.aryan.dhankar.DcCam;
=======
package com.aryan.dhankar.customcamera;
>>>>>>> 3467ea8c7d9d53fc3d4e32bb657d0c30142bf7ee

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

<<<<<<< HEAD
        assertEquals("com.aryan.dhankar.DcCam", appContext.getPackageName());
=======
        assertEquals("com.aryan.dhankar.customcamera", appContext.getPackageName());
>>>>>>> 3467ea8c7d9d53fc3d4e32bb657d0c30142bf7ee
    }
}
