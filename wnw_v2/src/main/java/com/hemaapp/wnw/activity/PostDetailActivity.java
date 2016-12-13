package com.hemaapp.wnw.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hemaapp.MyConfig;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.view.ShowLargeImageView;
import com.hemaapp.luna_framework.view.MyWebView;
import com.hemaapp.wnw.MyApplication;
import com.hemaapp.wnw.MyFragmentActivity;
import com.hemaapp.wnw.MyHttpInformation;
import com.hemaapp.wnw.MyUtil;
import com.hemaapp.wnw.R;
import com.hemaapp.wnw.adapter.pager.ImageScrollPagerAdapter;
import com.hemaapp.wnw.adapter.TagGoodsAdapter;
import com.hemaapp.wnw.model.Image;
import com.hemaapp.wnw.model.NoteDetail;
import com.hemaapp.wnw.model.RecommendItem;
import com.hemaapp.wnw.model.Tag;
import com.hemaapp.wnw.nettask.ImageTask;
import com.hemaapp.wnw.util.ShareParams;
import com.hemaapp.luna_framework.view.CircleIndicator.CircleIndicator;
import com.hemaapp.wnw.view.FlowLayout.TagFlowLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 帖子详情 Created by Hufanglin on 2016/2/25.
 */
public class PostDetailActivity extends MyFragmentActivity implements
        View.OnClickListener {

    private final int LOGIN = 1;
    private final int COMMENT_LIST = 2;

    private ImageView imageQuitActivity, imageHead, imagePraise, imageLove, imageToTop;
    private TextView txtTitle, txtName, txtTimeDiff, txtChangeSubscribe,
            txtCommentCount, txtPraiseCount, txtCollectCount;
    private MyWebView webView;
    private LinearLayout layoutRecommend;// 相关推荐的列表容器
    private TagFlowLayout flowLayout;
    private View layoutCommnetCount, layoutPraiseCount, layoutCollectCount,
            layoutShare;

    private ViewPager viewPager;
    private ImageScrollPagerAdapter viewPagerAdapter;
    private CircleIndicator indicator;

    private TagGoodsAdapter tagAdapter;
    private ArrayList<Image> listImage = new ArrayList<>();
    private List<Tag> tagList = new ArrayList<>();
    private String id;// 帖子id
    private NoteDetail noteDetailModel;

    @SuppressLint("JavascriptInterface")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_post);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            id = savedInstanceState.getString("id");
            noteDetailModel = savedInstanceState
                    .getParcelable("noteDetailModel");
        }

        if (isNull(id)) {
            showTextDialog(R.string.id_empty);
        } else {
            String token = "";
            if (MyUtil.IsLogin(this)) {
                token = getApplicationContext().getUser().getToken();
            }
            getNetWorker().noteGet(token, id);
        }
        String path = getApplicationContext().getSysInitInfo().getSys_plugins() + "share/note.php?id=" + id;
        webView.loadUrl(path);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("id", id);
        outState.putParcelable("noteDetailModel", noteDetailModel);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case NOTE_GET:
            case LOVE_OPERATE:
            case REMOVE_ROOT:
            case SUB_ADD:
            case RECORD_ADD:
                showProgressDialog(R.string.loading);
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case NOTE_GET:
            case LOVE_OPERATE:
            case REMOVE_ROOT:
            case SUB_ADD:
            case RECORD_ADD:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask,
                                            HemaBaseResult hemaBaseResult) {
        MyHttpInformation information = (MyHttpInformation) hemaNetTask
                .getHttpInformation();
        switch (information) {
            case NOTE_GET:
                HemaArrayResult<NoteDetail> noteDetailResult = (HemaArrayResult<NoteDetail>) hemaBaseResult;
                noteDetailModel = noteDetailResult.getObjects().get(0);
                setData();
                addGoods();
                break;
            case LOVE_OPERATE:
            case REMOVE_ROOT:
            case SUB_ADD:
                setData();
                break;
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask,
                                           HemaBaseResult hemaBaseResult) {
        cancelProgressDialog();
        showMyOneButtonDialog(getResources().getString(R.string.blog_detail),
                hemaBaseResult.getMsg());
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        cancelProgressDialog();
    }

    @Override
    protected void findView() {
        imageQuitActivity = (ImageView) findViewById(R.id.imageQuitActivity);
        imageHead = (ImageView) findViewById(R.id.imageHead);
        imagePraise = (ImageView) findViewById(R.id.imagePraise);
        imageLove = (ImageView) findViewById(R.id.imageLove);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.blog_detail);
        webView = (MyWebView) findViewById(R.id.webView);
        txtName = (TextView) findViewById(R.id.txtName);
        txtTimeDiff = (TextView) findViewById(R.id.txtTimeDiff);
        txtChangeSubscribe = (TextView) findViewById(R.id.txtChangeSubscribe);
        txtCommentCount = (TextView) findViewById(R.id.txtCommentCount);
        txtPraiseCount = (TextView) findViewById(R.id.txtPraiseCount);
        txtCollectCount = (TextView) findViewById(R.id.txtCollectCount);

        flowLayout = (TagFlowLayout) findViewById(R.id.flowLayout);

        tagAdapter = new TagGoodsAdapter(tagList, mContext);
        flowLayout.setAdapter(tagAdapter);

        layoutRecommend = (LinearLayout) findViewById(R.id.layoutRecommend);
        layoutShare = findViewById(R.id.layoutShare);
        layoutCommnetCount = findViewById(R.id.layoutCommnetCount);
        layoutPraiseCount = findViewById(R.id.layoutPraiseCount);
        layoutCollectCount = findViewById(R.id.layoutCollectCount);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager
                .getLayoutParams();
        params.height = 330 * MyUtil.getScreenWidth(mContext) / 640;
        viewPager.setLayoutParams(params);
        viewPagerAdapter = new ImageScrollPagerAdapter(mContext, listImage);
        viewPager.setAdapter(viewPagerAdapter);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        imageToTop = (ImageView) findViewById(R.id.imageToTop);
    }

    @Override
    protected void getExras() {
        id = mIntent.getStringExtra("id");
    }

    @Override
    protected void setListener() {
        imageQuitActivity.setOnClickListener(this);
        layoutShare.setOnClickListener(this);
        layoutCommnetCount.setOnClickListener(this);
        layoutPraiseCount.setOnClickListener(this);
        layoutCollectCount.setOnClickListener(this);
        txtChangeSubscribe.setOnClickListener(this);
        imageToTop.setOnClickListener(this);
//        webView.setOnImageClickListener(new MyWebView.OnImageClickListener() {
//            @Override
//            public void clickImage(String imageUrl) {
//                showLargeView(imageUrl);
//            }
//        });

        webView.setOnMultiImageClickListener(new MyWebView.OnMultiImageClickListener() {
            @Override
            public void clickMultiImage(String position, String images) {
                String[] imagesArray = images.split(";");
                ArrayList<Image> imagesList = new ArrayList<>();
                int i = 0;
                for (String image : imagesArray) {
                    imagesList.add(new Image("", "", "", "", image, image, String.valueOf(i++)));
                }
                try {
                    int index = Integer.parseInt(position);
                    Intent intent = new Intent(PostDetailActivity.this, MyShowLargePicActivity.class);
                    intent.putExtra("position", index);
                    intent.putExtra("images", imagesList);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                } catch (Exception e) {
                    Intent intent = new Intent(PostDetailActivity.this, MyShowLargePicActivity.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("images", imagesList);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.imageQuitActivity:
                finish(R.anim.my_left_in, R.anim.right_out);
                break;
            case R.id.layoutShare://分享帖子
                if (noteDetailModel == null) {
                    return;
                }
                String client_id = "";
                if (MyUtil.IsLogin(this)) {
                    client_id = MyApplication.getInstance().getUser().getId();
                }
                new ShareParams(mContext, getApplicationContext()).DoShare("note_share", noteDetailModel.getId(), client_id, noteDetailModel.getImgurlbig(),
                        noteDetailModel.getTitle(), noteDetailModel.getContent());
                break;
            case R.id.layoutCommnetCount:
                if (noteDetailModel == null) {
                    return;
                }
                intent = new Intent(this, PostCommentActivity.class);
                intent.putExtra("id", noteDetailModel.getId());
                intent.putExtra("client_id", noteDetailModel.getClient_id());
                startActivityForResult(intent, COMMENT_LIST);
                overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
                break;
            case R.id.layoutPraiseCount:
                if (noteDetailModel == null) {
                    return;
                }
                if (MyUtil.IsLogin(this)) {
                    if ("0".equals(noteDetailModel.getFlag())) {
                        getNetWorker().praiseOperate(
                                getApplicationContext().getUser().getToken(), "2",
                                "1", noteDetailModel.getId());
                        noteDetailModel.setFlag("1");
                        noteDetailModel.setPraise_count(String.valueOf(Integer
                                .valueOf(noteDetailModel.getPraise_count()) + 1));
                    } else {
                        getNetWorker().praiseOperate(
                                getApplicationContext().getUser().getToken(), "2",
                                "2", noteDetailModel.getId());
                        noteDetailModel.setFlag("0");
                        noteDetailModel.setPraise_count(String.valueOf(Integer
                                .valueOf(noteDetailModel.getPraise_count()) - 1));
                    }
                    setData();
                } else {
                    gotoLogin();
                }
                break;
            case R.id.layoutCollectCount:
                if (noteDetailModel == null) {
                    return;
                }
                if (MyUtil.IsLogin(this)) {
                    if ("0".equals(noteDetailModel.getLove_flag())) {
                        getNetWorker().loveOperate(
                                getApplicationContext().getUser().getToken(), "2",
                                "1", noteDetailModel.getId());
                        noteDetailModel.setLove_flag("1");
                        noteDetailModel.setLove_count(String.valueOf(Integer
                                .valueOf(noteDetailModel.getLove_count()) + 1));
                    } else {
                        getNetWorker().loveOperate(
                                getApplicationContext().getUser().getToken(), "2",
                                "2", noteDetailModel.getId());
                        noteDetailModel.setLove_flag("0");
                        noteDetailModel.setLove_count(String.valueOf(Integer
                                .valueOf(noteDetailModel.getLove_count()) - 1));
                    }
                    // setData();
                } else {
                    gotoLogin();
                }
                break;
            case R.id.txtChangeSubscribe:// 订阅和取消订阅
                if (noteDetailModel == null) {
                    return;
                }
                if (MyUtil.IsLogin(this)) {
                    if ("0".equals(noteDetailModel.getSubscribe_flag())) {// 未订阅
                        getNetWorker().subAdd(
                                getApplicationContext().getUser().getToken(),
                                noteDetailModel.getClient_id(), "");
                        noteDetailModel.setSubscribe_flag("1");
                        // setData();
                    } else {
                        getNetWorker().removeRoot(
                                getApplicationContext().getUser().getToken(), "3",
                                "1", noteDetailModel.getClient_id());
                        noteDetailModel.setSubscribe_flag("0");
                        // setData();
                    }
                } else {
                    gotoLogin();
                }

                break;
            case R.id.imageToTop:
                ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
                scrollView.scrollTo(0, 0);
                break;
        }
    }

    @Override
    protected boolean onKeyBack() {
        finish(R.anim.my_left_in, R.anim.right_out);
        return super.onKeyBack();
    }

    /**
     * 充实数据
     */
    private void setData() {

        if (noteDetailModel == null) {
            return;
        }

        txtName.setText(noteDetailModel.getNickname());
        txtTimeDiff.setText(noteDetailModel.getTimeDiff());
        txtCommentCount.setText(noteDetailModel.getReplycount());
        txtPraiseCount.setText(noteDetailModel.getPraise_count());
        txtCollectCount.setText(noteDetailModel.getLove_count());

        int PraiseResource = "0".equals(noteDetailModel.getFlag()) ? R.drawable.icon_praise
                : R.drawable.icon_praise_cancel;
        imagePraise.setImageResource(PraiseResource);
        int LoveResource = "0".equals(noteDetailModel.getLove_flag()) ? R.drawable.icon_star_white_uncheck
                : R.drawable.icon_star_white;
        imageLove.setImageResource(LoveResource);
        if ("0".equals(noteDetailModel.getSubscribe_flag())) {// 未订阅
            txtChangeSubscribe.setText(R.string.subscribe_add);
            txtChangeSubscribe
                    .setBackgroundResource(R.drawable.bg_subscribe_border);
            txtChangeSubscribe.setTextColor(getResources().getColor(
                    R.color.main_purple));
        } else {
            txtChangeSubscribe.setText(R.string.subscribe_cancel);
            txtChangeSubscribe
                    .setBackgroundResource(R.drawable.bg_subscribe_purple);
            txtChangeSubscribe.setTextColor(getResources().getColor(
                    R.color.white));
        }

        tagList.clear();
        tagList.addAll(noteDetailModel.getTableItems());
        tagAdapter.notifyDataChanged();

        try {// 设置头像
            URL url = new URL(noteDetailModel.getAvatar());
            ImageTask imageTask = new ImageTask(imageHead, url, mContext,
                    R.drawable.icon_register_head);
            imageWorker.loadImage(imageTask);
        } catch (MalformedURLException e) {
            imageHead.setImageResource(R.drawable.icon_register_head);
            e.printStackTrace();
        }

        listImage.clear();
        listImage.addAll(noteDetailModel.getImgItems());
        viewPagerAdapter = new ImageScrollPagerAdapter(mContext, listImage);
        viewPager.setAdapter(viewPagerAdapter);
        indicator.setViewPager(viewPager);
    }

    /**
     * 添加推荐商品
     */
    private void addGoods() {
        layoutRecommend.removeAllViews();

        for (final RecommendItem recommendItem : noteDetailModel
                .getRecomendItems()) {
            View view = LayoutInflater.from(mContext).inflate(
                    R.layout.listitem_goods, layoutRecommend, false);
            ViewHolder holder = new ViewHolder(view);
            holder.txtName.setText(recommendItem.getName());
            holder.txtPrize.setText("￥" + recommendItem.getPrice());
            holder.txtOldPrize.setText("￥" + recommendItem.getOldprice());
            holder.txtHasSell.setText("已售" + recommendItem.getPaycount());
            try {
                URL url = new URL(recommendItem.getImgurl());
                ImageTask imageTask = new ImageTask(holder.imageView, url,
                        mContext, R.drawable.logo_default_square);
                imageWorker.loadImage(imageTask);

            } catch (MalformedURLException e) {
                e.printStackTrace();
                holder.imageView
                        .setImageResource(R.drawable.logo_default_square);
            }
            holder.layoutFather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PostDetailActivity.this,
                            GoodsDetailActivity.class);
                    intent.putExtra("goods_id", recommendItem.getId());
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,
                            R.anim.my_left_out);
                }
            });
            holder.txtBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PostDetailActivity.this,
                            GoodsDetailActivity.class);
                    intent.putExtra("goods_id", recommendItem.getId());
                    intent.putExtra("ShowDialog", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in,
                            R.anim.my_left_out);
                }
            });
            layoutRecommend.addView(view);
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("AcitvityType", MyConfig.LOGIN);
        startActivityForResult(intent, LOGIN);
        overridePendingTransition(R.anim.right_in, R.anim.my_left_out);
    }

    private class ViewHolder {
        public ViewHolder(View convertView) {
            imageView = (ImageView) convertView.findViewById(R.id.imageView);
            txtOldPrize = (TextView) convertView.findViewById(R.id.txtOldPrize);
            txtOldPrize.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            txtOldPrize.getPaint().setAntiAlias(true);
            layoutFather = convertView.findViewById(R.id.layoutFather);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtHasSell = (TextView) convertView.findViewById(R.id.txtHasSell);
            txtPrize = (TextView) convertView.findViewById(R.id.txtPrize);
            txtBuy = (TextView) convertView.findViewById(R.id.txtBuy);
        }

        private ImageView imageView;
        private TextView txtName, txtHasSell, txtPrize, txtOldPrize, txtBuy;
        private View layoutFather;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case COMMENT_LIST:
                String token = "";
                if (MyUtil.IsLogin(this)) {
                    token = getApplicationContext().getUser().getToken();
                }
                getNetWorker().noteGet(token, id);
                break;
        }
    }

    private ShowLargeImageView mView;// 展示大图

    private void showLargeView(String img) {

        mView = new ShowLargeImageView(mContext,
                findViewById(R.id.father));
        mView.setImageURL(img);
        mView.show();
    }
}
