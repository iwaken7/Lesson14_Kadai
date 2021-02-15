package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsLikeServlet
 */
@WebServlet("/reports/like")
public class ReportsLikeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsLikeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // いいねのレポートIDからデータを取り出しいいねのカウントを前の数字から＋１する
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        Integer like_count = r.getLike_count();
        like_count++;
        r.setLike_count(like_count);

        // Likeのインスタンスを生成
        Like l = new Like();

        // レポートIDにログインした従業員IDといいねした時間をセット
        l.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
        l.setReport(r);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.setCreated_at(currentTime);
        l.setUpdated_at(currentTime);

        // データベースに保存
        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();

        // 日報一覧に遷移した後「いいねしました」のフラッシュメッセージを表示
        request.getSession().setAttribute("flush", "いいねしました");
        response.sendRedirect(request.getContextPath() + "/reports/index");

    }

}
