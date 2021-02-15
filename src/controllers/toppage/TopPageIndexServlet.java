package controllers.toppage;

import java.io.IOException;
import java.util.List;

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
 * Servlet implementation class TopPageIndexServlet
 */
@WebServlet("/index.html")
public class TopPageIndexServlet extends HttpServlet {
        private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopPageIndexServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // 従業員クラスからログインした従業員IDを取得
        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        // Reportテーブル
        // getMyAllReports("SELECT r FROM Report AS r WHERE r.employee = :employee ORDER BY r.id DESC")
        // ログインした従業員IDと日報の従業員IDが一致した日報を降順で取得
        List<Report> reports = em.createNamedQuery("getMyAllReports", Report.class)
                                   .setParameter("employee", login_employee)
                                   .setFirstResult(15 * (page - 1))
                                   .setMaxResults(15)
                                   .getResultList();

        // Reportテーブル
        // long型を使って上と同じ条件で取得した数を表示する処理
        long reports_count = (long)em.createNamedQuery("getMyReportsCount", Long.class)
                                         .setParameter("employee", login_employee)
                                         .getSingleResult();

        // Followテーブル
        // getMyFollowCount("SELECT COUNT(f) FROM Follow AS f WHERE f.follow = :employee")
        // ログインした従業員IDとフォローしたIDが一致したものを件数で取得
        long follows_count = (long)em.createNamedQuery("getMyFollowCount", Long.class)
                                         .setParameter("employee", login_employee)
                                         .getSingleResult();

        // Followテーブル
        // getMyFollowerCount("SELECT COUNT(f) FROM Follow AS f WHERE f.follower = :employee")
        // ログインした従業員IDとフォローされたIDが一致したものを件数で取得
        long followers_count = (long)em.createNamedQuery("getMyFollowerCount", Long.class)
                                           .setParameter("employee", login_employee)
                                           .getSingleResult();

        em.close();

        // ("/WEB-INF/views/topPage/index.jsp")に遷移した時値を渡すためにリクエストスコープにセットする
        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("follows_count", follows_count);
        request.setAttribute("followers_count", followers_count);
        request.setAttribute("page", page);

        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        // 〇〇さんの日報一覧に遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/topPage/index.jsp");
        rd.forward(request, response);
    }

}