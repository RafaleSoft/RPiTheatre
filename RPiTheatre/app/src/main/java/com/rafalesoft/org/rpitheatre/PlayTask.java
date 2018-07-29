package com.rafalesoft.org.rpitheatre;

import android.util.Log;

public class PlayTask extends QueryTask
{
	private final String mc_play = "PLAY";
	
	public PlayTask(RPiTheatre c)
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
				String title = strings[0];
				String details = mc_play + title;
				sendQuery(details);
				
				String playStatus = readString();
				Log.i("received play status: ",playStatus);
			}
			else
			{
				Log.e("PlayTask::doInBackground","Unable to connect to server");
			}
		}
		catch (QueryException e)
		{
			Log.e("PlayTask::doInBackground","Exception is throwed : " + e);
			return -1;
		}
		
		disconnect();
		
		return 0;
	}
}
