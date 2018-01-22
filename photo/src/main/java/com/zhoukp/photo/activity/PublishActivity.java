package com.zhoukp.photo.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhoukp.photo.R;
import com.zhoukp.photo.bean.Image;
import com.zhoukp.photo.utils.CacheUtils;
import com.zhoukp.photo.utils.ImageUtils;
import com.zhoukp.photo.utils.LogUtil;
import com.zhoukp.photo.view.Label;
import com.zhoukp.photo.view.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

/**
 * time：2018/1/12 14:11
 * mail：zhoukaiping@szy.cn
 * for：发布朋友圈页面
 *
 * @author zhoukp
 */

public class PublishActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int POI = 1;
    public static final int MUSIC = 2;
    public static final int CONTACT = 3;

    protected LinearLayout lllabel;
    protected TextView tvTitle, tvDone;
    protected ImageView ivPhoto;
    private NineGridlayout nineGridlayout;
    private RelativeLayout rlLocation, rlMusic, rlPublish;
    private TextView tvLocation, tvMusic, tvPublish;

    protected ArrayList<String> labelName;
    protected ArrayList<Integer> selected;
    protected ArrayList<Integer> gridLayoutSelected;
    protected ArrayList<String> gridLayoutLabelName;

    protected List<LocalMedia> selectList = new ArrayList<>();
    protected ArrayList<Image> images;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();

    }

    private void initViews() {

        gridLayoutSelected = CacheUtils.getArrayList(this, "KpGridLayout");
        gridLayoutLabelName = CacheUtils.getArrayListStr(this, "KpGridLayout_labelName");

        for (int i = 0; i < gridLayoutSelected.size(); i++) {
            LogUtil.e(gridLayoutSelected.get(i) + "");
            Label label = newItemView();
            label.tvText.setText(gridLayoutLabelName.get(gridLayoutSelected.get(i)));
            lllabel.addView(label, 0);
            LogUtil.e(gridLayoutLabelName.get(gridLayoutSelected.get(i)));
        }

        selected = CacheUtils.getArrayList(this, "gridLayoutAdd");
        labelName = CacheUtils.getArrayListStr(this, "gridLayoutAdd_labelName");

        for (int i = 0; i < selected.size(); i++) {
            LogUtil.e(selected.get(i) + "");
            Label label = newItemView();
            label.tvText.setText(labelName.get(selected.get(i)));
            lllabel.addView(label, 0);
        }
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText("发布相册");
        tvDone = (TextView) findViewById(R.id.tvDone);
        tvDone.setText("发送");
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        nineGridlayout = (NineGridlayout) findViewById(R.id.nineGridlayout);
        //定位信息
        rlLocation = (RelativeLayout) findViewById(R.id.rlLocation);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        //音乐信息
        rlMusic = (RelativeLayout) findViewById(R.id.rlMusic);
        tvMusic = (TextView) findViewById(R.id.tvMusic);
        //发布对象信息、
        rlPublish = (RelativeLayout) findViewById(R.id.rlPublish);
        tvPublish = (TextView) findViewById(R.id.tvPublish);


        lllabel = (LinearLayout) findViewById(R.id.LlLabel);
        lllabel.removeAllViews();
        lllabel.addView(newAddItem());
    }

    private void initEvent() {
        ivPhoto.setOnClickListener(this);
        rlLocation.setOnClickListener(this);
        rlMusic.setOnClickListener(this);
        rlPublish.setOnClickListener(this);
    }

    /**
     * 创建一个新的item
     *
     * @return TextView
     */
    private Label newItemView() {
        final Label label = new Label(this);
        label.tvText.setPadding(20, 10, 20, 10);
        label.ivDelete.setVisibility(View.VISIBLE);
        label.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从视图中移除label
                lllabel.removeView(label);
                //从存储中移除选中的index
                Integer indexAdd = -1;
                for (int i = 0; i < gridLayoutLabelName.size(); i++) {
                    if (gridLayoutLabelName.get(i).equals(label.tvText.getText().toString())) {
                        indexAdd = i;
                        break;
                    }
                }
                if (gridLayoutSelected.contains(indexAdd)) {
                    gridLayoutSelected.remove(indexAdd);
                }
                Integer index = -1;
                for (int i = 0; i < labelName.size(); i++) {
                    if (labelName.get(i).equals(label.tvText.getText().toString())) {
                        index = i;
                        break;
                    }
                }
                if (selected.contains(index)) {
                    selected.remove(index);
                }
            }
        });
        return label;
    }

    private TextView newAddItem() {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(40, 30, 0, 0);
        textView.setLayoutParams(params);
        textView.setPadding(20, 10, 20, 10);
        textView.setGravity(Gravity.CENTER);

        textView.setBackgroundResource(R.drawable.add_label_bg);
        textView.setTextSize(12);
        textView.setTextColor(getResources().getColor(R.color.colorBlack));
        textView.setText(" + 新建标签");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublishActivity.this, LabelActivity.class);
                intent.putExtra("KpGridLayout", arrayListToString(gridLayoutSelected));
                intent.putExtra("gridLayoutAdd", arrayListToString(selected));
                intent.putExtra("gridLayoutAdd_labelName", arrayListToStringStr(labelName));
                startActivity(intent);
            }
        });
        return textView;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Drawable drawable;
        switch (view.getId()) {
            case R.id.ivPhoto:
                //选择照片
                selectPictures();
                break;
            case R.id.rlLocation:
                drawable = getResources().getDrawable(R.drawable.icon_address_selected);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvLocation.setCompoundDrawables(drawable, null, null, null);
                intent = new Intent(PublishActivity.this, PoiActivity.class);
                startActivityForResult(intent, POI);
                break;
            case R.id.rlMusic:
                drawable = getResources().getDrawable(R.drawable.icon_music_selected);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvMusic.setCompoundDrawables(drawable, null, null, null);
                intent = new Intent(PublishActivity.this, MusicListActivity.class);
                startActivityForResult(intent, MUSIC);
                break;
            case R.id.rlPublish:
                intent = new Intent(PublishActivity.this, ContactActivity.class);
                startActivityForResult(intent, CONTACT);
                break;
            default:
                break;
        }
    }

    /**
     * 选择图片
     */
    private void selectPictures() {
        PictureSelector.create(PublishActivity.this)
                // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(PictureMimeType.ofImage())
                // 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .theme(R.style.picture_default_style)
                // 最大图片选择数量
                .maxSelectNum(9)
                // 最小选择数量
                .minSelectNum(1)
                // 每行显示个数
                .imageSpanCount(4)
                //多选 or单选
                .selectionMode(PictureConfig.MULTIPLE)
                // 是否可预览图片
                .previewImage(true)
                // 是否显示拍照按钮
                .isCamera(true)
                // 图片列表点击 缩放效果 默认true
                .isZoomAnim(true)
                // 是否裁剪
                .enableCrop(false)
                // 是否压缩
                .compress(true)
                //同步true或异步false 压缩 默认同步
                .synOrAsy(true)
                // glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .glideOverride(160, 160)
                // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .withAspectRatio(16, 9)
                // 是否显示uCrop工具栏，默认不显示
                .hideBottomControls(false)
                // 是否显示gif图片
                .isGif(false)
                // 裁剪框是否可拖拽
                .freeStyleCropEnabled(false)
                // 是否圆形裁剪
                .circleDimmedLayer(false)
                // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropFrame(false)
                // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                .showCropGrid(false)
                // 是否开启点击声音
                .openClickSound(false)
                // 是否传入已选图片
                //.selectionMedia(selectList)
                // 小于100kb的图片不压缩
                .minimumCompressSize(100)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    //九宫格选择图片回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    images = new ArrayList<>();
                    ContentResolver resolver = getContentResolver();
                    Bitmap bitmap;
                    for (LocalMedia media : selectList) {
                        LogUtil.e("imgPath==" + media.getPath());
                        bitmap = ImageUtils.getImageThumbnail(PublishActivity.this, resolver, media.getPath());
                        bitmap = ImageUtils.rotateImage(bitmap, ImageUtils.getExifOrientation(media.getPath()));
                        Image image = new Image(media.getPath(), bitmap, bitmap.getWidth(), bitmap.getHeight());
                        images.add(image);
                    }
                    nineGridlayout.setImagesData(images);
                    nineGridlayout.setVisibility(View.VISIBLE);
                    ivPhoto.setVisibility(View.GONE);
                    break;
                case POI:
                    //获取当前poi回调
                    String poi = data.getStringExtra("poi");
                    if (!TextUtils.isEmpty(poi)) {
                        tvLocation.setText(poi);
                    }
                    break;
                case MUSIC:
                    //添加音乐回调
                    String music = data.getStringExtra("music");
                    if (!TextUtils.isEmpty(music)) {
                        tvMusic.setText(music);
                    }
                    break;
                case CONTACT:
                    //选择发送对象回调
                    String object = data.getStringExtra("object");
                    if (!TextUtils.isEmpty(object)) {
                        tvPublish.setText(object);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.e("onPause");
        CacheUtils.putArrayList(PublishActivity.this, "KpGridLayout", new ArrayList<String>());
        CacheUtils.putArrayList(PublishActivity.this, "gridLayoutAdd", new ArrayList<String>());
    }

    /**
     * 链表转换为字符串
     *
     * @param arrayList 链表
     * @return String
     */
    private String arrayListToStringStr(ArrayList<String> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i) + "##";
        }
        return result;
    }

    /**
     * 链表转换为字符串
     *
     * @param arrayList 链表
     * @return String
     */
    private String arrayListToString(ArrayList<Integer> arrayList) {
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result += arrayList.get(i) + "##";
        }
        return result;
    }
}
