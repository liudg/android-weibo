package com.liudong.weibo.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liudong.weibo.BaseActivity;
import com.liudong.weibo.R;
import com.liudong.weibo.adapter.ImageBrowserAdapter;
import com.liudong.weibo.entity.PicUrls;
import com.liudong.weibo.utils.ImageUtils;

import java.io.IOException;
import java.util.ArrayList;

public class ImageBrowserActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager vp_image_brower;
    private TextView tv_image_index;
    private Button btn_save;
    private Button btn_original_image;

    private int position;
    private ArrayList<PicUrls> imgUrls;
    private ImageBrowserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browser);

        initData();
        initView();
        setData();
    }

    private void initData() {
        imgUrls = (ArrayList<PicUrls>) getIntent().getSerializableExtra("picUrls");
        position = getIntent().getIntExtra("position", 0);
    }

    private void initView() {
        vp_image_brower = (ViewPager) findViewById(R.id.vp_image_brower);
        tv_image_index = (TextView) findViewById(R.id.tv_image_index);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_original_image = (Button) findViewById(R.id.btn_original_image);

        btn_save.setOnClickListener(this);
        btn_original_image.setOnClickListener(this);
    }

    private void setData() {
        adapter = new ImageBrowserAdapter(this, imgUrls);
        vp_image_brower.setAdapter(adapter);

        final int size = imgUrls.size();
        //无限轮播，使其滚动到第一屏的时候还能继续向左滑动
        int initPosition = Integer.MAX_VALUE / 2 / size * size + position;
        if (size > 1) {
            tv_image_index.setVisibility(View.VISIBLE);
            tv_image_index.setText((position + 1) + "/" + size);
        } else {
            tv_image_index.setVisibility(View.GONE);
        }

        vp_image_brower.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int index = position % size;
                tv_image_index.setText((index + 1) + "/" + size);

                PicUrls pic = adapter.getPic(position);
                btn_original_image.setVisibility(pic.isShowOriImag() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        vp_image_brower.setCurrentItem(initPosition);
    }

    @Override
    public void onClick(View v) {
        PicUrls pic = adapter.getPic(vp_image_brower.getCurrentItem());

        switch (v.getId()) {
            case R.id.btn_save:
                Bitmap bitmap = adapter.getBitmap(vp_image_brower.getCurrentItem());
                boolean showOriImag = pic.isShowOriImag();
                String fileName = "img-" + (showOriImag ? "ori-" : "mid-") + pic.getImageId();

                //保存方法一
/*                String title = fileName.substring(0,fileName.lastIndexOf(".")); //去除后缀格式，系统保存自带有
                String insertImage = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, title, "栋的微博");
                if (insertImage == null) {
                    showToast("图片保存失败");
                }else {
                    showToast("图片保存成功");
                }*/

                //保存方法二
                try {
                    ImageUtils.saveFile(this, bitmap, fileName);
                    showToast("图片保存成功：SD卡根目录dongweibo文件夹");
                } catch (IOException e) {
                    showToast("图片保存失败");
                    e.printStackTrace();
                }

                break;
            case R.id.btn_original_image:
                pic.setShowOriImag(true);
                adapter.notifyDataSetChanged();
                btn_original_image.setVisibility(View.GONE);
                break;
        }
    }
}
