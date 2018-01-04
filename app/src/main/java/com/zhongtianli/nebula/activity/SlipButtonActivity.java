package com.zhongtianli.nebula.activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.zhongtianli.nebula.R;
import com.zhongtianli.nebula.interfaces.OnChangedListener;
import com.zhongtianli.nebula.utils.FinishUtil;
import com.zhongtianli.nebula.view.SlipButton;

public class SlipButtonActivity extends AppCompatActivity implements OnChangedListener {

    private SlipButton mSlipButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip_button);
        FinishUtil.addActivity(this);
        mSlipButton = (SlipButton) this.findViewById(R.id.on);
        mSlipButton.SetOnChangedListener(this);//设置事件监听
    }
    //这里为开或者关时自己所需要做的动作或实现的内容处理
    public void OnChanged(boolean CheckState) {
        if (CheckState) {
            Toast.makeText(this, "打开了", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "关闭了", Toast.LENGTH_LONG).show();
        }
    }
}

