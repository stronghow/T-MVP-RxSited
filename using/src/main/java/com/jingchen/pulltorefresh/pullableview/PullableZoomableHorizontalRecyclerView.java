package com.jingchen.pulltorefresh.pullableview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.jingchen.pulltorefresh.ZoomableRecyclerView;

/**
 * Created by yuety on 15/9/1.
 */
public class PullableZoomableHorizontalRecyclerView extends ZoomableRecyclerView implements Pullable
{
    public PullableZoomableHorizontalRecyclerView(Context context)
    {
        super(context);
    }

    public PullableZoomableHorizontalRecyclerView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableZoomableHorizontalRecyclerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown()
    {
        LinearLayoutManager lm = (LinearLayoutManager)getLayoutManager();
        if (lm.getItemCount() == 0)
        {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (lm.findFirstVisibleItemPosition() == 0
                && getChildAt(0).getLeft() >= 0)
        {
            // 滑到ListView的顶部了
            return true;
        } else
            return false;
    }
    @Override
    public boolean canPullUp()

    {
        LinearLayoutManager lm = (LinearLayoutManager)getLayoutManager();

        if (lm.getItemCount() == 0)
        {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (lm.findLastVisibleItemPosition() == (lm.getItemCount() - 1))
        {
            View last = getChildAt(lm.findLastVisibleItemPosition() - lm.findFirstVisibleItemPosition());
            // 滑到底部了
            if (last!= null&& last.getRight() <= getMeasuredWidth())
                return true;
        }



        return false;
    }
}
