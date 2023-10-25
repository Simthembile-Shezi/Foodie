package za.simshezi.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.simshezi.shop.R;
import za.simshezi.shop.api.JavaAPI;
import za.simshezi.shop.model.ProductModel;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ProductViewHolder>{
    private List<ProductModel> list;
    private static Context context;

    public OrderProductAdapter(Context context, List<ProductModel> list) {
        this.list = list;
        OrderProductAdapter.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_order_recycler_view, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.setProduct(model);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName, tvProductPrice;
        RecyclerView listView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvOrderProductName);
            tvProductPrice = itemView.findViewById(R.id.tvOrderProductPrice);
            listView = itemView.findViewById(R.id.lstOrderProductIngredients);
        }

        public void setProduct(ProductModel model) {
            ProductIngredientAdapter adapter = new ProductIngredientAdapter(context ,model.getIngredients());
            tvProductName.setText(model.getName());
            tvProductPrice.setText(String.format("R %s", JavaAPI.formatDouble(model.getPrice())));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            listView.setAdapter(adapter);
            listView.setLayoutManager(layoutManager);
        }
    }
}
