package com.jingchen.pulltorefresh.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.jingchen.pulltorefresh.ZoomableListView;

public class PullableZoomableListView extends ZoomableListView implements Pullable
{

	public PullableZoomableListView(Context context)
	{
		super(context);
	}

	public PullableZoomableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableZoomableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public boolean isCanPullDown=true;

	@Override
	public boolean canPullDown()
	{
		if(isCanPullDown == false)
			return false;

		if (getCount() == 0)
		{
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// 滑到ListView的顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getCount() == 0)
		{
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}
}
