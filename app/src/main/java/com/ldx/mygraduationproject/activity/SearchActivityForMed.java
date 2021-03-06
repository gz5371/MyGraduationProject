package com.ldx.mygraduationproject.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ldx.mygraduationproject.R;
import com.ldx.mygraduationproject.adapter.AdapterArticle;
import com.ldx.mygraduationproject.adapter.AdapterMedicineUse;
import com.ldx.mygraduationproject.bean.Article;
import com.ldx.mygraduationproject.bean.Medicine;
import com.ldx.mygraduationproject.constant.AppConfig;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by freeFreAme on 2019/4/17.
 */

public class SearchActivityForMed extends BaseActivity {

    @BindView(R.id.search_ed)
    EditText searchEd;
    @BindView(R.id.search_tx)
    TextView searchTx;
    @BindView(R.id.flow_layout)
    TagFlowLayout flowLayout;
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    @BindView(R.id.search_rv)
    RecyclerView searchRv;

    private LayoutInflater mInflater = null;
    private ArrayList<String> list = new ArrayList<>();
    private AdapterMedicineUse adapterMedicine = null ;
    private Handler getGetMedsHandlerByKeyWord;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        list.add("清火");
        list.add("感冒");
        list.add("发烧");
        list.add("胃痛");
        list.add("999");
        list.add("糖浆");
        list.add("维生素C");
        list.add("颗粒");


    }

    @Override
    protected void initView() {
        mInflater = getLayoutInflater();
        flowLayout.setAdapter(new TagAdapter(list) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_flow,
                        flowLayout, false);
                tv.setText(list.get(position));
                return tv;
            }
        });
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.d("00",position+ "**");
                searchEd.setText(list.get(position));
                return false;
            }
        });

        searchRv.setLayoutManager(new GridLayoutManager(SearchActivityForMed.this, 2));
        adapterMedicine = new AdapterMedicineUse(SearchActivityForMed.this);
        searchRv.setAdapter(adapterMedicine);
    }

    @OnClick(R.id.search_tx)
    public void onViewClicked() {
        getData();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.titleBar(searchToolbar).init();
    }

    @SuppressLint("HandlerLeak")
    private void getData(){
        String key = searchEd.getText().toString().trim();
        if (key.isEmpty()){
            Toast.makeText(SearchActivityForMed.this,"输入为空",Toast.LENGTH_LONG).show();
            return;
        }else{
            getMedFromNetByKeyWord(key);
        }
        getGetMedsHandlerByKeyWord = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Medicine> medicines=(ArrayList) msg.obj;
                if (medicines.size()==0) {
                    Toast.makeText(SearchActivityForMed.this,"当前字段无数据",Toast.LENGTH_LONG).show();
                }else{
                    adapterMedicine.refreshData(medicines);}
            }
        };




    }
    public void getMedFromNetByKeyWord(String keyword) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("keyword", keyword);
        final Request request = new Request.Builder()
                .url(AppConfig.FIND_BY_KEYWORD)
                .post(builder.build())
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseStr = response.body().string();
                List<Medicine> medicines = new ArrayList<>();
                medicines = com.alibaba.fastjson.JSONArray.parseArray(responseStr, Medicine.class);
                Message msg = getGetMedsHandlerByKeyWord.obtainMessage();
                msg.obj = medicines;
                getGetMedsHandlerByKeyWord.sendMessage(msg);

            }
        });
    }

}
