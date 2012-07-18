package co.nz.ss.taxiMe.CustomViews.Footers;

import co.nz.ss.taxiMe.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public abstract class ButtonRowWithFooter extends LinearLayout{

	protected Button leftButton;
	protected Button rightButton;
	protected Resources res;
	protected Activity parent;
	
	public ButtonRowWithFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		res = getResources();
		parent = (Activity)context;
		
		setOrientation(LinearLayout.VERTICAL);
		LayoutParams rootLp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		setLayoutParams(rootLp);
		
		LinearLayout buttonRow = new LinearLayout(context);
		buttonRow.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams buttonRowLp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		buttonRow.setLayoutParams(buttonRowLp);
		
		LayoutParams buttonLp = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 50);
		int buttonMargins = (int)res.getDimension(R.dimen.PreferencesButtonMargin);
		buttonLp.setMargins(buttonMargins, buttonMargins, buttonMargins, buttonMargins);
		
		leftButton = new Button(context);
		
		leftButton.setLayoutParams(buttonLp);
		
		leftButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				leftButtonClick();
			}
		});
		
		rightButton = new Button(context);
		
		rightButton.setLayoutParams(buttonLp);
		
		rightButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				rightButtonClick();
			}
		});
		
		buttonRow.addView(leftButton);
		buttonRow.addView(rightButton);
		
		addView(buttonRow);
		
		addView(new SignitureFooter(context));
		
		modifyLeftButton();
		modifyRightButton();
	}
	
	protected abstract void modifyLeftButton();
	protected abstract void modifyRightButton();
	protected abstract void leftButtonClick();
	protected abstract void rightButtonClick();
}
