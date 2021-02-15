package controlloers.follows;

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
 * Servlet implementation class TimelineIndexServlet
 */
@WebServlet("/follows/timeline")
public class FollowsTimelineServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowsTimelineServlet() {
        super();
        // TODO Auto-generated constructor stub
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

        // getMyFollowTimeline("SELECT r FROM Report AS r, Follow AS f WHERE r.employee = f.follower AND f.follow = :employee ORDER BY r.id DESC")
        // フォローされたIDとレポートに保存された従業員IDを一致させて
        // ログインした従業員IDとフォローした人が一致したReportクラスに保存されているデータを降順で表示する処理
        List<Report> follows = em.createNamedQuery("getMyFollowTimeline", Report.class)
                                  .setParameter("employee", login_employee)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();

        // long型を使って上と同じ条件で取得した数を表示する処理
        long follows_count = (long)em.createNamedQuery("getMyFollowTimelineCount", Long.class)
                                     .setParameter("employee", login_employee)
                                     .getSingleResult();

        // ("/WEB-INF/views/follows/timeline.jsp")に遷移した時値を渡すためにリクエストスコープにセットする
        request.setAttribute("follows", follows);
        request.setAttribute("follows_count", follows_count);
        request.setAttribute("page", page);
        em.close();

        // 日報タイムラインに遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follows/timeline.jsp");
        rd.forward(request, response);
    }

}
