package com.zxmdly.gridpagerhelper.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zxmdly.gridpagerhelper.adapter.SingerListAdapter;
import com.zxmdly.gridpagerhelper.databinding.FragmentSampleBinding;
import com.zxmdly.gridpagerhelper.entity.Singer;
import com.zxmdly.gridpagerhelper.helper.GridPagerHelper;
import com.zxmdly.gridpagerhelper.helper.GridPagerHelper.Config;
import com.zxmdly.gridpagerhelper.helper.GridPagerHelper.OnGridPagerHelperListener;
import com.zxmdly.gridpagerhelper.helper.SnapEnum;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouxuming
 * @date 2021/12/23 11:03 上午
 */
public class SampleFragment extends Fragment implements OnGridPagerHelperListener {

  private FragmentSampleBinding binding;
  private SingerListAdapter adapter;
  private int requestCount = 30; //单次请求数据大小
  private int dataPage = 0;//数据页码
  private GridPagerHelper helper;


  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState
  ) {
    binding = FragmentSampleBinding.inflate(inflater, container, false);
    initView();
    initData();
    return binding.getRoot();

  }

  private void initData() {
    adapter.setList(buildFakeData(dataPage));
  }

  private void initView() {
    adapter = new SingerListAdapter();
    GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3,
        GridLayoutManager.HORIZONTAL, false);
    binding.rv.setLayoutManager(layoutManager);
    binding.rv.setAdapter(adapter);
    helper = new GridPagerHelper(binding.rv, new Config(15).snapStyle(SnapEnum.LINEAR))
        .buildListener(this);
    binding.left.setOnClickListener(v -> helper.smoothPreviousPage());
    binding.right.setOnClickListener(v -> helper.smoothNextPage());
    binding.changeLayout.setOnClickListener(v -> {
      int orientation = layoutManager.getOrientation();
      if (orientation == RecyclerView.HORIZONTAL) {
        layoutManager.setOrientation(RecyclerView.VERTICAL);
      } else {
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
      }
    });
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }


  @Override
  public void onLoading() {

  }

  @Override
  public void onPageChanged(int page) {
    if (page <= 0) {
      page = 1;
    }
    binding.page.setText(String.valueOf(page));
  }

  @Override
  public void onLoadMore() {
    new Handler().postDelayed(() -> {
      helper.setLoadCompleted(true);
      List<Singer> singers = buildFakeData(++dataPage);
      if (singers == null || singers.size() == 0) {//模拟最后一页
        --dataPage;
        Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_SHORT).show();
      } else {
        adapter.addData(singers);
      }
    }, 150);//模拟网络耗时
  }

  /**
   * 假数据生产
   */
  private List<Singer> buildFakeData(int page) {
    List<Singer> data = new ArrayList<>();
    if (page == 5) { //模拟非整页数据
      data.add(new Singer("单个_" + page));
      return data;
    }
    if (page == 6) {//模拟没有数据了
      return null;
    }
    for (int i = 0; i < 45; i++) {//正常数据
      data.add(new Singer(i + "_" + page));
    }
    return data;
  }

}
