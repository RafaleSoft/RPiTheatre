package com.rafalesoft.org.rpitheatre;

import android.util.Log;

public class QuitTask extends QueryTask
{
	private String mc_quit = "QUIT";
	
	public QuitTask(RPiTheatre c)
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
				String cmd = strings[0];
				String command = mc_quit + cmd;
				sendQuery(command);

				String commandStatus = readString();
				Log.i("received quit status: ",commandStatus);
			}
			else
			{
				Log.e("QuitTask::doInBackground","Unable to connect to server");
			}
		}
		catch (QueryException e)
		{
			Log.e("QuitTask::doInBackground","Exception is throwed : " + e);
			return -1;
		}
		
		disconnect();
		
		return 0;
	}
}
