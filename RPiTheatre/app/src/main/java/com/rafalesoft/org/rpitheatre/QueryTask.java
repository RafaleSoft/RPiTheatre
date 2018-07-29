package com.rafalesoft.org.rpitheatre;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;


public class QueryTask extends AsyncTask<String, Integer, Integer>
{
	private Socket m_s = new Socket();
	private final int mc_timeOut = 5000;
	
	public RPiTheatre m_pContext = null;
	private String res = "";
	

	class QueryException extends Exception
	{
		private static final long serialVersionUID = 2245098619422682226L;
		
		String msg;
		public QueryException(String cause) {
			msg = cause;
		}
		
		public String getMessage() {
			return msg;
		}
	}
	
	public QueryTask(RPiTheatre c)
	{
		m_pContext = c;
	}
	
	public boolean isConnected() {
		return m_s.isConnected();
	}
	
	public void sendQuery(String query) throws QueryException
	{
		try
		{
			Log.d("QueryTask::sendQuery: ",query);
			m_s.getOutputStream().write(query.getBytes());
		}
		catch (IOException e)
		{
			throw new QueryException("Error while sending query");
		}
	}

	public String readString() throws QueryException
	{
		int stringSize = readShort();
		byte[] b = readRaw(stringSize);
		String string = new String(b);
		return string;
	}
	
	public byte[] readData() throws QueryException
	{
		int dataSize = readInt();
		byte [] data = readRaw(dataSize);
		return data;
	}
	
	public int readInt() throws QueryException
	{
		byte [] data = readRaw(8);
		int readint = Integer.parseInt(new String(data));
		return readint;
	}
	
	public int readShort() throws QueryException
	{
		byte [] data = readRaw(4);
		int readint = Integer.parseInt(new String(data));
		return readint;
	}
	
	public byte[] readRaw(int dataSize) throws QueryException
	{
		Log.d("QueryTask::readRaw: ","will read "+Integer.toString(dataSize)+" bytes");
		
		byte [] data = new byte[dataSize];
		try
		{
			int totalReceived = 0;
			while (totalReceived < dataSize)
			{
				int received = m_s.getInputStream().read(data,totalReceived,dataSize-totalReceived);
				if (received == -1)
				{
					Log.e("QueryTask::readRaw",
						  "Error when receiving raw bytes chunk");
					throw new QueryException("Error while receiving raw data: unexpected end of stream");
				}
				totalReceived += received;
			}
			Log.d("QueryTask::readRaw: ","received "+Integer.toString(totalReceived)+" bytes");
		}
		catch(IOException e)
		{
			Log.e("QueryTask::readRaw",
				  "Error while reading raw bytes chunk: "+e.getMessage());
			throw new QueryException("Error while receiving raw bytes chunk");
		}
		return data;
	}
	
	protected boolean connect(String address, int port)
	{
		try
		{
			InetSocketAddress addr = new InetSocketAddress(address,port);
	    	m_s.connect(addr,mc_timeOut);
	    	
	    	return true;
		}
		catch (IOException e)
		{
			Log.e("QueryTask::onPreExecute",
				  "Error while opening connection: "+e.getMessage());
			res = "IOException: " + e.getMessage();
			return false;
		}
		catch(IllegalArgumentException e)
		{
			res = "IllegalArgumentException: " + e.getMessage();
			Log.e("QueryTask::onPreExecute",
				  "Unknown Error while opening connection: "+e.getMessage());
			return false;
		}
		catch(Exception e)
		{
			Log.e("QueryTask::onPreExecute",
					"Exception is throwed : " + e.getMessage());
			res = "Exception: " + e.getMessage();
			return false;
		}
	}
	
	protected void disconnect()
	{
		try
		{
			m_s.close();
		}
		catch (IOException e)
		{
			Log.e("QueryTask::onPostExecute",
				  "Error while closing connection: "+e.getMessage());
		}
	}

	@Override
	protected Integer doInBackground(String... params) {
		return null;
	}
	
	@Override
	protected void onPostExecute(Integer result)
	{
		if (result < 0)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(m_pContext);
			builder.setMessage("Connect failed: " + res + ". Exit ?");
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
			{ 	public void onClick(DialogInterface dialog, int id) 
				{
					m_pContext.finish();
				}
			});
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() 
			{	public void onClick(DialogInterface dialog, int id)
				{ 	}
			});
			AlertDialog mDialog = builder.create();
			mDialog.show();
		}
	}
}
