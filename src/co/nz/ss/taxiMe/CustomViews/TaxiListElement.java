package co.nz.ss.taxiMe.CustomViews;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaxiListElement extends BorderedLinearLayout {
	
	private String phone;
	private Context context;

	public TaxiListElement(Context context, String taxiName, String phone, double cost) {
		super(context);
		
		this.context = context;
		
		this.phone = phone;
		
		Resources res = context.getResources();
		int margin = (int) res.getDimension(R.dimen.ListViewTextMargin);
		
		this.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams rootLp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		rootLp.setMargins(margin, margin, margin, margin);
		this.setLayoutParams(rootLp);
		
		LinearLayout taxiInfoLayout = new LinearLayout(context);
		LayoutParams taxiInfoParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 50);
		taxiInfoLayout.setOrientation(LinearLayout.VERTICAL);
		taxiInfoParams.setMargins(margin, margin, margin, margin);
		taxiInfoLayout.setLayoutParams(taxiInfoParams);
		
		TextView taxiNameView = new TextView(context);
		LayoutParams taxiNameParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		taxiNameParams.setMargins(margin, margin, margin, margin);
		taxiNameView.setLayoutParams(taxiNameParams);
		taxiNameView.setTextColor(res.getColor(R.color.ViewTextColor));
		taxiNameView.setTextSize(res.getDimension(R.dimen.TaxiNameSize));
		taxiNameView.setText(taxiName);
		
		TextView taxiPhoneView = new TextView(context);
		LayoutParams taxiPhoneParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		int indent = (int) res.getDimension(R.dimen.TaxiPhoneLeftIndent);
		taxiPhoneParams.setMargins(indent, margin, margin, margin);
		taxiPhoneView.setLayoutParams(taxiPhoneParams);
		taxiPhoneView.setTextColor(res.getColor(R.color.ViewTextColor));
		taxiPhoneView.setTextSize(res.getDimension(R.dimen.TaxiNumberSize));
		taxiPhoneView.setText(phone);
		taxiPhoneView.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		TextView callBlurb = new TextView(context);
		LayoutParams callBlurbParams = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		callBlurbParams.setMargins(margin, margin, margin, margin);
		callBlurb.setLayoutParams(callBlurbParams);
		callBlurb.setTextColor(res.getColor(R.color.ViewTextColor));
		callBlurb.setTextSize(res.getDimension(R.dimen.TaxiNameSize));
		callBlurb.setText(res.getString(R.string.CallBlurb));
		
		taxiInfoLayout.addView(taxiNameView);
		taxiInfoLayout.addView(taxiPhoneView);
		taxiInfoLayout.addView(callBlurb);
		
		this.addView(taxiInfoLayout);
		
		TextView taxiCost = new TextView(context);
		LayoutParams taxiCostParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		taxiPhoneParams.setMargins(margin, margin, margin, margin);
		taxiCost.setLayoutParams(taxiCostParams);
		taxiCost.setTextColor(res.getColor(R.color.HeaderColor));
		taxiCost.setTextSize(res.getDimension(R.dimen.TaxiCostSize));
		Double d = (double) Math.round(cost * 100);
		taxiCost.setText("$" + String.format("%.2f", d/100));
		taxiCost.setTypeface(null, Typeface.BOLD);
		
		this.addView(taxiCost);
		
		
		this.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				doCall();
			}
		});
	}
	
	public void doCall(){
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
		context.startActivity(i);
	}

	
	
}
