package app.kibbeh.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import app.kibbeh.R;
import app.kibbeh.model.OrderHistory;

/**
 * Created by archirayan on 19-Oct-16.
 */

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<OrderHistory> arrayOrderHistory;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, total_product, amount, status;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.tvDate_AdapterOrderHistory);
            total_product = (TextView) view.findViewById(R.id.tvTotalProduct_AdapterOrderHistory);
            amount = (TextView) view.findViewById(R.id.tvAmount_AdapterOrderHistory);
            status = (TextView) view.findViewById(R.id.tvStatus_AdapterOrderHistory);
        }
    }
    public OrderHistoryAdapter(Context activity, ArrayList<OrderHistory> arrayOrder) {
        this.context = activity;
        this.arrayOrderHistory = arrayOrder;
    }

    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_order_history, parent, false);
        return new OrderHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderHistoryAdapter.MyViewHolder holder, int position) {
        OrderHistory orderHistory = arrayOrderHistory.get(position);
        holder.date.setText(orderHistory.getDate());
        holder.total_product.setText(orderHistory.getTotal_products());
        holder.amount.setText(orderHistory.getTotal_amount());
        holder.status.setText(orderHistory.getStatus());
    }

    @Override
    public int getItemCount() {
        return arrayOrderHistory.size();
    }

}