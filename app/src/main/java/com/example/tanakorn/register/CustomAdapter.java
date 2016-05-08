package com.example.tanakorn.register;

/**
 * Created by tanakorn on 5/5/2016 AD.
 */
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private String session;
    private List<User> mUser;
    private Context mContext;



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public LinearLayout layout;
        public ViewHolder(View view) {
            super(view);
            layout = (LinearLayout) view.findViewById(R.id.layout);
            mName = (TextView) view.findViewById(R.id.name);
        }
    }

    public CustomAdapter(Context context, List<User> dataset,String session) {
        mUser = dataset;
        mContext = context;
        this.session = session;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_view_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final User player = mUser.get(position);

        viewHolder.mName.setText(player.getName());
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,chat.class);
                intent.putExtra("session",session);
                intent.putExtra("user",player.getName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }
}