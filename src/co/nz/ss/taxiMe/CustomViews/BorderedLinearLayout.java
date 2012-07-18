package co.nz.ss.taxiMe.CustomViews;

import co.nz.ss.taxiMe.R;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class BorderedLinearLayout extends LinearLayout {

	public BorderedLinearLayout(Context context){
		super(context);
		initUI();
	}
	
	public BorderedLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initUI();
	}
	
	private void initUI(){
		//Set base settings
		Resources res =  getResources();
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundDrawable(res.getDrawable(R.drawable.layout_border));
		int padding = (int) res.getDimension(R.dimen.BoarderWidth);
		this.setPadding(padding, padding, padding, padding);
	}

}
