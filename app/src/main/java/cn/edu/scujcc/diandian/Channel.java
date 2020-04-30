package cn.edu.scujcc.diandian;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Channel implements Serializable {
    private String id;
    private String title;//标题
    private String quality;//清晰度
    private String cover;//封面
    private String url;//播放地址

    @Override
    public String toString() {
        return "Channel{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", quality='" + quality + '\'' +
                ", cover='" + cover + '\'' +
                ", url='" + url + '\'' +
                ", comments=" +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
