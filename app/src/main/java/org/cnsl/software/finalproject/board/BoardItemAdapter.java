package org.cnsl.software.finalproject.board;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.cnsl.software.finalproject.R;
import org.cnsl.software.finalproject.utils.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BoardItemAdapter extends RecyclerView.Adapter<BoardItemAdapter.ViewHolder> {

    private List<BoardItem> boardItems;

    public BoardItemAdapter(ArrayList<BoardItem> list) {
        boardItems = list;
    }

    @NonNull
    @Override
    public BoardItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context ctx = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.board_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardItemAdapter.ViewHolder holder, int pos) {
        BoardItem item = boardItems.get(pos);
        holder.tvName.setText(item.getWriter());
        holder.tvHostName.setText(String.format("#%s", item.getHost()));
        holder.tvTime.setText(
                TimeFormatter.formatTime(item.getEpochSecond())
        );
        holder.tvContent.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return boardItems.size();
    }

    public void appendItem(BoardItem item) {
        boardItems.add(0, item);
        notifyItemInserted(0);
    }

    public void setNewItems(List<BoardItem> list) {
        final BoardItemDiffCallback diffCallback = new BoardItemDiffCallback(this.boardItems, list);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.boardItems.clear();
        this.boardItems.addAll(list);

        diffResult.dispatchUpdatesTo(BoardItemAdapter.this);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_board_writer)
        AppCompatTextView tvName;
        @BindView(R.id.tv_board_hostname)
        AppCompatTextView tvHostName;
        @BindView(R.id.tv_board_time)
        AppCompatTextView tvTime;
        @BindView(R.id.tv_board_content)
        AppCompatTextView tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
