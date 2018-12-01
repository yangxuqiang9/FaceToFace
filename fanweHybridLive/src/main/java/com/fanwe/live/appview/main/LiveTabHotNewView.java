package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.hybrid.activity.AppWebViewActivity;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.pullcondition.IgnorePullCondition;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.activity.AdImgWebViewActivity;
import com.fanwe.live.activity.LiveRankingActivity;
import com.fanwe.live.adapter.LiveTabHotNewAdapter;
import com.fanwe.live.appview.LiveTabHotHeaderView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.event.ESelectLiveFinish;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.LiveBannerModel;
import com.fanwe.live.model.LiveFilterModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.utils.GlideUtil;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 首页热门直播列表
 */
public class LiveTabHotNewView extends LiveTabBaseView implements BGABanner.Adapter<ImageView, LiveBannerModel>{
    public LiveTabHotNewView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabHotNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveTabHotNewView(Context context) {
        super(context);
        init();
    }


    //    private LinearLayout lilv_header;



    private SwipeMenuRecyclerView rv_content;
//    private LiveTabHotHeaderView mHeaderView;
    private BGABanner convenientBanner;//顶部广告栏控件
    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private List<LiveBannerModel> bannermodel = new ArrayList<>();
    private LiveTabHotNewAdapter mAdapter;
    View headerView;
    private int mSex;
    private String mCity;
    private void init() {

        setContentView(R.layout.view_live_tab_hot_new);
//        lilv_header = (LinearLayout) findViewById(R.id.lilv_header);

        rv_content = (SwipeMenuRecyclerView) findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new GridLayoutManager(getActivity(),2));
        rv_content.setAutoLoadMore(false);
        mAdapter = new LiveTabHotNewAdapter(mListModel, getActivity());
        addHeaderView();
        rv_content.addHeaderView(headerView);
        rv_content.setAdapter(mAdapter);
        getStateLayout().setAdapter(mAdapter);
        updateParams();

        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                startLoopRunnable();
            }

            @Override
            public void onRefreshingFromFooter() {
            }
        });
        startLoopRunnable();
    }

    /**
     * 添加HeaderView
     */
    private void addHeaderView() {

        convenientBanner = new BGABanner(getActivity(),null);
        headerView = View.inflate(getActivity(), R.layout.layout_header, null);
        convenientBanner = (BGABanner) headerView.findViewById(R.id.banner);
        convenientBanner.setAdapter(this);
        convenientBanner.setDelegate(new BGABanner.Delegate() {
            @Override
            public void onBannerItemClick(BGABanner banner, View itemView, @Nullable Object model, int position) {
                LiveBannerModel bannerModel11 = bannermodel.get(position);
                if(bannerModel11.getType()==0){
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(bannerModel11.getUrl()));//Url 就是你要打开的网址
                    intent.setAction(Intent.ACTION_VIEW);
                    getActivity().startActivity(intent);
                }else if(bannerModel11.getType()==2){
                    Intent intent = new Intent(getActivity(), LiveRankingActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });

        /*mHeaderView = new LiveTabHotHeaderView(getActivity());
        mHeaderView.setBannerItemClickCallback(new SDItemClickCallback<LiveBannerModel>() {
            @Override
            public void onItemClick(int position, LiveBannerModel item, View view) {
                Intent intent = item.parseType(getActivity());
                if (intent != null) {
                    getActivity().startActivity(intent);
                }
            }
        });
        SDViewPager viewPager = getParentViewPager();
        if (viewPager != null) {
            viewPager.addPullCondition(new IgnorePullCondition(mHeaderView.getSlidingView()));
        }*/
    }

    /**
     * 更新接口过滤条件
     */
    private void updateParams() {
        LiveFilterModel model = LiveFilterModel.get();

        mSex = model.getSex();
        mCity = model.getCity();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        startLoopRunnable();
    }

    /**
     * 选择过滤条件完成
     *
     * @param event
     */
    public void onEventMainThread(ESelectLiveFinish event) {
        updateParams();
        startLoopRunnable();
    }

    @Override
    protected void onLoopRun() {
        requestData();
    }

    /**
     * 请求热门首页接口
     */
    private void requestData() {
        CommonInterface.requestIndex(1, mSex, 0, mCity, new AppRequestCallback<Index_indexActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    bannermodel = actModel.getBanner();
                    convenientBanner.setData(bannermodel, Arrays.asList("", "", ""));
                    mListModel = actModel.getList();
                    mAdapter.updateData(mListModel);
                    rv_content.loadMoreFinish(false,false);
                }
            }

            @Override
            protected void onError(SDResponse resp) {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp) {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }

    @Override
    public void scrollToTop() {
        rv_content.scrollToPosition(0);
    }

    @Override
    protected void onRoomClosed(final int roomId) {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>() {
            @Override
            public LiveRoomModel onBackground() {
                synchronized (LiveTabHotNewView.this) {
                    if (SDCollectionUtil.isEmpty(mListModel)) {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext()) {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id()) {
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result) {
                if (result != null) {
                    synchronized (LiveTabHotNewView.this) {
                        mAdapter.removeData(result);
                    }
                }
            }
        });
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable LiveBannerModel model, int position) {
        GlideUtil.load(model.getImage()).placeholder(0).into(itemView);
    }


}
