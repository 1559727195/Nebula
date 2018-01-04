package com.zhongtianli.nebula.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.interfaces.OnDialogFragmentforCompleted;


/**
 * Created by zhu on 2016/10/25.
 */

public class EditNameDialogFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "robin debug";
    private View mRootView;
    private TextView txt_cotent_debug,mPassWordEditText;
    private TextView mOkTextView,mCancelTextView;
    private OnDialogFragmentforCompleted onDialogFragmentforCompleted;
    private  ImageView img_share;
    private  ImageView img_remove;

    public void setOnDialogFragmentforCompleted(OnDialogFragmentforCompleted onLoginInforCompleted) {
      onDialogFragmentforCompleted = onLoginInforCompleted;
    }


    public EditNameDialogFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.edit_name_dialog,container,false);
        //添加这一行
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initData();
        initEvent();
        return mRootView;
    }

    private void initEvent() {
//        txt_cotent_debug.setFocusable(true);
//        txt_cotent_debug.setFocusableInTouchMode(true);
//        txt_cotent_debug.requestFocus();//让debug-textview获取焦点
        img_share.setOnClickListener(this);
        img_remove.setOnClickListener(this);
    }

    public static EditNameDialogFragment newInstance(String prompt){
            EditNameDialogFragment pdf = new EditNameDialogFragment();
            Bundle b = new Bundle();
            b.putString("prompt-message", prompt);
            pdf.setArguments(b);
            return pdf;
        }


    public void initView(){
        txt_cotent_debug = (TextView) mRootView.findViewById(R.id.txt_cotent_debug);
        img_share = (ImageView) mRootView.findViewById(R.id.img_share);
        img_remove = (ImageView) mRootView.findViewById(R.id.img_remove);
//        mPassWordEditText = (EditText)mRootView.findViewById(R.id.id_txt_password);
//        mOkTextView = (TextView)mRootView.findViewById(R.id.id_ok);
//        mCancelTextView = (TextView)mRootView.findViewById(R.id.id_cancel);

    }

    public void initData(){
//        mOkTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onDialogFragmentforCompleted.onDialogFragmentforCompleted(getPrompt(),mPassWordEditText.getText().toString());
//                //dialogFragment给activity传值
//                dismiss();
//            }
//        });
//        mCancelTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }

    private String getPrompt(){
        Bundle b = getArguments();
        return b.getString("str");
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( dm.widthPixels,  getDialog().getWindow().getAttributes().height );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_share://分享文本功能代码
                setImg_share();
                break;
            case R.id.img_remove://删除debug内容
                setTxt_remove();
        }
    }

    private void setTxt_remove() {//清空
        txt_cotent_debug.setText("");
    }

    private void setImg_share(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String s = txt_cotent_debug.getText().toString();
        Log.e(TAG,txt_cotent_debug.getText().toString());
        sendIntent.putExtra(Intent.EXTRA_TEXT, s);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}




