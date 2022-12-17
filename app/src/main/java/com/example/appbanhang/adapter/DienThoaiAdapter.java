package com.example.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.appbanhang.Interface.ItemClickListen;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.ChiTietActivity;
import com.example.appbanhang.model.SanPhamMoi;

import java.text.DecimalFormat;
import java.util.List;

//các sp trong macbook
public class DienThoaiAdapter extends RecyclerView.Adapter<DienThoaiAdapter.MyViewHoder> {
    Context context; //truyền lên màn
    List<SanPhamMoi> array; //tạo list
    public DienThoaiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    @NonNull
    @Override
    public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dienthoai, parent, false); //gán view
        return new MyViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
        SanPhamMoi sanPham = array.get(position);
        holder.tensp.setText(sanPham.getTensp()); //lấy tên
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.giasp.setText("Giá:  "+decimalFormat.format(Double.parseDouble(sanPham.getGiasp()))+ "Đ"); //lấy giá
        holder.mota.setText(sanPham.getMota()); // lấy mota
        Glide.with(context).load(sanPham.getHinhanh()).into(holder.hinhanh); //lấy hình ảnh
        holder.setItemClickListen(new ItemClickListen() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    //click
                    Intent intent =  new Intent(context, ChiTietActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);  //chạy
                }
            }
        });
    }


    public class MyViewHoder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp, giasp, mota;
        ImageView hinhanh;
        private ItemClickListen itemClickListen;
        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemdt_ten);
            giasp = itemView.findViewById(R.id.itemdt_gia);
            mota = itemView.findViewById(R.id.itemdt_mota);
            hinhanh = itemView.findViewById(R.id.itemdt_image);
            itemView.setOnClickListener(this);
        }
        public void setItemClickListen(ItemClickListen itemClickListen) {
            this.itemClickListen = itemClickListen;
        }
        @Override
        public void onClick(View view) {
            itemClickListen.onClick(view, getAdapterPosition(), false);
        }
    }
}
