package org.cnsl.software.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder> {

    private ArrayList<String> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView textView;

        ViewHolder(View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.text1);
        }
    }

    TextAdapter(ArrayList<String> list) {
        mData = list;
    }

    @Override
    public TextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        TextAdapter.ViewHolder vh = new TextAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(TextAdapter.ViewHolder holder, int pos){
        String text = mData.get(pos);
        holder.textView.setText(text);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
