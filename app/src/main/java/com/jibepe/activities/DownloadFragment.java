package com.jibepe.activities;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.jibepe.util.DownloadHelper;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Famille PERIN on 28/01/2017.
 */
public class DownloadFragment extends Fragment {

    private final String TAG="DownloadFragment";

    public static DownloadFragment newInstance(URL url, File folder) {
        DownloadFragment myFragment = new DownloadFragment();

        Bundle args = new Bundle();
        args.putString("url", url.toString());
        args.putString("folder", folder.toString());
        myFragment.setArguments(args);

        return myFragment;
    }
    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    interface DownloadTaskCallbacks {
        //void onPreExecute();
        void onProgressUpdate(int percent);
        //void onCancelled();
        void onPostExecute();
    }

    private DownloadTaskCallbacks mCallbacks;
    private underDownloadFilesTask mTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (DownloadTaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        String strURL = getArguments().getString("url");
        String strFolder = getArguments().getString("folder");

        try {
            mTask = new underDownloadFilesTask(new URL(strURL), new File(strFolder));
            Log.d(TAG,"DownloadFilesTask created ");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mTask.execute("scene.mtl", "scene.obj", "iclauncher.png");

    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     * A dummy task that performs some (dumb) background work and
     * proxies progress updates and results back to the Activity.
     *
     * Note that we need to check if the callbacks are null in each
     * method in case they are invoked after the Activity's and
     * Fragment's onDestroy() method have been called.
     */
    private class underDownloadFilesTask extends AsyncTask<String, Integer, Long> {

        private URL base_url;
        private File target_folder;

        public underDownloadFilesTask (URL url, File folder) {
            super();
            base_url = url;
            target_folder = folder;
        }

        protected Long doInBackground(String... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                totalSize += DownloadHelper.DownloadFile(base_url, target_folder, urls[i]);
                publishProgress(i,count-1);
                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            int count = progress.length;
            if (count >= 2) {
                Log.d(TAG, "onProgressUpdate " + progress[0] + " / " + progress[1] + ". ");
                ((DownloadTaskCallbacks)getActivity()).onProgressUpdate(progress[0] * 100 /progress [1]);
            }
//            for (int i = 0; i < count; i++) {
//                Log.d(TAG,"onProgressUpdate");
//            }
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            Log.d(TAG,"onPostExecute");
            ((DownloadTaskCallbacks)getActivity()).onPostExecute();
            //showDialog("Downloaded " + result + " bytes");
        }
    }

}
