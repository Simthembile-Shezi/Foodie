package za.simshezi.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import za.simshezi.shop.R;
import za.simshezi.shop.model.ProductModel;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private List<ProductModel> list;
    private View.OnClickListener onClickListener;
    private static Context context;

    public CartAdapter(Context context, List<ProductModel> list, View.OnClickListener onClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
        CartAdapter.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_cart_recycler_view, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        ProductModel model = list.get(position);
        holder.setProduct(model);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder{
        TextView tvProductName, tvProductPrice;
        RecyclerView listView;
        ImageButton button;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvCartProductName);
            tvProductPrice = itemView.findViewById(R.id.tvCartProductPrice);
            listView = itemView.findViewById(R.id.lstCartProductIngredients);
            button = itemView.findViewById(R.id.btnCartProductDelete);
        }

        public void setProduct(ProductModel model) {
            ProductIngredientAdapter adapter = new ProductIngredientAdapter(context ,model.getIngredients());
            tvProductName.setText(model.getName());
            tvProductPrice.setText(String.format("R %s", model.getPrice()));
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            listView.setAdapter(adapter);
            listView.setLayoutManager(layoutManager);
        }
    }
}
