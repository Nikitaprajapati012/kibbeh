package app.kibbeh.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import app.kibbeh.R;


/**
 * Created by archirayan on 26-Aug-16.
 */
public class Video extends Activity /*implements SurfaceHolder.Callback*/ {
    SurfaceView mSurfaceView = null;
    private MediaPlayer mp = null;
    private SurfaceHolder mFirstSurface;
    private Uri mVideoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        mp = new MediaPlayer();
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d("TAG", "First surface created!");
                mFirstSurface = surfaceHolder;
                mVideoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.offlineapp);
                if (mVideoUri != null) {
                    Toast.makeText(Video.this, "HI", Toast.LENGTH_SHORT).show();
                    mp = MediaPlayer.create(getApplicationContext(),
                            mVideoUri, mFirstSurface);

                    int videoWidth = mp.getVideoWidth();
                    int videoHeight = mp.getVideoHeight();

                    //Get the width of the screen
                    int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

                    //Get the SurfaceView layout parameters
                    android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

                    //Set the width of the SurfaceView to the width of the screen
                    lp.width = screenWidth;

                    //Set the height of the SurfaceView to match the aspect ratio of the video
                    //be sure to cast these as floats otherwise the calculation will likely be 0
                    lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);

                    //Commit the layout parameters
                    mSurfaceView.setLayoutParams(lp);
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            Toast.makeText(Video.this, "Completed", Toast.LENGTH_SHORT).show();
//                            mp.stop();
//                            mp.release();
                            mp.stop();
//                            mp = MediaPlayer.create(getApplicationContext(),
//                                    mVideoUri, mFirstSurface);
                            mp = MediaPlayer.create(getApplicationContext(),
                                    mVideoUri, mFirstSurface);

                            int videoWidth = mp.getVideoWidth();
                            int videoHeight = mp.getVideoHeight();

                            //Get the width of the screen
                            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();

                            //Get the SurfaceView layout parameters
                            android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();

                            //Set the width of the SurfaceView to the width of the screen
                            lp.width = screenWidth;

                            //Set the height of the SurfaceView to match the aspect ratio of the video
                            //be sure to cast these as floats otherwise the calculation will likely be 0
                            lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);

                            //Commit the layout parameters
                            mSurfaceView.setLayoutParams(lp);
                            mp.start();
                        }
                    });
                }
//                Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
//                        + R.raw.northernlights);
//
//
//                try {
//                    mp.setDataSource(String.valueOf(video));
//                    mp.prepare();
//                } catch (IOException e) {
//                    Toast.makeText(Video.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//
//                //Get the dimensions of the video
//                int videoWidth = mp.getVideoWidth();
//                int videoHeight = mp.getVideoHeight();
//                Log.d("Resol", "DAATA" + videoWidth + " -- " + videoHeight);
//                //Get the width of the screen
//                int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//
//                //Get the SurfaceView layout parameters
//                android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//
//                //Set the width of the SurfaceView to the width of the screen
//                lp.width = screenWidth;
//
//                //Set the height of the SurfaceView to match the aspect ratio of the video
//                //be sure to cast these as floats otherwise the calculation will likely be 0
//                lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
//
//                //Commit the layout parameters
//                mSurfaceView.setLayoutParams(lp);
//
//                //Start video
//                mp.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d("taf", "First surface destroyed!");
                Toast.makeText(Video.this, "Completed", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"
//                + R.raw.northernlights);
//
//
//        try {
//            mp.setDataSource(String.valueOf(video));
//            mp.prepare();
//        } catch (IOException e) {
//            Toast.makeText(Video.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//
//        //Get the dimensions of the video
//        int videoWidth = mp.getVideoWidth();
//        int videoHeight = mp.getVideoHeight();
//        Log.d("Resol", "DAATA" + videoWidth + " -- " + videoHeight);
//        //Get the width of the screen
//        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//
//        //Get the SurfaceView layout parameters
//        android.view.ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
//
//        //Set the width of the SurfaceView to the width of the screen
//        lp.width = screenWidth;
//
//        //Set the height of the SurfaceView to match the aspect ratio of the video
//        //be sure to cast these as floats otherwise the calculation will likely be 0
//        lp.height = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
//
//        //Commit the layout parameters
//        mSurfaceView.setLayoutParams(lp);
//
//        //Start video
//        mp.start();
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//
//    }
}
