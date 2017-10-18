package com.example.hekd.learningworld.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hekd on 2017/10/18.
 */
@Entity
public class GDVideoInfo {

    @Id(autoincrement = true)
    private Long id;
    @Property(nameInDb = "VIDEO_PATH")//路径
    private String video_path;
    @Property(nameInDb = "VIDEO_PROGRESS")//已观看进度
    private int video_progress;

    @Property(nameInDb = "VIDEO_DURATION")//总进度
    private int video_duration;

    @Generated(hash = 666086990)
    public GDVideoInfo(Long id, String video_path, int video_progress,
            int video_duration) {
        this.id = id;
        this.video_path = video_path;
        this.video_progress = video_progress;
        this.video_duration = video_duration;
    }

    @Generated(hash = 918419838)
    public GDVideoInfo() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVideo_path() {
        return this.video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public int getVideo_progress() {
        return this.video_progress;
    }

    public void setVideo_progress(int video_progress) {
        this.video_progress = video_progress;
    }

    public int getVideo_duration() {
        return this.video_duration;
    }

    public void setVideo_duration(int video_duration) {
        this.video_duration = video_duration;
    }


}
