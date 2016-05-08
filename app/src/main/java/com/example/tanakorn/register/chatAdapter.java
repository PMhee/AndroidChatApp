package com.example.tanakorn.register;

/**
 * Created by tanakorn on 5/5/2016 AD.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ViewHolder> {

    private List<Message> messages;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mMessage;
        public ViewHolder(View view) {
            super(view);
            mMessage = (TextView) view.findViewById(R.id.message);
        }
    }

    public chatAdapter(Context context, List<Message> dataset) {
        messages = dataset;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_message_row, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = messages.get(position);
        viewHolder.mMessage.setText(message.getMessage());
        if(message.getForm().equals("5530237221")){
            viewHolder.mMessage.setGravity(Gravity.RIGHT);
        }else{
            viewHolder.mMessage.setGravity(Gravity.LEFT);
        }

    }

    @Override
    public int getItemCount() {
        Log.d("TESTWTF",messages.size() +"");
        return messages.size();
    }
}