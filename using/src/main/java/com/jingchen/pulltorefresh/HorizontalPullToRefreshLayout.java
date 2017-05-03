package com.jingchen.pulltorefresh;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jingchen.pulltorefresh.pullableview.Pullable;
import org.noear.using.R;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 * 
 * @author 陈靖
 */
public class HorizontalPullToRefreshLayout extends RelativeLayout implements IPullToRefreshView
{
	private int _TextColor = 0;

	public static final String TAG = "HPullToRefreshLayout";

	// 当前状态
	private int state = PullToRefreshState.INIT;
	// 刷新回调接口
	private OnRefreshListener mListener;

	// 按下X坐标，上一个事件点X坐标
	private float downX, lastX;

	// 下拉的距离。注意：pullDownX和pullUpX不可能同时不为0
	public float pullDownX = 0;
	// 上拉的距离
	private float pullUpX = 0;

	// 释放刷新的距离
	public float refreshDist = 100;
	// 释放加载的距离
	public float loadmoreDist = 100;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 4;
	// 第一次执行布局
	private boolean isLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;

	// 下拉箭头的转180°动画
	private RotateAnimation rotateAnimation;
	// 均匀旋转动画
	private RotateAnimation refreshingAnimation;

	// 下拉头
	private View refreshView;
	// 刷新结果：成功或失败
	private TextView refreshStateTextView;

	// 上拉头
	private View loadmoreView;
	// 加载结果：成功或失败
	private TextView loadStateTextView;

	// 实现了Pullable接口的View
	private View pullableView;
	// 过滤多点触碰
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	private Context mContext;

	private IStrings mStr = DefaultStrings.instance;

	public void setStrings(IStrings strings){
		mStr = strings;
	}

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			// 回弹速度随下拉距离moveDeltaX增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredWidth() * (pullDownX + Math.abs(pullUpX))));
			if (!isTouch)
			{
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == PullToRefreshState.REFRESHING && pullDownX <= refreshDist)
				{
					pullDownX = refreshDist;
					timer.cancel();
				} else if (state == PullToRefreshState.LOADING && -pullUpX <= loadmoreDist)
				{
					pullUpX = -loadmoreDist;
					timer.cancel();
				}

			}
			if (pullDownX > 0)
				pullDownX -= MOVE_SPEED;
			else if (pullUpX < 0)
				pullUpX += MOVE_SPEED;
			if (pullDownX < 0)
			{
				// 已完成回弹
				pullDownX = 0;
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != PullToRefreshState.REFRESHING && state != PullToRefreshState.LOADING)
					changeState(PullToRefreshState.INIT);
				timer.cancel();
				requestLayout();
			}
			if (pullUpX > 0)
			{
				// 已完成回弹
				pullUpX = 0;
				// 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != PullToRefreshState.REFRESHING && state != PullToRefreshState.LOADING)
					changeState(PullToRefreshState.INIT);
				timer.cancel();
				requestLayout();
			}
			Log.d("handle", "handle");
			// 刷新布局,会自动调用onLayout
			requestLayout();
			// 没有拖拉或者回弹完成
			if (pullDownX + Math.abs(pullUpX) == 0)
				timer.cancel();
		}

	};

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		mListener = listener;
	}

	public HorizontalPullToRefreshLayout(Context context)
	{
		super(context);
		initView(context);
	}

	public HorizontalPullToRefreshLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}

	public HorizontalPullToRefreshLayout(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context)
	{
		mContext = context;
		timer = new MyTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.rotating);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	private void hide()
	{
		timer.schedule(5);
	}

	/**
	 * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
	 */
	/**
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void refreshFinish(int refreshResult)
	{
		switch (refreshResult)
		{
		case PullToRefreshState.SUCCEED:
			// 刷新成功
			refreshStateTextView.setText(mStr.refresh_succeed());
			break;
		case PullToRefreshState.FAIL:
		default:
			// 刷新失败
			refreshStateTextView.setText(mStr.refresh_fail());
			break;
		}
		if (pullDownX > 0)
		{
			// 刷新结果停留1秒
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(PullToRefreshState.DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 1000);
		} else
		{
			changeState(PullToRefreshState.DONE);
			hide();
		}
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 * 
	 * @param refreshResult
	 *            PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void loadmoreFinish(int refreshResult)
	{
		switch (refreshResult)
		{
		case PullToRefreshState.SUCCEED:
			// 加载成功
			loadStateTextView.setText(mStr.load_succeed());
			break;
		case PullToRefreshState.FAIL:
		default:
			// 加载失败
			loadStateTextView.setText(mStr.load_fail());
			break;
		}
		if (pullUpX < 0)
		{
			// 刷新结果停留1秒
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(PullToRefreshState.DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 1000);
		} else
		{
			changeState(PullToRefreshState.DONE);
			hide();
		}
	}

	private void changeState(int to)
	{
		state = to;
		switch (state)
		{
		case PullToRefreshState.INIT:
			// 下拉布局初始状态
			refreshStateTextView.setText(mStr.pull_to_refresh());
			// 上拉布局初始状态
			loadStateTextView.setText(mStr.pullup_to_load());
			break;
		case PullToRefreshState.RELEASE_TO_REFRESH:
			// 释放刷新状态
			refreshStateTextView.setText(mStr.release_to_refresh());
			break;
		case PullToRefreshState.REFRESHING:
			// 正在刷新状态
			refreshStateTextView.setText(mStr.refreshing());
			break;
		case PullToRefreshState.RELEASE_TO_LOAD:
			// 释放加载状态
			loadStateTextView.setText(mStr.release_to_load());
			break;
		case PullToRefreshState.LOADING:
			// 正在加载状态
			loadStateTextView.setText(mStr.loading());
			break;
		case PullToRefreshState.DONE:
			// 刷新或加载完毕，啥都不做
			break;
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull()
	{
		canPullDown = true;
		canPullUp = true;
	}

	/*
	 * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			downX = ev.getX();
			lastX = downX;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0)
			{
				if (pullDownX > 0
						|| (((Pullable) pullableView).canPullDown()
								&& canPullDown && state != PullToRefreshState.LOADING))
				{
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownX = pullDownX + (ev.getX() - lastX) / radio;
					if (pullDownX < 0)
					{
						pullDownX = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownX > getMeasuredWidth())
						pullDownX = getMeasuredWidth();
					if (state == PullToRefreshState.REFRESHING)
					{
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (pullUpX < 0
						|| (((Pullable) pullableView).canPullUp() && canPullUp && state != PullToRefreshState.REFRESHING))
				{
					// 可以上拉，正在刷新时不能上拉
					pullUpX = pullUpX + (ev.getX() - lastX) / radio;
					if (pullUpX > 0)
					{
						pullUpX = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpX < -getMeasuredWidth())
						pullUpX = -getMeasuredWidth();
					if (state == PullToRefreshState.LOADING)
					{
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastX = ev.getX();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredWidth()
					* (pullDownX + Math.abs(pullUpX))));
			if (pullDownX > 0 || pullUpX < 0)
				requestLayout();
			if (pullDownX > 0)
			{
				if (pullDownX <= refreshDist
						&& (state == PullToRefreshState.RELEASE_TO_REFRESH || state == PullToRefreshState.DONE))
				{
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(PullToRefreshState.INIT);
				}
				if (pullDownX >= refreshDist && state == PullToRefreshState.INIT)
				{
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(PullToRefreshState.RELEASE_TO_REFRESH);
				}
			} else if (pullUpX < 0)
			{
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpX <= loadmoreDist
						&& (state == PullToRefreshState.RELEASE_TO_LOAD || state == PullToRefreshState.DONE))
				{
					changeState(PullToRefreshState.INIT);
				}
				// 上拉操作
				if (-pullUpX >= loadmoreDist && state == PullToRefreshState.INIT)
				{
					changeState(PullToRefreshState.RELEASE_TO_LOAD);
				}

			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownX + Math.abs(pullUpX)) > 8)
			{
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownX > refreshDist || -pullUpX > loadmoreDist)
			// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
			{
				isTouch = false;
			}
			if (state == PullToRefreshState.RELEASE_TO_REFRESH)
			{
				changeState(PullToRefreshState.REFRESHING);
				// 刷新操作
				if (mListener != null)
					mListener.onRefresh(this);
			} else if (state == PullToRefreshState.RELEASE_TO_LOAD)
			{
				changeState(PullToRefreshState.LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	/**
	 * @author chenjing 自动模拟手指滑动的task
	 * 
	 */
	private class AutoRefreshAndLoadTask extends
			AsyncTask<Integer, Float, String>
	{

		@Override
		protected String doInBackground(Integer... params)
		{
			while (pullDownX < 4 / 3 * refreshDist)
			{
				pullDownX += MOVE_SPEED;
				publishProgress(pullDownX);
				try
				{
					Thread.sleep(params[0]);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{
			changeState(PullToRefreshState.REFRESHING);
			// 刷新操作
			if (mListener != null)
				mListener.onRefresh(HorizontalPullToRefreshLayout.this);
			hide();
		}

		@Override
		protected void onProgressUpdate(Float... values)
		{
			if (pullDownX > refreshDist)
				changeState(PullToRefreshState.RELEASE_TO_REFRESH);
			requestLayout();
		}

	}

	/**
	 * 自动刷新
	 */
	public void autoRefresh()
	{
		AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
		task.execute(20);
	}

	/**
	 * 自动加载
	 */
	public void autoLoad()
	{
		pullUpX = -loadmoreDist;
		requestLayout();
		changeState(PullToRefreshState.LOADING);
		// 加载操作
		if (mListener != null)
			mListener.onLoadMore(this);
	}

	private void initView()
	{
		// 初始化下拉布局
		refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
		// 初始化上拉布局
		loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);

		refreshStateTextView.setText(mStr.pull_to_refresh());
		loadStateTextView.setText(mStr.pullup_to_load());

		setTextColor(_TextColor);
	}

	public void setTextColor(int textColor){
		if(textColor!=0){
			_TextColor = textColor;
			if(refreshStateTextView!=null) {
				refreshStateTextView.setTextColor(textColor);
				loadStateTextView.setTextColor(textColor);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		Log.d("Test", "Test");
		if (!isLayout)
		{
			// 这里是第一次进来的时候做一些初始化
			refreshView = getChildAt(0);
			pullableView = getChildAt(1);
			loadmoreView = getChildAt(2);
			isLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshView).getChildAt(0)
					.getMeasuredWidth();
			loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0)
					.getMeasuredWidth();
		}
		// 改变子控件的布局，这里直接用(pullDownX + pullUpX)作为偏移量，这样就可以不对当前状态作区分
		refreshView.layout((int) (pullDownX + pullUpX) - refreshView.getMeasuredWidth(),
				0,
				(int) (pullDownX + pullUpX),
				refreshView.getMeasuredHeight());

		pullableView.layout((int) (pullDownX + pullUpX),0,
				 (int) (pullDownX + pullUpX)+ pullableView.getMeasuredWidth(),
				pullableView.getMeasuredHeight());

		loadmoreView.layout((int) (pullDownX + pullUpX) + pullableView.getMeasuredWidth(),
				0,
				(int) (pullDownX + pullUpX) + pullableView.getMeasuredWidth()
						+ loadmoreView.getMeasuredWidth(),
				loadmoreView.getMeasuredHeight());
	}


}
