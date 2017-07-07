package com.fter.sopt.fter.search;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fter.sopt.fter.R;
import com.fter.sopt.fter.application.ApplicationController;
import com.fter.sopt.fter.network.NetworkService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchRecyclerAdapter SearchRecyclerAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;

    NetworkService service;
    ApplicationController applicationController;
    Context context;
    TextView searchContentView;
    TextView noResult;
    ImageView searchBtn;
    Spinner spinner;
    String search_content;
    String part;
    ArrayList<SearchData> itemDatas;
    ArrayList<SearchData> dataSet;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String user_nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();
        user_nick = pref.getString("USERNICK",null);

        service = ApplicationController.getInstance().getNetworkService();

        itemDatas = new ArrayList<SearchData>();
        dataSet = new ArrayList<SearchData>();

        recyclerView = (RecyclerView) findViewById(R.id.serchRecyclerView);

        context = this;
        SearchRecyclerAdapter = new SearchRecyclerAdapter(itemDatas, recyclerView, linearLayoutManager, context, SearchActivity.this,user_nick);
        recyclerView.setAdapter(SearchRecyclerAdapter);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        searchBtn = (ImageView)findViewById(R.id.searchBtn);
        searchContentView = (TextView)findViewById(R.id.write_keyword);
        spinner = (Spinner)this.findViewById(R.id.search_part);

        noResult = (TextView)findViewById(R.id.noResult);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(i){
                    case 0:
                        part = "bm";
                        break;
                    case 1:
                        part = "develop";
                        break;
                    case 2:
                        part = "design";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        noResult.setVisibility(View.GONE);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search_content = searchContentView.getText().toString();
                if(search_content.length() == 0){
                    Toast.makeText(getApplicationContext(),"내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    search_content = null;
                }




                Call<SearchResult> getSearchPost= service.getSearchPost(new SearchInfo(search_content,part));
                getSearchPost.enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        if(response.isSuccessful()){
                            if(response.body().message.equals("ok")){

                                if(response.body().result.size() == 0){
                                    noResult.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                else{
                                    recyclerView.setVisibility(View.VISIBLE);
                                    noResult.setVisibility(View.GONE);
                                    itemDatas = response.body().result;
                                    SearchRecyclerAdapter.setAdapter(itemDatas);

                                    Toast.makeText(getApplicationContext(), "불러오기 성공!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "불러오기 실패......", Toast.LENGTH_SHORT).show();
                    }

                });



            }
        });


    }
//    protected void onRestart() {
//        super.onRestart();
//
//        //first clear the recycler view so items are not populated twice
//        SearchRecyclerAdapter.clear();
//
////        //then reload the data
////        PostCall doPostCall = new PostCall(); //my AsyncTask...
////        doPostCall.execute();
//    }
}