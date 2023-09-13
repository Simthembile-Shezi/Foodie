package za.simshezi.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.simshezi.shop.R;
import za.simshezi.shop.model.IngredientModel;
import za.simshezi.shop.model.ProductModel;

public class ProductIngredientAdapter extends RecyclerView.Adapter<ProductIngredientAdapter.ProductIngredientViewHolder> {
    List<IngredientModel> list;
    private LayoutInflater inflater;
    public ProductIngredientAdapter(Context context, List<IngredientModel> ingredients) {
        list = ingredients;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ProductIngredientAdapter.ProductIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ProductIngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductIngredientAdapter.ProductIngredientViewHolder holder, int position) {
        IngredientModel model = list.get(position);
        holder.setIngredient(model);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductIngredientViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvItemCount;
        public ProductIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvItemCount = itemView.findViewById(R.id.tvItemCount);
        }

        public void setIngredient(IngredientModel model) {
            if(model.getCount() > 0){
                tvName.setText(model.getName());
                tvItemCount.setText(String.format("%d", model.getCount()));
            }
        }
    }
}
