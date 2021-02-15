package controllers.reports;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsShowServlet
 */
@WebServlet("/reports/show")
public class ReportsShowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsShowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        //選択された日報IDとログインしたIDを取得
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));
        Employee e = (Employee)request.getSession().getAttribute("login_employee");

        // checkRegisteredlikes("SELECT COUNT(l) FROM Like AS l WHERE l.report = :report AND l.employee = :employee")
        // いいねした日報IDと選択した日報IDが一致しいいねした従業員IDとログインしたIDが一致した件数を取得
        long likes_record = (long)em.createNamedQuery("checkRegisteredlikes", Long.class)
                                        .setParameter("report", r)
                                        .setParameter("employee", e)
                                        .getSingleResult();

        // checkRegisteredfollow("SELECT COUNT(f) FROM Follow AS f WHERE f.follow = :follow AND f.follower = :follower")
        // //フォローしたIDとフォローしたログインした従業員IDが一致しフォローされたIDとフォローされた日報から取り出した従業員IDが一致した件数を取得
        long follows_record = (long)em.createNamedQuery("checkRegisteredfollow", Long.class)
                                        .setParameter("follow", e)
                                        .setParameter("follower", r.getEmployee())
                                        .getSingleResult();


        em.close();

        // ("/WEB-INF/views/reports/show.jsp")に遷移した時値を渡すためにリクエストスコープにセットする
        request.setAttribute("report", r);
        request.setAttribute("likes_record", likes_record);
        request.setAttribute("follows_record", follows_record);
        request.setAttribute("_token", request.getSession().getId());
        request.getSession().setAttribute("report_id", r.getId());

        // 日報詳細に遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/show.jsp");
        rd.forward(request, response);
    }

}
