package com.rafalesoft.org.rpitheatre;

import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CatalogTask extends QueryTask
{
	List<Movie> theatre = new ArrayList<Movie>();
	
	private final String mc_catalog = "CATALOG";
	private ProgressDialog progress = null; 
	private int max = -1;
	
	public CatalogTask(RPiTheatre c)
	{
		super(c);
		progress = new ProgressDialog(c);
		progress.setTitle("Querying catalog from server...");
		progress.setMessage("Plese wait...");
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.show();
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
				String catalog = mc_catalog + strings[0];
				sendQuery(catalog);
			
				int catalogSize = readShort();
				if (catalogSize == -1)
				{
					Log.e("CatalogTask::doInBackground",
						  "Error when receiving answer of CATALOG message");
					return -1;
				}
				publishProgress(catalogSize);
				
				for (int i=0;i<catalogSize;i++)
				{
					Movie movie = new Movie();
					movie.title = readString();
					Log.i("received movie: ",movie.title);
					
					movie.style = readString();
					Log.i("received style: ",movie.style);
					
					movie.author = readString();
					Log.i("received author: ",movie.author);
					
					movie.image = readString();
					Log.i("received image: ",movie.image);
					
					movie.bitmap = null;
					byte [] data = readData();
					if (data.length > 0)
						movie.bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

					theatre.add(movie);
					
					publishProgress(i+1);
				}
			}
			else
			{
				Log.e("CatalogTask::doInBackground","Unable to connect to server");
			}
		}
		catch (QueryException e)
		{
			Log.e("CatalogTask::doInBackground","Exception is throwed : " + e.getMessage());
			return -1;
		}

		disconnect();
		
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer result)
	{
		super.onPostExecute(result);
		// This final update should not be necessary.
		m_pContext.fillTable(theatre);
		
		if (progress != null)
			progress.dismiss();
		progress = null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... progress)
	{
		if (max < 0)
		{
			max = progress[0];
			this.progress.setMax(max);
		}
		else
		{
			this.progress.setProgress(progress[0]);
			m_pContext.fillTable(theatre);
			theatre.clear();
		}
		super.onProgressUpdate(progress);
    }

}
