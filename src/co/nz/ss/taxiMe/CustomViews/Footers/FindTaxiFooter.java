package co.nz.ss.taxiMe.CustomViews.Footers;

import co.nz.ss.taxiMe.Activities.FindTaxiActivity;
import android.content.Context;
import android.util.AttributeSet;

public class FindTaxiFooter extends SubmitBackFooter {

	private FindTaxiActivity parent;
	
	public FindTaxiFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(context instanceof FindTaxiActivity){
			parent = (FindTaxiActivity)context;
		}
	}

	@Override
	protected void leftButtonClick() {
		parent.doBack();
	}

	@Override
	protected void rightButtonClick() {
		parent.doGo();
	}

}
