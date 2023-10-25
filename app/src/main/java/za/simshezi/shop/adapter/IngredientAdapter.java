package za.simshezi.shop.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import za.simshezi.shop.R;
import za.simshezi.shop.api.JavaAPI;
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private static ProductModel product;
    private static AdapterClickListener onClickListener;
    private static int position;

    public IngredientAdapter(ProductModel product, AdapterClickListener onClickListener) {
        IngredientAdapter.product = product;
        IngredientAdapter.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_recycler_view, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientViewHolder holder, @SuppressLint("RecyclerView") int position) {
        IngredientAdapter.position = position;
        IngredientModel ingredient = product.getIngredients().get(position);
        holder.setIngredient(ingredient);
    }

    @Override
    public int getItemCount() {
        return product.getIngredients().size();
    }

    public void add(IngredientModel ingredient) {
        product.getIngredients().add(ingredient);
        notifyItemInserted(product.getIngredients().size() - 1);
    }

    public void remove(int position) {
        product.getIngredients().remove(position);
        notifyItemRemoved(position);
    }

    public void move(int fromPosition, int toPosition) {
        IngredientModel ingredient = product.getIngredients().remove(fromPosition);
        product.getIngredients().add(toPosition, ingredient);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void edit(int position, IngredientModel ingredient) {
        product.getIngredients().add(position, ingredient);
        notifyItemChanged(position);
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private IngredientModel ingredient;
        private TextView tvName, tvPrice, tvCount;
        private ImageButton btnRemove, btnAdd;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvPrice = itemView.findViewById(R.id.tvIngredientPrice);
            tvCount = itemView.findViewById(R.id.tvIngredientCount);
            btnRemove = itemView.findViewById(R.id.btnRemoveIngredient);
            btnAdd = itemView.findViewById(R.id.btnAddIngredient);
        }

        public void setIngredient(IngredientModel ingredient) {
            this.ingredient = ingredient;
            tvName.setText(ingredient.getName());
            tvPrice.setText(String.format("R %s", JavaAPI.formatDouble(ingredient.getPrice())));
            btnRemove.setOnClickListener(view -> {
                int count = ingredient.getCount();
                if (count > 0) {
                    ingredient.setCount(count - 1);
                    ingredientCount(ingredient);
                }
            });
            btnAdd.setOnClickListener(view -> {
                ingredient.setCount(ingredient.getCount() + 1);
                ingredientCount(ingredient);
            });
        }

        private void ingredientCount(IngredientModel ingredient) {
            tvCount.setText(String.format("%s", ingredient.getCount()));
            product.getIngredients().remove(ingredient);
            product.getIngredients().add(position, ingredient);
            onClickListener.onClick(product);
        }
    }
}
