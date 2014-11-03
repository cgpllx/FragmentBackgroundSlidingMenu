package com.liangxiao.slidingmenu.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.liangxiao.weixin2.R;
import com.nineoldandroids.view.ViewHelper;

public class SlidingMenu extends HorizontalScrollView {
	LinearLayout mWapper;
	ViewGroup mMenu;
	ViewGroup mContent;
	int mScreenWidth;

	int mMenuWidth;

	// dp
	int mMenuRightPadding = 100;

	Boolean once = false;

	Boolean isOpen = false;

	/**
	 * δʹ�ö��������ʱ������
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	/**
	 * ��ʹ�����Զ���������ʱ����ô˷���
	 * 
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// ��ȡ���Ƕ����hyman����
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.SlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 100, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}

		a.recycle();

		// ��ȡ��ǰ��Ļ�Ŀ��
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		mScreenWidth = outMetrics.widthPixels;

		// ��dpת��Ϊpx
		// mMenuRightPadding = (int) TypedValue.applyDimension(
		// TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources()
		// .getDisplayMetrics());
	}

	public SlidingMenu(Context context) {
		this(context, null);

	}

	/**
	 * ������view�Ŀ�ߣ��Լ��Ŀ��
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);
			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPadding;
			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * ͨ������ƫ����,��menu����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		super.onLayout(changed, l, t, r, b);

		if (changed) {
			this.scrollTo(mMenuWidth, 0);// ��menu���صĲ�������
		}

	}

	/**
	 * �жϻ����ľ��룬��menu������ʾ��������
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();// ��������߲��ֵ�width
			if (scrollX >= mMenuWidth / 2) {
				// ���ز���
				this.smoothScrollTo(mMenuWidth, 0);
				isOpen = false;
			} else {
				this.smoothScrollTo(0, 0);
				isOpen = true;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void openMenu() {
		if (isOpen)
			return;
		this.smoothScrollTo(0, 0);
		isOpen = true;
	}

	public void closeMenu() {
		if (!isOpen)
			return;
		this.smoothScrollTo(mMenuWidth, 0);
		isOpen = false;
	}

	public void toggle() {
		if (isOpen) {
			closeMenu();
		} else {
			openMenu();
		}
	}

	/**
	 * ��������ʱ
	 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);
		float scale = l * 1.0f / mMenuWidth;// 1~0

		// �������Զ���������TranslationX
		ViewHelper.setTranslationX(mMenu, mMenuWidth * scale);
	}
}
