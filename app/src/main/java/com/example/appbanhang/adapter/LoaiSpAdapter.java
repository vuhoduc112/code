package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.LoaiSp;

import java.util.List;

//các mục trong navigation
public class LoaiSpAdapter extends BaseAdapter {
    List<LoaiSp> array; //muốn truyền vào một list như nào ?
    Context context; //truyền vào màn hình để hệ thống biết vẽ trên màn hình nào

    public LoaiSpAdapter(Context context, List<LoaiSp> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size(); //lấy hết dữ liệu trong mảng
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //load dữ liệu nhanh hơn
    public class ViewHolder{
        TextView texsanpham;
        ImageView imghinhanh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        //khi view rỗng sẽ nhảy vào if
        if(view==null){
            viewHolder = new ViewHolder(); //khai báo lại, để dễ sử dụng thuộc tính class
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); //hàm này giúp ta get layout ra
            view = layoutInflater.inflate(R.layout.item_sanpham,null); //gán view
            viewHolder.texsanpham = view.findViewById(R.id.item_tensp); //gán id
            viewHolder.imghinhanh = view.findViewById(R.id.item_image); //gán id
            view.setTag(viewHolder);
            Glide.with(context).load(array.get(i).getHinhanh()).into(viewHolder.imghinhanh); //gán vào viewHolder
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.texsanpham.setText(array.get(i).getTensanpham()); //lấy dữ liệu tên sản phẩm từ database để hiện lên màn
        Glide.with(context).load(array.get(i).getHinhanh()).into(viewHolder.imghinhanh); //hiện lên màn
        return view;
    }
}
