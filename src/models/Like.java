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

@Table(name = "likes")
@NamedQueries({
    @NamedQuery(
            name = "getYourselfLikes", //いいねした日報IDと選択した日報IDが一致した時にいいねIDを降順で取得
            query = "SELECT l FROM Like AS l WHERE l.report = :report ORDER BY l.id DESC"
    ),
    @NamedQuery(
            name = "getYourselfLikesCount", //いいねした日報IDと選択した日報IDが一致した時にいいねIDを件数を取得
            query = "SELECT COUNT(l) FROM Like AS l WHERE l.report = :report"
    ),
    @NamedQuery(
            name = "checkRegisteredlikes", //いいねした日報IDと選択した日報IDが一致しいいねした従業員IDとログインしたIDが一致した件数を取得
            query = "SELECT COUNT(l) FROM Like AS l WHERE l.report = :report AND l.employee = :employee"
    )
})

@Entity
public class Like {
    @Id
    @Column(name = "id") //リソース内での連番
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id" , nullable = false) //いいねした従業員ID
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "report_id" , nullable = false) //いいねした日報ID
    private Report report;

    @Column(name = "created_at" , nullable = false) //いいねした日時
    private Timestamp created_at;

    @Column(name = "updated_at" , nullable = false) //いいねの更新日時
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
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
