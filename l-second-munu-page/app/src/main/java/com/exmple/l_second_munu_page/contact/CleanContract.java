package com.exmple.l_second_munu_page.contact;

import com.exmple.l_second_munu_page.bean.FileTitleEntity;
import com.exmple.l_second_munu_page.v.BaseView;

import java.util.List;

/**
 * Created by lang.chen on 2019/10/12
 */
public interface CleanContract {

     interface View extends BaseView {

        void udpateFilesView();

        void updateImgChat(List<FileTitleEntity> lists);

        void cancelLoadingDialog();
    }

    interface Presenter  {

        void  getFiles();
    }

}
