package org.cnsl.software.finalproject.board;

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
}
