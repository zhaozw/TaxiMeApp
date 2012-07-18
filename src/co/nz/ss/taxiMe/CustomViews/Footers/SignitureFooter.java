package co.nz.ss.taxiMe.CustomViews.Footers;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;
import co.nz.ss.taxiMe.R;

public class SignitureFooter extends TextView{
	
	public SignitureFooter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public SignitureFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public SignitureFooter(Context context) {
		super(context);
		initView();
	}
	
	public void initView(){
		Resources res = getResources();
		super.setText(res.getString(R.string.ViewFooter));
		super.setTextColor(res.getColor(R.color.ViewTextColor));
		super.setGravity(android.view.Gravity.RIGHT);
		super.setTextSize(res.getDimension(R.dimen.FooterSize));
	}

}
