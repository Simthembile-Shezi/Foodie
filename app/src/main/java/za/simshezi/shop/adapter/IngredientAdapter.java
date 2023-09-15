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
import za.simshezi.shop.model.IngredientModel;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    public static List<IngredientModel> ingredients;
    private static View.OnClickListener onClickListener;
    private static int position;

    public IngredientAdapter(List<IngredientModel> ingredients, View.OnClickListener onClickListener) {
        IngredientAdapter.ingredients = ingredients;
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
        IngredientModel ingredient = ingredients.get(position);
        holder.setIngredient(ingredient);
        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void add(IngredientModel ingredient) {
        ingredients.add(ingredient);
        notifyItemInserted(ingredients.size() - 1);
    }

    public void remove(int position) {
        ingredients.remove(position);
        notifyItemRemoved(position);
    }

    public void move(int fromPosition, int toPosition) {
        IngredientModel ingredient = ingredients.remove(fromPosition);
        ingredients.add(toPosition, ingredient);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void edit(int position, IngredientModel ingredient) {
        ingredients.add(position, ingredient);
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
            tvPrice.setText(String.format("R %s", ingredient.getPrice()));
            btnRemove.setOnClickListener(view -> {
                int count = ingredient.getCount();
                if(count > 0){
                    ingredient.setCount(count - 1);
                    ingredientCount(ingredient);
                }
            });
            btnAdd.setOnClickListener(view -> {
                ingredient.setCount(ingredient.getCount() + 1 );
                ingredientCount(ingredient);
            });
        }

        private void ingredientCount(IngredientModel ingredient) {
            tvCount.setText(String.format("%s", ingredient.getCount()));
            ingredients.remove(ingredient);
            ingredients.add(position, ingredient);
            onClickListener.onClick(itemView);
        }
    }
}
