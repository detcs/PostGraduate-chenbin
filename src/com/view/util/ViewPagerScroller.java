package com.view.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ViewPagerScroller extends Scroller {
    private int mScrollDuration = 2000;             // 滑动速度
	private int mDuration=2000;
  
    /**
     * 设置速度速度
     * @param duration
     */
    public void setScrollDuration(int duration){
        this.mScrollDuration = duration;
    }
      
    public ViewPagerScroller(Context context) {
        super(context);
    }
  
    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }
  
    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }
  
    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
  
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }
  
    public void setmDuration(int time) {  
        mDuration = time;  
        }  
      
        /** 
         * get current animation time 
         *  
         * @return 
         */  
        public int getmDuration() {  
        return mDuration;  
        }  
      
    public void initViewPagerScroll(ViewPager viewPager) {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
