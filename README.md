# GridPagerHelper
一款支持动态横竖模式加载更多、滑动惯性定位、页码计算的列表辅助器

![image](https://github.com/13120241790/GridPagerHelper/blob/main/gridpagerhelper.gif)

![image](https://github.com/13120241790/GridPagerHelper/blob/main/screenshot_1.png)
![image](https://github.com/13120241790/GridPagerHelper/blob/main/screenshot_2.png)



## 概述

需求在横屏、竖屏上支持加载更多，既要支持手滑分页、还要支持点击按钮前后翻页以及页码联动。调研了一下市面上有的加载更多库例如 smart 、 smooth 等，功能很强大，但是在横竖屏动态切换的时候就没有适配得更好，需要单独再包裹横向的 layout ，鉴于需求也不需要那么多功能即自定义。



## 功能

- 自定义加载更多

- 预加载支持

- 页码计算回调

- 支持手动滑动翻页以及按钮点击翻页

- 惯性定位可选

  

## 使用

```java
    helper = new GridPagerHelper(recyclerView, new Config(15).snapStyle(SnapEnum.PAGER))
        .buildListener(new OnGridPagerHelperListener() {
          @Override
          public void onLoadMore() {
            
          }

          @Override
          public void onLoading() {

          }

          @Override
          public void onPageChanged(int page) {

          }
        });

//按钮翻页
 binding.left.setOnClickListener(v -> helper.smoothPreviousPage());
 binding.right.setOnClickListener(v -> helper.smoothNextPage());
```



## API

| 方法名                                  | 释义                         |
| --------------------------------------- | ---------------------------- |
| release                                 | 释放移除监听                 |
| setLoadCompleted                        | 设置加载状态                 |
| smoothNextPage                          | 翻下一页                     |
| smoothPreviousPage                      | 翻上一页                     |
| OnGridPagerHelperListener#onLoadMore    | 加载更多                     |
| OnGridPagerHelperListener#onLoading     | 加载中                       |
| OnGridPagerHelperListener#onPageChanged | 页码改变                     |
| Config#isPreLoad                        | 是否开启预加载               |
| Config(int uiPageSize)                  | 构造#每页个数                |
| SnapEnum(LINEAR,PAGER,NONE)             | 滑动惯性定位[线性、翻页、空] |



## 支持

- Issue
- QQ：623414533
