package com.rafalesoft.org.rpitheatre;


import android.graphics.BitmapFactory;
import android.util.Log;


public class DetailsTask extends QueryTask
{
	private final String mc_details = "DETAILS";
	private Movie movie = new Movie();

	
	public DetailsTask(RPiTheatre c)
	{
		super(c);
	}
	
	@Override
	protected Integer doInBackground(String... strings)
	{
		if (!connect(m_pContext.getServerAddress(),m_pContext.getServerPort()))
			return -1;
		
		try
		{
			if (isConnected())
			{
				movie.title = strings[0];
				String details = mc_details + movie.title;
				sendQuery(details);

				movie.style = readString();
				Log.i("received style: ",movie.style);
				
				movie.author = readString();
				Log.i("received author: ",movie.author);

				movie.actors = readString();
				Log.i("received actors: ",movie.actors);
				
				movie.duration = readString();
				Log.i("received duration: ",movie.duration);
				
				movie.resume = readString();
				Log.i("received resume: ",movie.resume);
				
				movie.image = readString();
				Log.i("received image: ",movie.image);
				
				movie.bitmap = null;
				byte [] data = readData();
				if (data.length > 0)
					movie.bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			}
			else
			{
				Log.e("DetailsTask::doInBackground","Unable to connect to server");
			}
		}
		catch (QueryException e)
		{
			Log.e("DetailsTask::doInBackground","Exception is throwed : " + e);
			return -1;
		}
		
		disconnect();
		
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer result)
	{
		super.onPostExecute(result);
		m_pContext.fillDetails(movie);
	}
}
