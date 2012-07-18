package co.nz.ss.taxiMe.Activities;

import java.util.ArrayList;
import java.util.Collections;

import co.nz.ss.taxiMe.R;
import co.nz.ss.taxiMe.CustomViews.TaxiListElement;
import co.nz.ss.taxiMe.businessObjects.URLFactory;
import co.nz.ss.taxiMe.exceptions.connectionExceptions.ConnectionException;
import co.nz.ss.taxiMe.parsers.InfoParser;
import co.nz.ss.taxiMe.parsers.PricesParser;
import co.nz.ss.taxiMe.parsers.RouteParser;
import co.nz.ss.taxiMe.tripObjects.Route;
import co.nz.ss.taxiMe.tripObjects.Taxi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Displays info about a route
 * @author Niall
 *
 */
public class TaxiListActivity extends TabActivity{

	private String startAddress;
	private String endAddress;
	private ArrayList<Taxi> taxis;
	private Dialog dialog;
	private Route route;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taxilistlayout);
		taxis =new ArrayList<Taxi>();
		
		//Get the start and end addresses
		Intent i = getIntent();
		startAddress = i.getStringExtra(getString(R.string.FromString));
		endAddress = i.getStringExtra(getString(R.string.endString));
		
		//Initilise
		init();
	}

	/**
	 * Calle don the done button click
	 */
	public void doDone(){
		exit();
	}
	
	/**
	 * Initilises the view and objects
	 */
	private void init(){
		//Load route asyncroniously
		new infoLoader().execute();
		
		Context context = TaxiListActivity.this;
		
		//Show loading dialog
		dialog = ProgressDialog.show(context, context.getString(R.string.LOADING_TITLE),
				context.getText(R.string.LOADING_MESSAGE), true);
		dialog.show();
	}
	
	/**
	 * Setups objects once route has been loaded
	 */
	private void postInitSetup(){
		
		LinearLayout scroller = (LinearLayout) findViewById(R.id.ListScroller);
		
		Collections.sort(taxis);
		
		//Display the taxi info
		for(Taxi t: taxis){
			scroller.addView(new TaxiListElement(TaxiListActivity.this, t.getName(), 
					t.getPhoneNuber(), t.getCurrentPrice()));
		}
		
		//Set up tabs
		TabHost tabhost = getTabHost();
		
		//Intent for map view
		Intent intent = new Intent(TaxiListActivity.this, MapViewer.class);
		intent.putExtra(getString(R.string.Route), route); //Ensure map view knows about route
		
		tabhost.addTab(tabhost.newTabSpec(getString(R.string.TaxiTabHeadder)).setIndicator(getString(R.string.TaxiTabHeadder)).setContent(R.id.TaxisTab));
		tabhost.addTab(tabhost.newTabSpec(getString(R.string.MapTabHeader)).setIndicator(getString(R.string.MapTabHeader)).setContent(intent));
		tabhost.addTab(tabhost.newTabSpec(getString(R.string.RouteInfoTabHeader)).setIndicator(getString(R.string.RouteInfoTabHeader)).setContent(R.id.routeTab));
		
		//Add the route overview
		TextView routeOverview = (TextView) findViewById(R.id.routeOverview);
		routeOverview.setText(route.getOverView());
		
		//Retrive and display instructions
		String[] instructions = route.getInstructions();
		ListView lv = new ListView(TaxiListActivity.this);
		lv.setAdapter(new ArrayAdapter<String>(lv.getContext(), R.layout.listviewitem, instructions));
		LinearLayout listParent = (LinearLayout) findViewById(R.id.listContainer);
		listParent.addView(lv);
		
	}
	
	/**
	 * Get the route
	 * @return
	 * @throws ConnectionException
	 */
	private Route getRoute() throws ConnectionException{

		return RouteParser.parse(URLFactory.getRouteURL(startAddress, endAddress, true, 
				TaxiListActivity.this), TaxiListActivity.this);

	}

	/**
	 * Exits
	 */
	private void exit(){
		TaxiListActivity.this.finish();
	}
	
	/**
	 * Shows an error message with the given text
	 * @param message
	 */
	private void showErrorDialog(String message){
		AlertDialog.Builder builder = new AlertDialog.Builder(TaxiListActivity.this);
		builder.setMessage(message)
		       .setCancelable(false)
		       .setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                exit();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	/**
	 * Loads the route info asyncroniously
	 * @author Niall
	 *
	 */
	public class infoLoader extends AsyncTask<String, String, String>{

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			//If a string was returned, an error occoured
			if(result!=null){
				showErrorDialog(result);
			}
			//Otherwise, all ent well and we can continue initilising
			else{
				postInitSetup();
			}
			
			//Dismiss the loading dialog
			if(dialog!=null){
				dialog.dismiss();
				dialog=null;
			}

		}

		/**
		 * Gets the route
		 */
		@Override
		protected String doInBackground(String... params) {
			try{
				route = getRoute();
				int distance = route.getDistance();
				double[] prices = PricesParser.parsePrices(distance, TaxiListActivity.this);
				Taxi currentTaxi;
				
				for(int i = 0; i< prices.length; i++){
					String[] taxiInfo = InfoParser.parseInfo(i, TaxiListActivity.this);
					currentTaxi = new Taxi(taxiInfo[0], taxiInfo[3]);
					currentTaxi.setCurrentPrice(prices[i]);
					taxis.add(currentTaxi);
				}
				
				return null;
				
			} catch (ConnectionException e) {
				Log.e("TEST", e.getClass().getName());
				return e.getMessage();
			}
		}
		
	}
	
}
