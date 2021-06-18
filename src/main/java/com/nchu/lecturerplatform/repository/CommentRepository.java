package com.nchu.lecturerplatform.repository;

import com.nchu.lecturerplatform.domain.Comment;
import com.nchu.lecturerplatform.domain.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Long>  {

    List<Comment> findAllByCourseAndParentId(Course course,Long parentId);
    List<Comment> findAllByCourseAndParentIdAndTitleContaining(Course course,Long parentId,String title);
    List<Comment> findAllByCourseAndParentIdAndUsernameAndTitleContaining(Course course,Long parentId,String username,String title);
    List<Comment> findAllByCourseAndParentIdAndVideoSite(Course course,Long parentId,String videoSite);
    List<Comment> findAllByTitleContaining(String title);
    @Modifying
    @Query(value="update Comment o set o.viewState=1 where o.parentId=:id and o.replyUsername=:name and o.parentId<>0") //基于HQL
    int updateViewState(@Param("id") Long parentId, @Param("name") String replyName);
    Long countByReplyUsernameAndViewState(String username,int viewState);
    List<Comment> findByReplyUsernameAndViewState(String replyUsername,int viewState);
    @Modifying
    @Query(value="update Comment o set o.imageSite=:imageSite where o.username=:username") //基于HQL
    int updateImageSiteByUsername(@Param("username") String username, @Param("imageSite") String imageSite);

}
