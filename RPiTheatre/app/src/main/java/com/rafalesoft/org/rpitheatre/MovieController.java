package com.rafalesoft.org.rpitheatre;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class MovieController
{
	private RPiTheatre theatre = null;
	private String currentMovie = new String("");
	private String playingMovie = new String("");
	private PlayTask playtask = null;
	private ControlTask controlTask = null;
	
	private PlayMovie playMovie = new PlayMovie();
	private NextMovie nextMovie = new NextMovie();
	private PrevMovie prevMovie = new PrevMovie();
	private PauseMovie pauseMovie = new PauseMovie();
	private PreviewMovie previewMovie = new PreviewMovie();
	//private VideoView videoView = null;
	
	private OnSeekBarChangeListener volumeControl = new VolumeControl();
	
	public StopMovie stopMovie = new StopMovie();
	
	
	public MovieController(RPiTheatre rPiTheatre,View controllerView)
	{
		theatre = rPiTheatre;
		/*
		ImageButton ib = (ImageButton) controllerView.findViewById(R.id.movie_play);
		ib.setOnClickListener(playMovie);
		ib = (ImageButton) controllerView.findViewById(R.id.movie_next);
		ib.setOnClickListener(nextMovie);
		ib = (ImageButton) controllerView.findViewById(R.id.movie_prev);
		ib.setOnClickListener(prevMovie);
		ib = (ImageButton) controllerView.findViewById(R.id.movie_pause);
		ib.setOnClickListener(pauseMovie);
		Button b = (Button) controllerView.findViewById(R.id.movie_stop);
		b.setOnClickListener(stopMovie);
		b = (Button)controllerView.findViewById(R.id.preview);
		b.setOnClickListener(previewMovie);
		TextView tv = (TextView)controllerView.findViewById(R.id.movie_resume); 
		tv.setMovementMethod(new ScrollingMovementMethod());

		volumeControl = new VolumeControl();
		SeekBar volume = (SeekBar)controllerView.findViewById(R.id.volume_slider);
		volume.setMax(25);
		volume.setProgress(25);
		volume.setOnSeekBarChangeListener(volumeControl);
		*/
	}
	
	public void setCurrentMovie(String movie)
	{
		currentMovie = movie;
	}
	
	public class PreviewMovie implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			Log.i("MovieController::previewMovie", currentMovie);
			/*
			File sdcard = Environment.getExternalStorageDirectory();
			String url = "rtp://192.168.1.27:2048"; //"udp://192.168.1.27:2048"; // your URL here
			MediaPlayer mediaPlayer = new MediaPlayer();
			try {
				mediaPlayer.setDataSource(sdcard.getAbsolutePath()+"RPiTheatre/config.sdp");
				mediaPlayer.prepare(); // might take long! (for buffering, etc)
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaPlayer.start();
			*/
		}	
	}
	public class PlayMovie implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Log.i("MovieController::playMovie", currentMovie);
			if (currentMovie.compareTo(playingMovie) != 0)
			{
				if (!playingMovie.isEmpty())
					pauseMovie.onClick(v);
				
				playtask = new PlayTask(theatre);
				playtask.execute(currentMovie);
				playingMovie = currentMovie;
			}
		}	
	}
	public class NextMovie implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			if (!playingMovie.isEmpty())
			{
				Log.i("MovieController::nextChapter", currentMovie);
				controlTask = new ControlTask(theatre);
				controlTask.execute(ControlTask.mc_next);
			}
			else
				Log.i("MovieController::nextChapter", "no current movie playing");
		}	
	}
	public class PrevMovie implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			Log.i("MovieController::prevMovie", currentMovie);
			if (!playingMovie.isEmpty())
			{
				Log.i("MovieController::prevChapter", currentMovie);
				controlTask = new ControlTask(theatre);
				controlTask.execute(ControlTask.mc_prev);
			}
			else
				Log.i("MovieController::prevChapter", "no current movie playing");
		}	
	}
	public class PauseMovie implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			if (!playingMovie.isEmpty())
			{
				Log.i("MovieController::pauseMovie", currentMovie);
				controlTask = new ControlTask(theatre);
				controlTask.execute(ControlTask.mc_pause);
			}
			else
				Log.i("MovieController::pauseMovie", "no current movie playing");
		}	
	}
	public class StopMovie implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			if (!playingMovie.isEmpty())
			{
				Log.i("MovieController::pauseMovie", currentMovie);
				controlTask = new ControlTask(theatre);
				controlTask.execute(ControlTask.mc_stop);
				playingMovie = "";
			}
			else
				Log.i("MovieController::pauseMovie", "no current movie playing");
		}	
	}
	public class VolumeControl implements OnSeekBarChangeListener
	{
		@Override
		public void onProgressChanged(	SeekBar seekBar,
										int progress,
										boolean fromUser)
		{
			if (!playingMovie.isEmpty())
			{
				Log.i("MovieController::volumeControl", currentMovie);
				//if (fromUser)
				controlTask = new ControlTask(theatre);
				controlTask.execute(ControlTask.mc_volume,Integer.toString(progress));
			}
			else
				Log.i("MovieController::volumeControl", "no current movie playing");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			System.out.println("Start tracking touch");
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			System.out.println("Stop tracking touch");
		}
	}
}
