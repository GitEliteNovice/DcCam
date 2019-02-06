package com.aryan.dhankar.DcCam;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * A {@link CameraCaptureSession.CaptureCallback} for capturing a still picture.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
abstract class ImageCaptureCallback
        extends CameraCaptureSession.CaptureCallback {

    static final int STATE_PREVIEW = 0;
    static final int STATE_LOCKING = 1;
    static final int STATE_LOCKED = 2;
    static final int STATE_PRECAPTURE = 3;
    static final int STATE_WAITING = 4;
    static final int STATE_CAPTURING = 5;

    private int state;

    ImageCaptureCallback() {
    }

    void setState(int state) {
        this.state = state;
    }

    @Override
    public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                    @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
        process(partialResult);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                   @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
        process(result);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void process(@NonNull CaptureResult result) {
        switch (state) {
            case STATE_LOCKING: {
                Integer af = result.get(CaptureResult.CONTROL_AF_STATE);
                if (af == null) {
                    break;
                }
                if (af == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED ||
                        af == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                    Integer ae = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (ae == null || ae == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                        setState(STATE_CAPTURING);
                            Log.d("captureworking:","state capturing ");
                        onReady();
                    } else {
                        setState(STATE_LOCKED);
                            Log.d("captureworking:","state locked");
                        onPrecaptureRequired();
                    }
                }
                break;
            }
            case STATE_PRECAPTURE: {
                Integer ae = result.get(CaptureResult.CONTROL_AE_STATE);
                if (ae == null || ae == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                        ae == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED ||
                        ae == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                    setState(STATE_WAITING);
                }
                break;
            }
            case STATE_WAITING: {
                Integer ae = result.get(CaptureResult.CONTROL_AE_STATE);
                if (ae == null || ae != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                    setState(STATE_CAPTURING);
                    Log.d("captureworking:","state waiting");
                    onReady();

                }
                break;
            }
        }
    }

    /**
     * Called when it is ready to take a still picture.
     */
    abstract void onReady();

    /**
     * Called when it is necessary to run the precapture sequence.
     */
    abstract void onPrecaptureRequired();

}
