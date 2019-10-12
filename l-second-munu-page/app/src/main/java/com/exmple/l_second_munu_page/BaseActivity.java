package com.exmple.l_second_munu_page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by lang.chen on 2019/8/15
 */
public abstract  class BaseActivity<T> extends AppCompatActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initPresenter();
        
    }


    protected  abstract void initViews();
    protected  abstract void  initPresenter();

}
