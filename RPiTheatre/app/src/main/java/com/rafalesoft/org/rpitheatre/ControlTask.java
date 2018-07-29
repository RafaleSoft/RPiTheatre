package com.rafalesoft.org.rpitheatre;

import android.util.Log;


public class ControlTask extends QueryTask {

	private static final String mc_cmd = "COMMAND";
	public static final String mc_stop = "STOP";
	public static final String mc_next = "NEXT";
	public static final String mc_prev = "PREV";
	public static final String mc_pause = "PAUSE";
	public static final String mc_volume = "VOLUME";
	
	public ControlTask(RPiTheatre c)
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
				String command = mc_cmd;
				for (String cmd:strings)
					command = command + cmd;
				
				sendQuery(command);

				String commandStatus = readString();
				Log.i("received command status: ",commandStatus);
			}
			else
			{
				Log.e("ControlTask::doInBackground","Unable to connect to server");
			}
		}
		catch (QueryException e)
		{
			Log.e("ControlTask::doInBackground","Exception is throwed : " + e);
			return -1;
		}
		
		disconnect();
		
		return 0;
	}
}
