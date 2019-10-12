package com.exmple.l_second_munu_page;

import android.os.Bundle;
import android.util.Log;

import com.exmple.l_second_munu_page.bean.FileTitleEntity;
import com.exmple.l_second_munu_page.contact.CleanContract;
import com.exmple.l_second_munu_page.p.CleanPresenter;

import java.util.List;

public class MainActivity extends BaseActivity<CleanPresenter> implements CleanContract.View{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter.getFiles();

    }

    @Override
    protected void initViews() {


    }

    @Override
    protected void initPresenter() {
        mPresenter=new CleanPresenter(this);
        mPresenter.init(getApplicationContext());
    }


    @Override
    public void setPresenter(Object presenter) {
    }

    @Override
    public void udpateFilesView() {

        Log.i("test","udpateFilesView()");
    }

    @Override
    public void updateImgChat(List<FileTitleEntity> lists) {

    }

    @Override
    public void cancelLoadingDialog() {

    }
}
