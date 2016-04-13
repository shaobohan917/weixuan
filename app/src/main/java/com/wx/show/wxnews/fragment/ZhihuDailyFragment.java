package com.wx.show.wxnews.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;
import com.wx.show.wxnews.R;
import com.wx.show.wxnews.activity.HomeActivity;
import com.wx.show.wxnews.adapter.HomeZhihuDailyAdapter;
import com.wx.show.wxnews.entity.ZhihuDaily;
import com.wx.show.wxnews.util.DateUtil;
import com.wx.show.wxnews.util.ToastUtil;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Luka on 2016/3/24.
 * E-mail:397308937@qq.com
 */

@SuppressLint("ValidFragment")
public class ZhihuDailyFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener,DatePickerDialog.OnDateSetListener {

    @Bind(R.id.tv_date_now)
    TextView tvDateNow;
    @Bind(R.id.tv_date_select)
    TextView tvDateSelect;
    @Bind(R.id.rl_date)
    RelativeLayout rlDate;
    @Bind(R.id.recyclerView)
    PullLoadMoreRecyclerView mRecyclerView;

    private HomeActivity activity;
    private HomeZhihuDailyAdapter mAdapter;

    public ZhihuDailyFragment() {
    }

    public ZhihuDailyFragment(HomeActivity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, null);
        ButterKnife.bind(this, view);

        mRecyclerView.setLinearLayout();
        mRecyclerView.setPushRefreshEnable(false);
        mRecyclerView.setOnPullLoadMoreListener(this);

        rlDate.setVisibility(View.VISIBLE);
        tvDateNow.setText("当前日期:" +DateUtil.getCurrentDate("date"));
        tvDateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDate();
            }
        });
        return view;
    }

    private void chooseDate() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setAccentColor(getResources().getColor(R.color.mdtp_accent_color));
        dpd.show(activity.getFragmentManager(),"Datepickerdialog");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity.getZhihuDaily(DateUtil.getCurrentDate("date"));
    }

    public void setData(List<ZhihuDaily.StoriesBean> data) {
        if (mAdapter == null) {
            mAdapter = new HomeZhihuDailyAdapter(activity, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRecyclerView.setPullLoadMoreCompleted();
        activity.disLoading();
    }

    @Override
    public void onRefresh() {
        activity.getZhihuDaily(DateUtil.getCurrentDate("date"));
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String month = monthOfYear + 1 + "";
        String day = dayOfMonth + "";
        if (monthOfYear < 10) {
            month = 0 + "" + (monthOfYear + 1);
        }
        if (dayOfMonth < 10) {
            day = 0 + "" + dayOfMonth;
        }
        String date = year + month + day;
        if(Integer.parseInt(date)>Integer.parseInt((DateUtil.getCurrentDate("date")))){
            ToastUtil.showToast(activity,"请选择今天以前的时间");
        }else{
            //请求数据
            activity.getZhihuDaily(date);
            tvDateNow.setText("当前日期:" + date);
        }
    }
}
