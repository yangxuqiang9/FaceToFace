package com.fanwe.live.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.fanwe.live.control.LivePlayerSDK;
import com.fanwe.live.control.LivePushSDK;
import com.fanwe.live.control.TPlayCallbackWrapper;
import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * 视频播放view
 */
public class LiveVideoView extends TXCloudVideoView
{
    private LivePlayerSDK playerSDK;
    private LivePushSDK pushSDK;
    private OnTouchEventListener listener;

    public LiveVideoView(Context context)
    {
        super(context);
        init();
    }

    public LiveVideoView(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        init();
    }

    private void init()
    {

    }

    /**
     * 设置播放监听
     *
     * @param playCallback
     */
    public void setPlayCallback(LivePlayerSDK.TPlayCallback playCallback)
    {
        mPlayCallbackWrapper.setPlayCallback(playCallback);
    }

    /**
     * 获得播放对象
     *
     * @return
     */
    public LivePlayerSDK getPlayer()
    {
        if (playerSDK == null)
        {
            playerSDK = new LivePlayerSDK();
            playerSDK.init(this);
            playerSDK.setPlayCallback(mPlayCallbackWrapper);
        }
        return playerSDK;
    }

    /**
     * 获得推流对象
     *
     * @return
     */
    public LivePushSDK getPusher()
    {
        if (pushSDK == null)
        {
            pushSDK = LivePushSDK.getInstance();
        }
        return pushSDK;
    }

    private TPlayCallbackWrapper mPlayCallbackWrapper = new TPlayCallbackWrapper()
    {

    };

    public void destroy()
    {
        if (playerSDK != null)
        {
            playerSDK.onDestroy();
            playerSDK = null;
        }
        if (pushSDK != null)
        {
            pushSDK.onDestroy();
            pushSDK = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x=0;
        int y=0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                x= (int) event.getX();
                y = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int moveY = (int) event.getY();
                int deffX=moveX-x;
                int deffY=moveY-y;

                int absX = Math.abs(deffX);
                int absY = Math.abs(deffY);
                //上下滑动
                if(absY>20&&(absY-absX)>20){
                    //向下
                    if(deffY>0){
                        if(listener!=null){
                            listener.moveUP(getX()+deffX,getY()-deffY);
                        }
                    //向上
                    }else {
                        if(listener!=null){
                            listener.moveDown(getX()+deffX,getY()-deffY);
                        }
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                x=0;
                y=0;
                break;
        }

        return super.onTouchEvent(event);
    }

    public void setListener(OnTouchEventListener listener){
        this.listener=listener;
    }

    public interface OnTouchEventListener{
        void moveUP(float x,float y);
        void moveDown(float x,float y);
    }
}
