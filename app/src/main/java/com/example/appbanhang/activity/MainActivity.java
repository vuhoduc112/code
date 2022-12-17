package com.example.appbanhang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.adapter.LoaiSpAdapter;
import com.example.appbanhang.adapter.SanphammoiAdapter;
import com.example.appbanhang.model.LoaiSp;
import com.example.appbanhang.model.LoaiSpModel;
import com.example.appbanhang.model.SanPhamMoi;
import com.example.appbanhang.model.SanPhamMoiModel;
import com.example.appbanhang.retrofit.AipBanHang;
import com.example.appbanhang.retrofit.RetrofitClient;
import com.example.appbanhang.util.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AipBanHang aipBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanphammoiAdapter spAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aipBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(AipBanHang.class);
        Anhxa();
        ActionBar(); // hàm bắt sự kiện toolbar
        if(isConnect(this)){
            Toast.makeText(this, "kết nối internet thành công", Toast.LENGTH_SHORT).show();
            ActionViewFlipper(); //bắt sự kiện cho viewflipper cho việc chạy quảng cáo
            //get các file kết nối với php
            getLoaiSanPham();
            getSpMoi();
            getEventClick(); //hàm click vào các sự kiện trong toolbar như trang chủ, liên hệ, thông tin, ...
        }
        else {
            Toast.makeText(this, "không có internet, vui lòng kết nối", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(), LapTopActivity.class);
                        startActivity(laptop);
                        break;
                }
            }
        });
    }

    //sp ở trang chủ
    private void getSpMoi() {
        compositeDisposable.add(aipBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    mangSpMoi = sanPhamMoiModel.getResult();
                    spAdapter = new SanphammoiAdapter(getApplicationContext(), mangSpMoi);
                    recyclerViewManHinhChinh.setAdapter(spAdapter);
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Không kết nối được với server"+throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
        ));

    }

    // các mục ở navigation
    private void getLoaiSanPham() {
        compositeDisposable.add(aipBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaiSpModel -> {
                    if (loaiSpModel.isSuccess()){
                        mangloaisp = loaiSpModel.getResult();
                        //khởi tạo adapter
                        loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                        listViewManHinhChinh.setAdapter(loaiSpAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>(); //tạo mảng chứa đường dẫn các tấm hình
        mangquangcao.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdaPiDh8OX4wZDz1c6FW_Ypv7VmmeAgaPPhlxe-7IVtWiBwYI6bX5eIud4aHF_r3j-OhQ&usqp=CAU");  // đưa dữ liệu vào
        mangquangcao.add("https://phucanhcdn.com/media/news/7724_capture.PNG");
        mangquangcao.add("https://thietkehaithanh.com/wp-content/uploads/2019/01/thietkehaithanh-banner-laptop-800x300.png");
        mangquangcao.add("https://www.ankhang.vn/media/news/1211_Banner-hp-tang-tai-nghe.jpg");
        //tạo vòng for để chạy hình nhanh hơn
        for (int i = 0; i<mangquangcao.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext()); // khởi tạo imageView
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView); //set lại kích cỡ imageview
            imageView.setScaleType(ImageView.ScaleType.FIT_XY); //căn vừa với viewlippep mà không bị cắt đi
            viewFlipper.addView(imageView);//add vào viewflipper
        }
        viewFlipper.setFlipInterval(3000); //  chuyển đổi trong 3s
        viewFlipper.setAutoStart(true); //tự động chạy
        // sét animation
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setAnimation(slide_in); //set animesion vào viewlipper
        viewFlipper.setAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);// hàm hỗ trợ toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //nút home của toolbar
        toolbar.setNavigationIcon(android.R.drawable.star_on);// truyền hình ảnh cho nút home
        //bắt sự kiện khi click vào sẽ mở ra thanh menu
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START); //ĐƯA màn hình ra giữa
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewManHinhChinh = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(layoutManager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);
        //khởi tạo list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
    }

    //hàm kiểm tra xem có kết nối internet hay không
    private boolean isConnect(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); //Nho them quyen vao khong bi loi
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi !=null && wifi.isConnected()) ||(mobile !=null &&mobile.isConnected())){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}