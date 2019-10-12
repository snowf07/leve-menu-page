package com.exmple.l_second_munu_page.p;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.exmple.l_second_munu_page.R;
import com.exmple.l_second_munu_page.bean.FileChildEntity;
import com.exmple.l_second_munu_page.bean.FileTitleEntity;
import com.exmple.l_second_munu_page.contact.CleanContract;
import com.exmple.l_second_munu_page.utils.DateUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lang.chen on 2019/8/21
 */
public class CleanPresenter implements CleanContract.Presenter {


    private static String TAG="CleanPresenter.class";
    private  CleanContract.View mView;

    /**
     * 微信根目录
     */
    private String wxRootPath = Environment.getExternalStorageDirectory().getPath() + "/tencent/MicroMsg";

    /**
     * 图片存储路径
     */
    private String path;

    /**
     * 聊天图片
     */
    public List<FileTitleEntity> listsChat = new ArrayList<>();




    public CleanPresenter(CleanContract.View view){
        mView=view;
        mView.setPresenter(this);
    }

    @Override
    public void getFiles() {

        getImgChat();
    }



    /**
     * 在扫描文件时，先要调用这个来查找正确路径
     * 获取聊天图片保存目录
     * <p>
     * 初始化 一级目录标题
     */
    public void init(Context context) {
        path = getPath(wxRootPath);
        String[] titles = context.getResources().getStringArray(R.array.wx_file_titles);
        for (int i = 0; i < titles.length; i++) {
            //聊天图片
            FileTitleEntity fileEntity = new FileTitleEntity();
            fileEntity.title = titles[i];
            fileEntity.type = i;
            fileEntity.id = String.valueOf(i);
            listsChat.add(fileEntity);
        }

    }




    private void getImgChat() {

        Observable.create((ObservableOnSubscribe<String>) emitter -> {

            //scanAllImgChat(path);
            scanAllImagesChat(wxRootPath);
            emitter.onNext("");
            emitter.onComplete();
        })
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String value) {
                        totalFileSize(listsChat);
                        mView.updateImgChat(listsChat);


                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                        mView.cancelLoadingDialog();
                    }
                });

    }



    public   void totalFileSize(List<FileTitleEntity> lists){
        if(null==lists ||  lists.size()==0){
            return;
        }



        for(FileTitleEntity fileTitleEntity: lists){
            long size=0L;
            for(FileChildEntity fileChildEntity:fileTitleEntity.lists){
                size+=fileChildEntity.size;
            }
            fileTitleEntity.size=size;
        }
    }




    /**
     * 获取缓存文件目录
     * <p>
     * 根据目录 长度20位 并且包含emoji目录
     */
    private String getPath(String wxRootPath) {
        File fileRoot = new File(wxRootPath);
        if (fileRoot.isDirectory()) {
            File[] files = fileRoot.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String fileName = file.getName();
                        if (fileName.length() > 20) {
                            File[] filesNext = file.listFiles();
                            if (null != filesNext) {
                                for (File f : filesNext) {
                                    if (f.getName().equals("emoji")) {
                                        Log.i("TAG", file.getPath());
                                        return file.getPath();

                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
        return null;
    }




    /**
     * 扫描聊天中的图片，包括缩略图
     *
     * @param path
     */
    private void scanAllImagesChat(String path) {
        File fileRoot = new File(path);
        if (fileRoot.isDirectory()) {
            File[] files = fileRoot.listFiles();
            if (null != files) {
                for (File file1 : files) { //第一层
                    if (file1.isDirectory() && file1.getName().length() > 20) {
                        File[] files2 = file1.listFiles();
                        if (null != files2) {
                            for (File file2C : files2) { //第2层  /a/ 遍历
                                if (file2C.isDirectory() && (file2C.getName().equals("image2") || file2C.getName().equals("image"))) {
                                    File[] files3 = file2C.listFiles(); //
                                    if (null != files3) {
                                        for (File file : files3) { //第3层  /a/image2/
                                            scanAllImagesChild(file.getPath());
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }


    private  void scanAllImagesChild(String  path) {
        File fileRoot = new File(path);

        if (fileRoot.isDirectory()) {
            File[] files = fileRoot.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        scanAllImagesChild(path + "/" + file.getName());
                    } else{
                        if (file.getName().startsWith("th_")) {
                            FileChildEntity fileChildEntity = new FileChildEntity();
                            fileChildEntity.name = file.getName() + ".jpg";
                            fileChildEntity.path = file.getPath();
                            fileChildEntity.size = file.length();
                            fileChildEntity.parentId = FileTitleEntity.Type.TH;
                            Log.i(TAG, "file=" + file.getName() + ",path" + file.getPath() + ",time=" + file.lastModified());
                            listsChat.get(FileTitleEntity.Type.TH).lists.add(fileChildEntity);
                        } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                            FileChildEntity fileChildEntity = new FileChildEntity();
                            fileChildEntity.name = file.getName();
                            fileChildEntity.path = file.getPath();
                            fileChildEntity.size = file.length();

                            if (DateUtils.isSameDay(System.currentTimeMillis(), file.lastModified())) {
                                //是否为今天
                                fileChildEntity.parentId = FileTitleEntity.Type.TODAY;
                                listsChat.get(FileTitleEntity.Type.TODAY).lists.add(fileChildEntity);
                            } else if (DateUtils.isYesterday(file.lastModified())) {
                                //是否为昨天
                                fileChildEntity.parentId = FileTitleEntity.Type.YESTERDAY;
                                listsChat.get(FileTitleEntity.Type.YESTERDAY).lists.add(fileChildEntity);
                            } else if (DateUtils.isSameMonth(System.currentTimeMillis(), file.lastModified())) {
                                //是否为同一个月
                                fileChildEntity.parentId = FileTitleEntity.Type.MONTH;
                                listsChat.get(FileTitleEntity.Type.MONTH).lists.add(fileChildEntity);
                            } else {
                                //是否为半年内
                                fileChildEntity.parentId = FileTitleEntity.Type.YEAR_HALF;
                                listsChat.get(FileTitleEntity.Type.YEAR_HALF).lists.add(fileChildEntity);
                            }
                        }
                    }
                }
            }
        }
    }



}
