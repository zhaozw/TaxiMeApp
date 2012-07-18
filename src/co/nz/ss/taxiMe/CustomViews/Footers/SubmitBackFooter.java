package co.nz.ss.taxiMe.CustomViews.Footers;

import android.content.Context;
import android.util.AttributeSet;
import co.nz.ss.taxiMe.R;

public abstract class SubmitBackFooter extends ButtonRowWithFooter {

	public SubmitBackFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void modifyLeftButton() {
		leftButton.setText(res.getText(R.string.BackString));
	}

	@Override
	protected void modifyRightButton() {
		rightButton.setText(res.getText(R.string.SubmitString));
	}
}

