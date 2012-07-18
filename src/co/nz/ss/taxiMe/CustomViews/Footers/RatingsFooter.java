package co.nz.ss.taxiMe.CustomViews.Footers;

import co.nz.ss.taxiMe.Activities.RatingActivity;
import android.content.Context;
import android.util.AttributeSet;

public class RatingsFooter extends SubmitBackFooter {

	private RatingActivity parent;
	
	public RatingsFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(context instanceof RatingActivity){
			parent = (RatingActivity)context;
		}
	}

	@Override
	protected void leftButtonClick() {
		parent.doBack();
	}

	@Override
	protected void rightButtonClick() {
		parent.submit();
	}

}
