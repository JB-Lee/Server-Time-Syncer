package org.cnsl.software.finalproject.board;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class BoardItemDiffCallback extends DiffUtil.Callback {
    private final List<BoardItem> oldBoardItems;
    private final List<BoardItem> newBoardItems;

    public BoardItemDiffCallback(List<BoardItem> oldBoardItems, List<BoardItem> newBoardItems) {
        this.oldBoardItems = oldBoardItems;
        this.newBoardItems = newBoardItems;
    }

    @Override
    public int getOldListSize() {
        return oldBoardItems.size();
    }

    @Override
    public int getNewListSize() {
        return newBoardItems.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBoardItems.get(oldItemPosition).hashCode() == newBoardItems.get(newItemPosition).hashCode();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final BoardItem oldItem = oldBoardItems.get(oldItemPosition);
        final BoardItem newItem = newBoardItems.get(newItemPosition);

        return oldItem.getEpochSecond() == newItem.getEpochSecond();
    }
}
