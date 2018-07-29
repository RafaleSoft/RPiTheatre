package com.rafalesoft.org.rpitheatre;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class RPiTheatre extends AppCompatActivity
{
    private CatalogTask catalogTask = null;
    private DetailsTask detailsTask = null;
    private QuitTask quitTask = null;

    private LinearLayout mainView = null;
    private TableLayout catalogView = null;
    private RelativeLayout movieDetail = null;
    private int currentView = -1;

    private MovieController movieController = null;


    private String server_address = "192.168.1.16";
    private int server_port = 2048;

    public final String getServerAddress() {
        return server_address;
    }

    public final int getServerPort() {
        return server_port;
    }

    public void fillTable(List<Movie> theatre)
    {
        /*
        for (final Movie movie:theatre)
        {
            View itemview = getLayoutInflater().inflate(R.layout.movie_item_layout,null);
            TableRow child = (TableRow)itemview;
            TextView ed = (TextView) child.findViewById(R.id.movieName);
            ed.setText(movie.title);
            TextView st = (TextView) child.findViewById(R.id.style_duration);
            st.setText(movie.style);
            TextView au = (TextView) child.findViewById(R.id.author);
            au.setText("By " + movie.author);

            ImageButton ib = (ImageButton)child.findViewById(R.id.movieIcon);
            if (movie.bitmap != null)
                ib.setImageBitmap(movie.bitmap);

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieDetails(movie.title);
                }
            });
            catalogView.addView(child);
        }
        */
    }

    public void fillDetails(Movie movie)
    {
        /*
        TextView t = (TextView)movieDetail.findViewById(R.id.movie_title);
        t.setText(movie.title);

        TextView s = (TextView)movieDetail.findViewById(R.id.movie_style);
        s.setText(movie.style);

        TextView b = (TextView)movieDetail.findViewById(R.id.movie_author);
        b.setText("By " + movie.author);

        TextView d = (TextView)movieDetail.findViewById(R.id.duration);
        d.setText("approx. length: " + movie.duration);

        TextView a = (TextView)movieDetail.findViewById(R.id.movie_actors);
        a.setText("With: " + movie.actors);

        TextView r = (TextView)movieDetail.findViewById(R.id.movie_resume);
        r.setText(movie.resume);

        ImageView iv = (ImageView) movieDetail.findViewById(R.id.movie_image);
        if (movie.bitmap != null)
            iv.setImageBitmap(movie.bitmap);
            */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpi_theatre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rpi_theatre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

/*
public class RPiTheatre extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		server_address = sp.getString("server_ip", "192.168.1.20");
		server_port = Integer.parseInt(sp.getString("server_port", "2048"));

		View startView = getLayoutInflater().inflate(R.layout.movie_list_layout,null);
		mainView = (LinearLayout)startView;
		catalogView = (TableLayout)mainView.findViewById(R.id.movie_list);
		setContentView(startView);

		startView = getLayoutInflater().inflate(R.layout.movie_detail_layout, null);
		movieController = new MovieController(this,startView);
		movieDetail = (RelativeLayout)startView;

		currentView = R.layout.movie_list_layout;
		catalogTask = new CatalogTask(this);
		catalogTask.execute("LIST");
	}


	private void movieDetails(String movie)
	{
		Log.i("Movie details: ",movie);
		setContentView(movieDetail);
		currentView = R.layout.movie_detail_layout;
		movieController.setCurrentMovie(movie);

		detailsTask = new DetailsTask(this);
		detailsTask.execute(movie);
	}

	@Override
    protected void onStart()
    {
    	super.onStart();        // The activity is about to become visible.
    }

    @Override
    protected void onResume()
    {
    	super.onResume();        // The activity has become visible (it is now "resumed").
    }

    @Override
    protected void onPause()
    {
    	super.onPause();        // Another activity is taking focus (this activity is about to be "paused").
    }

    @Override
    protected void onStop()
    {
    	super.onStop();        // The activity is no longer visible (it is now "stopped")
    }

    @Override
    protected void onDestroy()
    {
    	super.onDestroy();        // The activity is about to be destroyed.
    }

    @Override
	public void onBackPressed()
	{
    	if (R.layout.movie_detail_layout == currentView)
    	{
    		setContentView(mainView);
    		currentView = R.layout.movie_list_layout;
    	}
    	else if (R.layout.movie_list_layout == currentView)
    	{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.app_exit);
			builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{ 	public void onClick(DialogInterface dialog, int id)
				{
					movieController.stopMovie.onClick(null);
					finish();
				}
			});
			builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener()
			{	public void onClick(DialogInterface dialog, int id)
				{ 	}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
    	}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contextual_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		getMenuInflater().inflate(R.menu.contextual_menu, menu);
		boolean res = super.onPrepareOptionsMenu(menu);
		return res;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.settings)
		{
			Intent intent = new Intent(this,SettingsManager.class);
			startActivity(intent);
			return true;
		}
		else if (item.getItemId() == R.id.shutdown_server)
		{
			quitTask = new QuitTask(this);
			quitTask.execute("QUIT");
			return true;
		}
		else if (item.getItemId() == R.id.power_off)
		{
			quitTask = new QuitTask(this);
			quitTask.execute("OFF");
			return true;
		}
		else if (item.getItemId() == R.id.rescan)
		{
			catalogView.removeAllViews();
			catalogTask = new CatalogTask(this);
			catalogTask.execute("LIST");
			return true;
		}
		else
			return super.onOptionsItemSelected(item);
	}
}

 */