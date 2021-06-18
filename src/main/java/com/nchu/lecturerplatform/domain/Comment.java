package com.nchu.lecturerplatform.domain;

import lombok.Data;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Comment implements Comparable<Comment>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
    private Date createTime;
    private String username;
    private String nickname;
    private String imageSite;
    //private Long courseId;
    @Column(name = "parent_id", nullable = false)
    private Long parentId = 0L;//父评论，如果不设置，默认为0
    /*@Column(name = "top_id")
    private Long topId;//顶级问答id，即问题id*/
    private String replyUsername;
    private String replyNickname;
    private int type;//讲师评论类型为1，学生评论类型为0，默认为0；
    private int viewState;//查阅状态，0表示未查阅，1表示已查阅，默认为0
    private String videoSite;//确定某视频下的标识

    /**
     * 评论对顶级问题 一对一
     */
    @JoinColumn(name = "top_id",referencedColumnName = "id")
    @OneToOne(targetEntity = Comment.class,cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    private Comment topComment;

    @ManyToOne(targetEntity = Course.class)
    @JoinColumn(name = "course_id",referencedColumnName = "id")
    private Course course;

    @OneToMany(targetEntity = Comment.class,cascade = CascadeType.REMOVE)//设置级联删除
    @JoinColumn(name = "parent_id", referencedColumnName = "id")//parent_id即回复的评论id
    @OrderBy("id ASC")
    private List<Comment> commentList;

    public Comment(String title, String content, String username, String nickname, String imageSite, Course course, String replyUsername,String replyNickname, int type) {
        this.title = title;
        this.content = content;
        this.username = username;
        this.nickname = nickname;
        this.imageSite = imageSite;
        this.course = course;
        this.replyUsername=replyUsername;
        this.replyNickname = replyNickname;
        this.type = type;
    }

    public Comment() {

    }

    @Override
    public int compareTo(Comment o) {
        //按时间排序
        return this.createTime.compareTo(o.getCreateTime());
    }
}
