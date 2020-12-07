package com.funcoders.Instadpsaver;

import com.funcoders.Instadpsaver.bean.ContactModel;
import com.funcoders.Instadpsaver.bean.InstaBean;

import java.util.ArrayList;
import java.util.List;

public interface MainMenuview {

    void showProgressDialog();
    void hideProgressDialog();
    void displayIntaData(InstaBean bean);
    void displayImages(ArrayList<InstaBean> imgList);
    void displayVedio(ArrayList<InstaBean> vedioList);

}
