package com.zhoukp.photo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhoukp.photo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间：2018/1/8 13:52
 * 邮箱：zhoukaiping@szy.cn
 * 作用：下拉刷新的ListView
 *
 * @author zhoukp
 */

public class FreshListView extends ListView {

    /**
     * 下拉刷新
     */
    protected LinearLayout headerView;
    /**
     * 下拉刷新控件
     */
    protected View llPullDownRefresh;
    /**
     * 加载更多控件
     */
    private View footerView;
    /**
     * 加载更多控件的高
     */
    private int footerViewHeight;

    private ImageView ivArrow;
    private ProgressBar pbStatus;
    private TextView tvStatus;
    private TextView tvTime;

    /**
     * 下拉刷新控件的高
     */
    private int pullDownRefreshHeight;
    /**
     * 下拉刷新
     */
    public static final int PULL_DOWN_REFRESH = 0;
    /**
     * 手松刷新
     */
    public static final int RELEASE_REFRESH = 1;
    /**
     * 正在刷新
     */
    public static final int REFRESHING = 2;
    /**
     * 当前状态
     */
    private int currentStatus = PULL_DOWN_REFRESH;

    /**
     * 动画
     */
    private Animation upAnimation;
    private Animation downAnimation;

    /**
     * 是否已经加载更多
     */
    private boolean isLoadMore = false;

    private float startY = -1;

    public FreshListView(Context context) {
        this(context, null);
    }

    public FreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView(context);
        initAnimation();
        initFooterView(context);
    }

    /**
     * 初始化HeaderView
     *
     * @param context 上下文
     */
    private void initHeaderView(Context context) {
        headerView = (LinearLayout) View.inflate(context, R.layout.fresh_header, null);
        llPullDownRefresh = headerView.findViewById(R.id.ll_pull_down_refresh);
        ivArrow = (ImageView) headerView.findViewById(R.id.iv_arrow);
        pbStatus = (ProgressBar) headerView.findViewById(R.id.pb_status);
        tvStatus = (TextView) headerView.findViewById(R.id.tv_status);
        tvTime = (TextView) headerView.findViewById(R.id.tv_time);

        //测量
        llPullDownRefresh.measure(0, 0);
        pullDownRefreshHeight = llPullDownRefresh.getMeasuredHeight();

        //默认隐藏下拉刷新控件
        llPullDownRefresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
        //添加listView的头部
        addHeaderView(headerView);
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        upAnimation.setDuration(500);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, -360,
                Animation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        downAnimation.setDuration(500);
        downAnimation.setFillAfter(true);
    }

    /**
     * 初始化FooterView
     *
     * @param context 上下文
     */
    private void initFooterView(Context context) {
        footerView = View.inflate(context, R.layout.fresh_footer, null);
        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();

        //默认隐藏加载更多控件
        footerView.setPadding(0, -footerViewHeight, 0, 0);

        //listView添加footerView
        addFooterView(footerView);

        //监听listView滚动
        setOnScrollListener(new KpOnScrollListener());
    }

    /**
     * 滚动监听
     */
    private class KpOnScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当静止或者惯性滚动的时候并且是最后一条可见
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    || scrollState == OnScrollListener.SCROLL_STATE_FLING) {

                if (getLastVisiblePosition() >= getCount() - 1) {
                    //最后一条可见
                    //显示加载更多控件
                    footerView.setPadding(8, 8, 8, 8);
                    //状态改变
                    isLoadMore = true;
                    //回调接口
                    if (onRefreshListener != null) {
                        onRefreshListener.onLoadMore();
                    }
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        }
    }

    /**
     * 触摸事件的处理
     *
     * @param event 事件
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下事件的处理
                //记录起始坐标
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动事件的处理
                if (startY == -1) {
                    startY = event.getY();
                }
                //如果是正在刷新，就不让再次刷新
                if (currentStatus == REFRESHING) {
                    break;
                }
                //新的坐标
                float endY = event.getY();
                //记录滑动的距离
                float disY = endY - startY;
                if (disY > 0) {
                    //下拉
                    int paddingTop = (int) (-pullDownRefreshHeight + disY);

                    if (paddingTop < 0 && currentStatus != PULL_DOWN_REFRESH) {
                        //下拉刷新状态
                        currentStatus = PULL_DOWN_REFRESH;
                        //更新状态
                        refreshViewState();

                    } else if (paddingTop > 0 && currentStatus != RELEASE_REFRESH) {
                        //手松刷新状态
                        currentStatus = RELEASE_REFRESH;
                        //更新状态
                        refreshViewState();
                    }
                    llPullDownRefresh.setPadding(0, paddingTop, 0, 0);
                } else if (disY < 0) {
                    //上拉
                    int paddingTop = (int) (footerViewHeight - disY);
                    footerView.setPadding(8, paddingTop, 8, 8);
                }
                break;
            case MotionEvent.ACTION_UP:
                //手指离开事件的处理
                //重新初始化
                startY = -1;
                if (currentStatus == PULL_DOWN_REFRESH) {
                    llPullDownRefresh.setPadding(0, -pullDownRefreshHeight, 0, 0);
                } else if (currentStatus == RELEASE_REFRESH) {
                    //设置状态为正在刷新
                    currentStatus = REFRESHING;
                    refreshViewState();
                    llPullDownRefresh.setPadding(0, 0, 0, 0);
                    //回调接口
                    if (onRefreshListener != null) {
                        onRefreshListener.onPullDownRefresh();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 更新状态
     */
    private void refreshViewState() {
        switch (currentStatus) {
            case PULL_DOWN_REFRESH:
                //下拉状态
                ivArrow.startAnimation(downAnimation);
                tvStatus.setText("下拉刷新...");
                break;
            case RELEASE_REFRESH:
                //手指释放状态
                ivArrow.startAnimation(upAnimation);
                tvStatus.setText("手松刷新...");
                break;
            case REFRESHING:
                //正在刷新状态
                tvStatus.setText("正在刷新...");
                tvTime.setText("上次更新时间:" + getSystemTime());
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.GONE);
                pbStatus.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 当刷新完成的时候回调该方法
     * 用于刷新状态的还原
     *
     * @param sucess boolean
     */
    public void onRefreshFinish(boolean sucess) {
        if (isLoadMore) {
            //加载更多
            isLoadMore = false;
            //隐藏加载更多布局
            footerView.setPadding(0, -footerViewHeight, 0, 0);
        } else {
            //下拉刷新
            tvStatus.setText("下拉刷新");
            currentStatus = PULL_DOWN_REFRESH;
            ivArrow.clearAnimation();
            pbStatus.setVisibility(View.GONE);
            ivArrow.setVisibility(View.VISIBLE);
            //隐藏下拉刷新控件
            llPullDownRefresh.setPadding(0, -pullDownRefreshHeight, 0, 0);

            if (sucess) {
                tvTime.setText("上次更新时间 " + getSystemTime());
            }
        }
    }

    /**
     * 得到系统的当前时间
     *
     * @return 当前时间
     */
    public String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * 监听控件的刷新
     */
    public interface OnRefreshListener {

        /**
         * 当下拉刷新的时候回调这个方法
         */
        void onPullDownRefresh();

        /**
         * 当加载更多的时候回调这个方法
         */
        void onLoadMore();

    }

    private OnRefreshListener onRefreshListener;

    /**
     * 设置监听刷新,由外界设置
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        this.onRefreshListener = listener;
    }
}
