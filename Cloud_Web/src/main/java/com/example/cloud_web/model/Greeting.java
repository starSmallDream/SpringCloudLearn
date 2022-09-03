package com.example.cloud_web.model;

/**
 * @author xiaohuichao
 * @createdDate 2022/7/18 16:01
 */
public class Greeting {

    private final long id;
    private final String content;

    public Greeting(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
