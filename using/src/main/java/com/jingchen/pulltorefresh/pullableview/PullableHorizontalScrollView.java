package com.jingchen.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class PullableHorizontalScrollView extends HorizontalScrollView implements Pullable
{

	public PullableHorizontalScrollView(Context context)
	{
		super(context);
	}

	public PullableHorizontalScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollX() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getScrollX() >= (getChildAt(0).getWidth() - getMeasuredWidth()))
			return true;
		else
			return false;
	}

}
