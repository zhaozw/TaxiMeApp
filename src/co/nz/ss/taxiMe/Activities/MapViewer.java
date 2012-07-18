package co.nz.ss.taxiMe.Activities;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import co.nz.ss.taxiMe.R;
import co.nz.ss.taxiMe.tripObjects.Route;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * A Map viewer displays a map and a route on the map
 * @author Niall
 *
 */
public class MapViewer extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maplayout);
		
		//Get the route
		Route route = (Route) getIntent().getSerializableExtra(getString(R.string.Route));
		
		//size the map to the display
		Display display = getWindowManager().getDefaultDisplay();
		int size = display.getWidth();
		Resources res = MapViewer.this.getResources();
		int borderSize = (int) res.getDimension(R.dimen.BoarderWidth);
		int margins = (int) res.getDimension(R.dimen.ButtonMargin);
		size = size - (borderSize + margins);
		
		//Set up map layout
		MapView map = (MapView) findViewById(R.id.map);
		map.setBuiltInZoomControls(true);
		LayoutParams mapLp = new LayoutParams(size,size);
		mapLp.height = mapLp.width;
		map.setLayoutParams(mapLp);
		
		//calculate route span
		int neLat = (int) (route.getNorthEastBound().getLatitude() * 1e6);
		int neLong = (int) (route.getNorthEastBound().getLongitude() * 1e6);
		int swLat = (int) (route.getSouthWestBound().getLatitude() * 1e6);
		int swLong = (int) (route.getSouthWestBound().getLongitude() * 1e6);
		int lonSpanE6 = neLong - swLong;
		int latSpanE6 = neLat - swLat;
		
		//Find map centre from route
		int lonCentre = neLong + lonSpanE6/2;
		int latCentre = neLat - latSpanE6/2;
		
		//Zoom and move to route
		MapController mc = map.getController();
		mc.setCenter(new GeoPoint(latCentre, lonCentre));
		mc.zoomToSpan(latSpanE6, lonSpanE6);
		
		//Display satalite TODO: make an option
		map.setSatellite(true);
		
		//Add route overlays
		ArrayList<GeoPoint> routePoints = route.getPath();
		for(int i = 0; i < routePoints.size()-1; i++){
			map.getOverlays().add(new myOverlay(routePoints.get(i), routePoints.get(i+1)));
		}
		
		//Display copyright (Required by google TOS)
		TextView copyright = (TextView) findViewById(R.id.copyRight);
		copyright.setText(route.getCopyrightString());
		
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * An overlay used to display the route
	 * @author Niall
	 *
	 */
	private class myOverlay extends Overlay{

		private GeoPoint start;
		private GeoPoint end;
		
		private int radius=6;
		
		public myOverlay(GeoPoint start, GeoPoint end){
			this.start = start;
			this.end= end;
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {
			super.draw(canvas, mapView, shadow);
			
			Projection projection = mapView.getProjection();
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(Color.BLUE); 
			
			Point point1 = new Point();
			projection.toPixels(start, point1);
			
			Point point2 = new Point();
			projection.toPixels(end, point2);
			
			paint.setStrokeWidth(5); 
			paint.setAlpha(255); 
			canvas.drawLine(point1.x, point1.y, point2.x,point2.y, paint); 

		}
		
	}

}
