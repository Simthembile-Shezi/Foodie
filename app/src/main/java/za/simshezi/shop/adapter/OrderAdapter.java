package za.simshezi.shop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.simshezi.shop.R;
import za.simshezi.shop.api.JavaAPI;
import za.simshezi.shop.model.OrderModel;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderModel> list;
    private AdapterClickListener listener;

    public OrderAdapter(List<OrderModel> list, AdapterClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_recycle_view, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        OrderModel model = list.get(position);
        holder.setupOrder(model);
        holder.itemView.setOnClickListener((view) ->listener.onClick(list.get(position)));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private OrderModel model;
        private TextView tvName, tvPrice, tvDate, tvStatus, tvItems;
        private ImageView imgShop;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvOrderShopName);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvItems = itemView.findViewById(R.id.tvOrderItemCount);
        }

        public void setupOrder(OrderModel model) {
            this.model = model;
            tvName.setText(String.format("%s", model.getCustomer()));
            tvStatus.setText(String.format("%s", model.getStatus()));
            tvItems.setText(String.format("%s Items", model.getItems()));
            tvDate.setText(String.format("%s", JavaAPI.getTime(model.getTime())));
            tvPrice.setText(String.format("R %s", JavaAPI.formatDouble(model.getPrice())));
        }
    }
}
