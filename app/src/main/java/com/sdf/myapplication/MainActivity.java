package com.sdf.myapplication;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<LocalMedia> selectAudioList = new ArrayList<>();
    private List<LocalMedia> selectPictList = new ArrayList<>();
    private List<LocalMedia> selectVideoList = new ArrayList<>();
    private int clickType = 5;

    private int maxSelectNum = 1000;

    private Button mAudioBtn, mImgBtn,mVideoBtn,mAudioDelBtn,mImgDelBtn,mVideoDelBtn;
    private RecyclerView mAudioRecycler;
    private RecyclerView mPicRecycler;
    private RecyclerView mVideoRecycler;
    private SelectorImageAdapter audioAdapter, picAdapter, videoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAudioBtn = (Button) findViewById(R.id.btn_audio);
        mImgBtn = (Button) findViewById(R.id.btn_pic);
        mVideoBtn = (Button) findViewById(R.id.btn_video);

        mAudioDelBtn = (Button) findViewById(R.id.btn_del_audio);
        mImgDelBtn = (Button) findViewById(R.id.btn_del_img);
        mVideoDelBtn = (Button) findViewById(R.id.btn_del_video);

        mAudioRecycler = (RecyclerView) findViewById(R.id.recycler_audio);
        mPicRecycler = (RecyclerView) findViewById(R.id.recycler_pic);
        mVideoRecycler = (RecyclerView) findViewById(R.id.recycler_video);

        mAudioBtn.setOnClickListener(this);
        mImgBtn.setOnClickListener(this);
        mVideoBtn.setOnClickListener(this);

        mAudioDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
////                int index = mAudioRecycler.getAdapterPosition();
////                if (index != RecyclerView.NO_POSITION) {
//                    selectAudioList.removeAll();
//                    mAudioRecycler.notifyItemRemoved(index);
//                    mAudioRecyclernotifyItemRangeChanged(index, list.size());
////                }
            }
        });
        mImgDelBtn.setOnClickListener(this);
        mVideoDelBtn.setOnClickListener(this);

        //Item之间的间距
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(SelectorItemDecoration.TOP_DECORATION,10);//top间距

        stringIntegerHashMap.put(SelectorItemDecoration.BOTTOM_DECORATION,10);//底部间距

        stringIntegerHashMap.put(SelectorItemDecoration.LEFT_DECORATION,10);//左间距

        stringIntegerHashMap.put(SelectorItemDecoration.RIGHT_DECORATION,10);//右间距


        SelectorGridLayoutManager audioManager = new SelectorGridLayoutManager(MainActivity.this, 3, GridLayoutManager.VERTICAL, false);
        mAudioRecycler.setLayoutManager(audioManager);
        mAudioRecycler.addItemDecoration(new SelectorItemDecoration(stringIntegerHashMap));
        audioAdapter = new SelectorImageAdapter(MainActivity.this, onAddAudioClickListener);
        audioAdapter.setList(selectAudioList);
//        audioAdapter.setSelectMax(maxSelectNum);
        mAudioRecycler.setAdapter(audioAdapter);
        audioAdapter.setOnItemClickListener(new SelectorImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectAudioList.size() > 0) {
                    LocalMedia media = selectAudioList.get(position);
                    PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                }
            }
        });

        SelectorGridLayoutManager picManager = new SelectorGridLayoutManager(MainActivity.this, 3, GridLayoutManager.VERTICAL, false);
        mPicRecycler.setLayoutManager(picManager);
        mPicRecycler.addItemDecoration(new SelectorItemDecoration(stringIntegerHashMap));
        picAdapter = new SelectorImageAdapter(MainActivity.this, onAddPicClickListener);
        picAdapter.setList(selectPictList);
//        picAdapter.setSelectMax(maxSelectNum);
        mPicRecycler.setAdapter(picAdapter);
        picAdapter.setOnItemClickListener(new SelectorImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectPictList.size() > 0) {
                    PictureSelector.create(MainActivity.this).themeStyle(
                            R.style.picture_default_style).openExternalPreview(position, selectPictList);
                }
            }
        });

        SelectorGridLayoutManager videoManager = new SelectorGridLayoutManager(MainActivity.this, 3, GridLayoutManager.VERTICAL, false);
        mVideoRecycler.setLayoutManager(videoManager);
        mVideoRecycler.addItemDecoration(new SelectorItemDecoration(stringIntegerHashMap));
        videoAdapter = new SelectorImageAdapter(MainActivity.this, onAddVideoClickListener);
        videoAdapter.setList(selectVideoList);
//        videoAdapter.setSelectMax(maxSelectNum);
        mVideoRecycler.setAdapter(videoAdapter);
        videoAdapter.setOnItemClickListener(new SelectorImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectVideoList.size() > 0) {
                    LocalMedia media = selectVideoList.get(position);
                    PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());

                }
            }
        });

        // 上传完成后调用 同时获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(MainActivity.this);
                } else {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private SelectorImageAdapter.onAddClickListener onAddAudioClickListener = new SelectorImageAdapter.onAddClickListener() {
        @Override
        public void onAddClick() {
            PictureSelector.create(MainActivity.this)
                    .openGallery(PictureMimeType.ofAudio())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(false)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .isGif(true)// 是否显示gif图片
                    .selectionMedia(selectAudioList)// 是否传入已选图片
                    .previewEggs(true)// 预览图片时 增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.TYPE_AUDIO);//结果回调onActivityResult code
        }
    };

    private SelectorImageAdapter.onAddClickListener onAddPicClickListener = new SelectorImageAdapter.onAddClickListener() {
        @Override
        public void onAddClick() {
            PictureSelector.create(MainActivity.this)
                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(false)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                    .isGif(true)// 是否显示gif图片
                    .selectionMedia(selectPictList)// 是否传入已选图片
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.TYPE_IMAGE);//结果回调onActivityResult code
        }
    };

    private SelectorImageAdapter.onAddClickListener onAddVideoClickListener = new SelectorImageAdapter.onAddClickListener() {
        @Override
        public void onAddClick() {
            PictureSelector.create(MainActivity.this)
                    .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.MULTIPLE)// 多选
                    .previewImage(true)// 是否可预览图片
                    .previewVideo(true)// 是否可预览视频
                    .enablePreviewAudio(true) // 是否可播放音频
                    .isCamera(false)// 是否显示拍照按钮
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .enableCrop(false)// 是否裁剪
                    .compress(false)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                    .isGif(true)// 是否显示gif图片
                    .selectionMedia(selectVideoList)// 是否传入已选图片
                    .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.TYPE_VIDEO);//结果回调onActivityResult code
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.TYPE_AUDIO:
                    // 图片选择结果回调
                    selectAudioList = PictureSelector.obtainMultipleResult(data);
                    // LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准
                    for (LocalMedia media : selectAudioList) {
                        KLog.e("音频-----》", media.getPath());
                    }
                    audioAdapter.setList(selectAudioList);
                    audioAdapter.notifyDataSetChanged();
                    break;
                case PictureConfig.TYPE_IMAGE:
                    selectPictList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectPictList) {
                        KLog.e("图片-----》", media.getPath());
                    }
                    picAdapter.setList(selectPictList);
                    picAdapter.notifyDataSetChanged();
                    break;
                case PictureConfig.TYPE_VIDEO:
                    selectVideoList = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia media : selectVideoList) {
                        KLog.e("视频-----》", media.getPath());
                    }
                    videoAdapter.setList(selectVideoList);
                    videoAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_audio:
                audioAdapter.mOnAddClickListener.onAddClick();
                break;
            case R.id.btn_pic:
                picAdapter.mOnAddClickListener.onAddClick();
                break;
            case R.id.btn_video:
                videoAdapter.mOnAddClickListener.onAddClick();
                break;
            default:
                break;
        }
    }
}
