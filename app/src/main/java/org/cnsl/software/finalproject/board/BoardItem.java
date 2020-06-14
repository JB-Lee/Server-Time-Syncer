package org.cnsl.software.finalproject.board;

import java.util.Objects;

public class BoardItem {

    private final String writer;
    private final String host;
    private final String content;
    private final int epochSecond;

    public BoardItem(String writer, String host, String content, int epochSecond) {
        this.writer = writer;
        this.host = host;
        this.content = content;
        this.epochSecond = epochSecond;
    }

    public String getWriter() {
        return writer;
    }

    public String getHost() {
        return host;
    }

    public String getContent() {
        return content;
    }

    public int getEpochSecond() {
        return epochSecond;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardItem boardItem = (BoardItem) o;
        return epochSecond == boardItem.epochSecond &&
                Objects.equals(writer, boardItem.writer) &&
                Objects.equals(host, boardItem.host) &&
                Objects.equals(content, boardItem.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(writer, host, content, epochSecond);
    }
}
