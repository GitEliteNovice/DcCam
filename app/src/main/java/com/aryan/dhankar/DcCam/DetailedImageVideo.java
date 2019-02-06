package com.aryan.dhankar.DcCam;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aryan.dhankar.DcCam.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.Calendar;


public class DetailedImageVideo extends Activity  {
    com.jsibbold.zoomage.ZoomageView image_frame;
    String image_or_video_path ;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";

    private SimpleExoPlayerView mExoPlayerView;
    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;

    private int mResumeWindow;
    private long mResumePosition;
    private FrameLayout main_media_frame;

    ProgressBar loading_progress;
    String message_type;

    //zoom in and zoom out

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image_video);

        image_or_video_path=getIntent().getStringExtra(Config.KeyName.FILEPATH);
        setUI();
        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
        }
    }

    private void setUI() {

        loading_progress=findViewById(R.id.loading_progress);
        loading_progress.setVisibility(View.VISIBLE);
        image_frame=findViewById(R.id.image_frame);
        main_media_frame=findViewById(R.id.main_media_frame);
        if (image_or_video_path.contains(".mp4")){
            message_type= Config.MessageType.VIDEO;
            image_frame.setVisibility(View.GONE);
            main_media_frame.setVisibility(View.VISIBLE);
        }else {
            message_type= Config.MessageType.IMAGE;
            image_frame.setVisibility(View.VISIBLE);
            main_media_frame.setVisibility(View.GONE);
            setImage();
        }

        File file = new File(image_or_video_path);
        int file_size = Integer.parseInt(String.valueOf((file.length()/1024)/1024));
     //   image_frame.setOnTouchListener(this);
   //     Toast.makeText(DetailedImageVideo.this,"File is "+file_size +" mb",Toast.LENGTH_SHORT).show();
    }
    private void setImage(){
        loading_progress.setVisibility(View.GONE);
        Glide.with(image_frame.getContext())
                .asBitmap()
                .load(image_or_video_path)
                .apply( new RequestOptions().signature(new ObjectKey(Calendar.getInstance().getTime()))
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        )
                .into(image_frame);
    }
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen);

        super.onSaveInstanceState(outState);
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(DetailedImageVideo.this, R.drawable.ic_fullscreen_exit_white_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        ((FrameLayout) findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(DetailedImageVideo.this, R.drawable  .ic_fullscreen));
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void initExoPlayer() {
        if (loading_progress!=null) {
            loading_progress.setVisibility(View.GONE);
        }
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        LoadControl loadControl = new DefaultLoadControl();
        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this), trackSelector, loadControl);
        mExoPlayerView.setPlayer(player);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            mExoPlayerView.getPlayer().seekTo(mResumeWindow, mResumePosition);
        }

        player.prepare(mVideoSource);
        mExoPlayerView.getPlayer().setPlayWhenReady(true);
    }


    @Override
    protected void onResume() {

        super.onResume();
        if (image_or_video_path.contains(".mp4")){

            if (mExoPlayerView == null) {

                mExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.exoplayer);
                // initFullscreenDialog();
                //initFullscreenButton();

                // String streamUrl = "https://mnmedias.api.telequebec.tv/m3u8/29880.m3u8";
                String userAgent = Util.getUserAgent(DetailedImageVideo.this, getApplicationContext().getApplicationInfo().packageName);
                DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(userAgent, null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(DetailedImageVideo.this, null, httpDataSourceFactory);
                //   Uri daUri = Uri.parse(streamUrl);

                // mVideoSource = new HlsMediaSource(daUri, dataSourceFactory, 1, null, null);
                DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

                mVideoSource = new ExtractorMediaSource(Uri.parse(image_or_video_path),
                        dataSourceFactory, extractorsFactory, null, null);
                initExoPlayer();
            }

            else {
                mExoPlayerView.getPlayer().setPlayWhenReady(true);
            }
            //  initExoPlayer();
/*
            if (mExoPlayerFullscreen) {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(UploadFeedActivity.this, R.drawable.exo_controls_next));
                mFullScreenDialog.show();
            }*/
        }
    }


    @Override
    protected void onPause() {

        super.onPause();

        if (mExoPlayerView != null && mExoPlayerView.getPlayer() != null) {
            mExoPlayerView.getPlayer().setPlayWhenReady(false);
        }

    }




}
