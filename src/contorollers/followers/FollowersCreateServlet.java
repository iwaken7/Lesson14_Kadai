package contorollers.followers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowersCreateServlet
 */
@WebServlet("/followers/create")
public class FollowersCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowersCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // Employeeクラスから従業員IDを取得
        Employee e = em.find(Employee.class, Integer.parseInt(request.getParameter("id")));

        // Followのインスタンスを生成
        Follow f = new Follow();

        // フォローした時の処理
        f.setFollow((Employee)request.getSession().getAttribute("login_employee"));
        f.setFollower(e);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        f.setCreated_at(currentTime);
        f.setUpdated_at(currentTime);

        // データベースに保存
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        em.close();

        // 日報一覧に遷移した時「フォローしました」のフラッシュメッセージを表示
        request.getSession().setAttribute("flush", "フォローしました");
        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}
