package co.nz.ss.taxiMe.CustomViews.Footers;

import co.nz.ss.taxiMe.Activities.Preferences;
import android.content.Context;
import android.util.AttributeSet;

public class PreferencesFooter extends SubmitBackFooter  {

	public PreferencesFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void leftButtonClick() {
		if(parent instanceof Preferences){
			Preferences parentAct = (Preferences)parent;
			parentAct.doBack();
		}

	}

	@Override
	protected void rightButtonClick() {
		if(parent instanceof Preferences){
			Preferences parentAct = (Preferences)parent;
			parentAct.submit();
		}
	}

}
