package com.zxmdly.gridpagerhelper.adapter;

import android.widget.ImageView;
import android.widget.TextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zxmdly.gridpagerhelper.R;
import com.zxmdly.gridpagerhelper.entity.Singer;
import org.jetbrains.annotations.NotNull;

/**
 * @author zhouxuming
 * @date 2021/12/20 7:12 下午
 */
public class SingerListAdapter extends BaseQuickAdapter<Singer, BaseViewHolder> {

  public SingerListAdapter() {
    super(R.layout.singer_item);
  }

  @Override
  protected void convert(@NotNull BaseViewHolder baseViewHolder, Singer singer) {
    TextView tv = baseViewHolder.getView(R.id.tv_singer_name);
    ImageView iv = baseViewHolder.getView(R.id.iv_singer_header);
    tv.setText(singer.name);
    if (baseViewHolder.getLayoutPosition() % 2 == 0) {
      iv.setImageResource(R.mipmap.jay_head);
    } else {
      iv.setImageResource(R.mipmap.jay);
    }
  }
}
