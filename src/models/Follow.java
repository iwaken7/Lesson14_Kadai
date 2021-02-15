package models;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "follows")
@NamedQueries({
    @NamedQuery(
            name = "getMyFollow", //ログインした従業員IDとフォローしたIDが一致したものを降順で取得
            query = "SELECT f FROM Follow AS f WHERE f.follow = :employee ORDER BY f.id DESC"
    ),
    @NamedQuery(
            name = "getMyFollowCount", //ログインした従業員IDとフォローしたIDが一致したものを件数で取得
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follow = :employee"
    ),
    @NamedQuery(
            name = "getMyFollower", //ログインした従業員IDとフォローされたIDが一致したものを降順で取得
            query = "SELECT f FROM Follow AS f WHERE f.follower = :employee ORDER BY f.id DESC"
    ),
    @NamedQuery(
            name = "getMyFollowerCount", //ログインした従業員IDとフォローされたIDが一致したものを件数で取得
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follower = :employee"
    ),
    @NamedQuery(
            name = "getMyFollowTimeline", //フォローされたIDと日報の従業員IDを一致させてログインしたIDとフォローしたIDが一致したものを降順で取得
            query = "SELECT r FROM Report AS r, Follow AS f WHERE r.employee = f.follower AND f.follow = :employee ORDER BY r.id DESC"
    ),
    @NamedQuery(
            name = "getMyFollowTimelineCount",  //フォローされたIDと日報の従業員IDを一致させてログインしたIDとフォローしたIDが一致したものを件数で取得
            query = "SELECT COUNT(r) FROM Report AS r, Follow AS f WHERE r.employee = f.follower AND f.follow = :employee"
    ),
    @NamedQuery(
            name = "checkRegisteredfollow", //フォローしたIDとフォローしたログインした従業員IDが一致しフォローされたIDとフォローされた日報から取り出した従業員IDが一致した件数を取得
            query = "SELECT COUNT(f) FROM Follow AS f WHERE f.follow = :follow AND f.follower = :follower"
    )
})

@Entity
public class Follow {
    @Id
    @Column(name = "id") //リソース内の連番
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follow_id" , nullable = false) //フォローしたID
    private Employee follow;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false) //フォローされたID
    private Employee follower;

    // *今回は作成日時と更新日時を表示することはないがcreated_atとupdate_atはクラスを作成するとき追加が必要なのでセット
    @Column(name = "created_at" , nullable = false) //作成日時
    private Timestamp created_at;

    @Column(name = "updated_at" , nullable = false) //更新日時
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollow() {
        return follow;
    }

    public void setFollow(Employee follow) {
        this.follow = follow;
    }

    public Employee getFollower() {
        return follower;
    }

    public void setFollower(Employee follower) {
        this.follower = follower;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
       this.updated_at = updated_at;
    }
}
