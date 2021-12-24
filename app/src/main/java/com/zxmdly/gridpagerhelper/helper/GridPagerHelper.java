package com.zxmdly.gridpagerhelper.helper;

import android.util.Log;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhouxuming
 * @date 2021/12/23 9:59 上午 一款支持加载更多、滑动惯性定位、页码计算的列表辅助器 目前仅支持 Grid 方式，理论上 Linear 也可以支持，不支持瀑布流 Layout
 * //
 */
public class GridPagerHelper extends OnScrollListener {

  public static final String TAG = GridPagerHelper.class.getSimpleName();

  private RecyclerView recyclerView;
  private int firstVisiblePosition;
  private int lastVisiblePosition;
  private int firstCompletelyVisiblePosition;
  private int lastCompletelyVisiblePosition;
  private boolean loadCompleted = true;
  private boolean isPreLoad = true; //是否预加载
  private int preloadFactor = 1;//预加载系数
  private int uiPageSize;//每页数据多少条
  private int dx;
  private int dy;

  private OnGridPagerHelperListener listener;

  public GridPagerHelper(RecyclerView recyclerView, Config config) {
    super();
    if (this.recyclerView == null) {
      this.recyclerView = recyclerView;
    }
    if (config != null) {
      this.isPreLoad = config.isPreLoad;
      this.uiPageSize = config.uiPageSize;
      this.preloadFactor = config.preloadFactor;
      if (config.snapEnum == SnapEnum.PAGER) {
        new PagerSnapHelper().attachToRecyclerView(this.recyclerView);
      } else if (config.snapEnum == SnapEnum.LINEAR) {
        new LinearSnapHelper().attachToRecyclerView(this.recyclerView);
      }
    }
  }

  public GridPagerHelper buildListener(OnGridPagerHelperListener listener) {
    if (this.listener == null) {
      this.listener = listener;
    }
    if (recyclerView != null) {
      recyclerView.addOnScrollListener(this);
    }
    return this;
  }

  @Override
  public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
    super.onScrollStateChanged(recyclerView, newState);
    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
      if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        findPosition(gridLayoutManager);
        int totalItem = gridLayoutManager.getItemCount() - 1; //adapter 的 item 总数，从 0 开始计算

        //处理加载更多
        if (isPreLoad) {
          if (lastVisiblePosition > totalItem - (uiPageSize * preloadFactor) && handleDirection(gridLayoutManager)) {//默认提前一页预加载
            if (loadCompleted) {
              listener.onLoadMore();
            } else {
              listener.onLoading();
            }
            loadCompleted = false;
          }
        } else {
          if (lastVisiblePosition == totalItem && handleDirection(gridLayoutManager)) {//正常滑动到底部加载
            if (loadCompleted) {
              listener.onLoadMore();
            } else {
              listener.onLoading();
            }
            loadCompleted = false;
          }
        }
      }
    }
  }

  @Override
  public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
    super.onScrolled(recyclerView, dx, dy);
    if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
      GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
      findPosition(gridLayoutManager);
      callbackPage();
      this.dx = dx;
      this.dy = dy;
    }
  }

  /**
   * 配置项
   */
  public static class Config {

    private int uiPageSize;
    private boolean isPreLoad = true;
    private int preloadFactor = 1;
    private SnapEnum snapEnum = SnapEnum.PAGER;

    public Config(int uiPageSize) {
      if (uiPageSize <= 0) {
        throw new IllegalArgumentException();
      }
      this.uiPageSize = uiPageSize;
    }

    public Config isPreLoad(boolean isPreLoad) {
      this.isPreLoad = isPreLoad;
      return this;
    }

    /**
     * 预加载系数，该系数为 uiPageSize 的倍数、默认不设置为 1 倍
     */
    public Config preloadFactor(@IntRange(from = 1,to = 3) int preloadFactor){
      this.preloadFactor = preloadFactor;
      return this;
    }

    public Config snapStyle(SnapEnum snapEnum) {
      this.snapEnum = snapEnum;
      return this;
    }
  }

  public interface OnGridPagerHelperListener {

    void onLoadMore();

    void onLoading();

    void onPageChanged(int page);
  }

  /**
   * 翻下一页
   */
  public void smoothNextPage() {
    int i = lastVisiblePosition + uiPageSize;
    recyclerView.smoothScrollToPosition(i);
  }

  /**
   * 翻上一页
   */
  public void smoothPreviousPage() {
    int i = firstVisiblePosition - uiPageSize;
    recyclerView.smoothScrollToPosition(i);
  }


  private void callbackPage() {
    int lp = lastCompletelyVisiblePosition + 1;
    int currentPage = 1;
    if (lp % uiPageSize == 0) {//取模能取净
      currentPage = lp / uiPageSize;
    } else {
      currentPage = lp / uiPageSize + 1;
    }
    Log.e(TAG, "currentPage : " + currentPage);
    if (listener != null) {
      listener.onPageChanged(currentPage);
    }
  }

  private void findPosition(GridLayoutManager layoutManager) {
    firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
    lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
    firstCompletelyVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
    lastCompletelyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
  }

  public void setLoadCompleted(boolean loadCompleted) {
    this.loadCompleted = loadCompleted;
  }

  public void release() {
    if (recyclerView != null) {
      recyclerView.removeOnScrollListener(this);
    }
  }

  private boolean handleDirection(GridLayoutManager layoutManager) {
    int orientation = layoutManager.getOrientation();
    if (orientation == RecyclerView.HORIZONTAL) {
      return dx > 0;
    } else {
      return dy > 0;
    }
  }
}
