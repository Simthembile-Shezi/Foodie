package za.simshezi.shop.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.simshezi.shop.R;
import za.simshezi.shop.api.ImagesAPI;
import za.simshezi.shop.model.ProductModel;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public ProductModel product;
    private List<ProductModel> products;
    private View.OnClickListener listener;


    public ProductAdapter(List<ProductModel> products, View.OnClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_recycler_view, parent, false);

        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductModel product = products.get(position);
        holder.setProduct(product);
        holder.itemView.setOnClickListener(view -> {
            this.product = products.get(position);
            listener.onClick(view);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void add(ProductModel product) {
        products.add(product);
        notifyItemInserted(products.size() - 1);
    }

    public void remove(int position) {
        products.remove(position);
        notifyItemRemoved(position);
    }

    public void move(int fromPosition, int toPosition) {
        ProductModel product = products.get(fromPosition);
        products.add(toPosition, product);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void edit(int position, ProductModel product) {
        products.add(position, product);
        notifyItemChanged(position);
    }

    public ProductModel get(int position){
        return products.get(position);
    }
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        public ProductModel product;
        public TextView tvName, tvDescription, tvPrice;
        public ImageView imgProduct;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecyclerProductName);
            tvDescription = itemView.findViewById(R.id.tvRecyclerProductDescription);
            tvPrice = itemView.findViewById(R.id.tvRecyclerProductPrice);
            imgProduct = itemView.findViewById(R.id.imgRecyclerProduct);
        }

        public void setProduct(ProductModel product) {
            this.product = product;
            byte[] data = product.getImage();
            if(data != null){
                imgProduct.setImageBitmap(ImagesAPI.convertToBitmap(data));
            }
            imgProduct.setImageResource(R.drawable.image_5);
            tvName.setText(product.getName());
            tvDescription.setText(product.getDescription());
            tvPrice.setText(String.format("R %s", product.getPrice()));
        }
    }
}
