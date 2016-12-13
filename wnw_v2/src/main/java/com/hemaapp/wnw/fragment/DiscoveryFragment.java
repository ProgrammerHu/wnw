package com.hemaapp.wnw.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.wnw.MyAdapter;
import com.hemaapp.wnw.MyFragment;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.activity.DiscoveryGoodsListActivity;
import com.hemaapp.wnw.activity.DiscoveryGoodsRankingListActivity;
import com.hemaapp.wnw.activity.FansRankingListActivity;
import com.hemaapp.wnw.activity.GoodsDetailActivity;
import com.hemaapp.wnw.activity.MainFragmentActivity;
import com.hemaapp.wnw.activity.UserGoodsListActivity;
import com.hemaapp.wnw.adapter.DiscoveryMainAdapter;
import com.hemaapp.wnw.model.DiscoveryTypeList;
import com.hemaapp.wnw.model.FansModel;
import com.hemaapp.wnw.model.MerchantListModel;
import com.hemaapp.wnw.model.TypeGoodsList;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.result.MyArrayResult;
import com.hemaapp.wnw.view.CircleImageView;
import com.hemaapp.wnw.view.MeasureGridView;
import com.hemaapp.wnw.view.MyRefreshLoadmoreLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * 发现界面
 * Created by Hufanglin on 2016/2/22.
 * v2.0.0废弃
 */
public class DiscoveryFragment extends MyFragment implements View.OnClickListener {
    private MainFragmentActivity activity;

    private XtomListView listView;
    private MyRefreshLoadmoreLayout refreshLoadmoreLayout;
    private DiscoveryMainAdapter adapter;
    private LinearLayout layoutSaleCharts, layoutFansCharts, layoutImageSquare, layoutImageCircle;
    private MeasureGridView gridView;
    private GridViewAdapter gridViewAdapter;
    private ImageView imageToTop;

    private ArrayList<DiscoveryTypeList> typeLists = new ArrayList<>();
    private ArrayList<FansModel> fansModels = new ArrayList<>();
    private ArrayList<MerchantListModel> listData = new ArrayList<>();
    private ArrayList<TypeGoodsList> monthSales = new ArrayList<>();//月销量排行
    private int page = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_discovery);
        super.onCreate(savedInstanceState);
        showProgressDialog(R.string.loading);
        getNetWorker().monthSale();
        addCircleImage();
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {

    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case MONTH_SALE:
                MyArrayResult<TypeGoodsList> monthSaleRes = (MyArrayResult<TypeGoodsList>) hemaBaseResult;
                monthSales.clear();
                for (int i = 0; i < monthSaleRes.getObjects().size() && i < 4; i++) {
                    monthSales.add(monthSaleRes.getObjects().get(i));
                }
                addImageTop();
                getNetWorker().fans();
                break;
            case FANS:
                MyArrayResult<FansModel> fResult = (MyArrayResult<FansModel>) hemaBaseResult;
                fansModels.clear();
                for (int i = 0; i < fResult.getObjects().size() && i < 4; i++) {
                    fansModels.add(fResult.getObjects().get(i));
                }
                addCircleImage();
                getNetWorker().typeList();
                break;
            case TYPE_LIST:
                MyArrayResult<DiscoveryTypeList> typeListRes = (MyArrayResult<DiscoveryTypeList>) hemaBaseResult;
                typeLists.clear();
                typeLists.addAll(typeListRes.getObjects());
                gridViewAdapter.notifyDataSetChanged();
                getNetWorker().merchantList(String.valueOf(page));
                break;
            case MERCHANT_LIST:
                cancelProgressDialog();
                refreshLoadmoreLayout.refreshSuccess();
                refreshLoadmoreLayout.loadmoreSuccess();
                MyArrayResult<MerchantListModel> merchantRes = (MyArrayResult<MerchantListModel>) hemaBaseResult;
                if (page == 0) {
                    listData.clear();
                }
                listData.addAll(merchantRes.getObjects());
                adapter.notifyDataSetChanged();
                refreshLoadmoreLayout.setLoadmoreable(merchantRes.getObjects().size() >=
                        activity.getApplicationContext().getSysInitInfo().getSys_pagesize());
                page++;
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
        refreshLoadmoreLayout.refreshFailed();
        refreshLoadmoreLayout.loadmoreFailed();
    }

    @Override
    protected void findView() {
        activity = (MainFragmentActivity) getActivity();
        listView = (XtomListView) findViewById(R.id.listView);
        refreshLoadmoreLayout = (MyRefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        View headerView = LayoutInflater.from(activity).inflate(R.layout.header_discovery, listView, false);
        listView.addHeaderView(headerView);
        adapter = new DiscoveryMainAdapter(activity, listView, listData);
        listView.setAdapter(adapter);
        layoutSaleCharts = (LinearLayout) listView.findViewById(R.id.layoutSaleCharts);
        layoutFansCharts = (LinearLayout) listView.findViewById(R.id.layoutFansCharts);
        layoutImageSquare = (LinearLayout) listView.findViewById(R.id.layoutImageSquare);
        layoutImageCircle = (LinearLayout) listView.findViewById(R.id.layoutImageCircle);
        gridView = (MeasureGridView) listView.findViewById(R.id.gridView);
        gridViewAdapter = new GridViewAdapter(activity);
        gridView.setAdapter(gridViewAdapter);
        imageToTop = (ImageView) findViewById(R.id.imageToTop);
    }

    @Override
    protected void setListener() {
        layoutSaleCharts.setOnClickListener(this);
        layoutFansCharts.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                getNetWorker().monthSale();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                getNetWorker().merchantList(String.valueOf(page));
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    /**
     * 添加月销量排行
     */
    private void addImageTop() {
        int width = (MyUtil.getScreenWidth(activity) - MyUtil.dip2px(activity, 15) * 5) / 4;
        layoutImageSquare.removeAllViews();
        for (int i = 0; i < monthSales.size() && i < 4; i++) {
            final TypeGoodsList model = monthSales.get(i);
            View view = LayoutInflater.from(activity).inflate(R.layout.image_discovery_square, null, false);
            CardView cardView = (CardView) view.findViewById(R.id.cardView);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(model.getName());
            cardView.getLayoutParams().height = cardView.getLayoutParams().width = width;
            try {
                URL url = new URL(model.getImgurl());
                ImageTask task = new ImageTask(imageView, url, activity, R.drawable.logo_default_square);
                imageWorker.loadImage(task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.logo_default_square);
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, GoodsDetailActivity.class);
                    intent.putExtra("goods_id", model.getId());
                    intent.putExtra("ShowDialog", false);
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }
            });

            layoutImageSquare.addView(view);
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int visibility = firstVisibleItem == 0 ? View.INVISIBLE
                        : View.VISIBLE;
                imageToTop.setVisibility(visibility);
            }
        });
    }

    /**
     * 添加月粉丝排行
     */
    private void addCircleImage() {
        int width = (MyUtil.getScreenWidth(activity) - MyUtil.dip2px(activity, 15) * 5) / 4;
        int marginLeft = MyUtil.dip2px(activity, 15);
        layoutImageCircle.removeAllViews();
        for (int i = 0; i < fansModels.size(); i++) {
            final FansModel fansModel = fansModels.get(i);
            CircleImageView imageView = new CircleImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
            params.leftMargin = marginLeft;
            params.topMargin = params.bottomMargin = MyUtil.dip2px(activity, 10);
            imageView.setLayoutParams(params);
            try {
                URL url = new URL(fansModel.getAvatar());
                ImageTask task = new ImageTask(imageView, url, activity, R.drawable.logo_default_square);
                imageWorker.loadImage(task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                imageView.setImageResource(R.drawable.logo_default_square);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, UserGoodsListActivity.class);
                    intent.putExtra("FansModel", fansModel);
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }
            });
            layoutImageCircle.addView(imageView);
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.layoutSaleCharts:
                intent = new Intent(activity, DiscoveryGoodsRankingListActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutFansCharts:
                intent = new Intent(activity, FansRankingListActivity.class);
                startActivity(intent);
                activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.imageToTop:
                listView.smoothScrollToPosition(0);
                break;

        }
    }


    /**
     * 在header中的标签使用是适配器
     */
    class GridViewAdapter extends MyAdapter {

        public GridViewAdapter(Context mContext) {
            super(mContext);
        }

        @Override
        public int getCount() {
            return typeLists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_image_text, null, false);
            ViewHolder holder = new ViewHolder(convertView);
            holder.textView.setText(typeLists.get(position).getName());
            try {
                URL url = new URL(typeLists.get(position).getImgurl());
                ImageTask task = new ImageTask(holder.imageView, url, mContext, R.drawable.logo_default_square);
                gridView.addTask(position, 0, task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                holder.imageView.setImageResource(R.drawable.logo_default_square);
            }
            convertView.findViewById(R.id.layoutFather).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, DiscoveryGoodsListActivity.class);
                    intent.putExtra("type_id", typeLists.get(position).getId());
                    intent.putExtra("title", typeLists.get(position).getName());
                    startActivity(intent);
                    activity.overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            private ImageView imageView;
            private TextView textView;

            public ViewHolder(View convertView) {
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                textView = (TextView) convertView.findViewById(R.id.textView);
            }
        }
    }
}

