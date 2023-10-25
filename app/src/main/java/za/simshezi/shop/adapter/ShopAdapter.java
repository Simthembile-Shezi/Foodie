package za.simshezi.shop.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Lambda;
import za.simshezi.shop.R;
import za.simshezi.shop.model.ProductModel;
import za.simshezi.shop.model.ShopModel;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    private List<ShopModel> list;
    private AdapterClickListener listener;

    public ShopAdapter(AdapterClickListener listener) {
        this.list = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_recycler_view, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopModel model = list.get(position);
        holder.setShop(model);
        holder.itemView.setOnClickListener((view -> listener.onClick(list.get(position))));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filter(List<ShopModel> filter) {
        list = filter;
        notifyDataSetChanged();
    }

    public void add(ShopModel model) {
        list.add(model);
        notifyItemInserted(list.size() - 1);
    }

    public void edit(ShopModel shop) {
        int position = list.indexOf(shop);
        if (position > -1) {
            list.remove(shop);
            list.add(position, shop);
            notifyItemChanged(position);
        }
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        private ShopModel model;
        private TextView tvName, tvRating;
        private ImageView imgShop;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecyclerShopName);
            tvRating = itemView.findViewById(R.id.tvRecyclerShopRating);
            imgShop = itemView.findViewById(R.id.imgRecyclerShop);
        }

        public void setShop(ShopModel model) {
            this.model = model;
            byte[] data = model.getImage();
            if (data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                imgShop.setImageBitmap(bitmap);
            } else {
                imgShop.setImageResource(R.drawable.baseline_fastfood_24);
            }
            tvName.setText(model.getName());
            tvRating.setText(String.format("%s", model.getRating()));
        }
    }
}
