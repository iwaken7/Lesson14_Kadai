package contorollers.followers;

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
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowersIndexServlet
 */
@WebServlet("/followers/index")
public class FollowersIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowersIndexServlet() {
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

        // getMyFollower("SELECT f FROM Follow AS f WHERE f.follower = :employee ORDER BY f.id DESC")
        // ログインした従業員IDとフォローされたIDが一致したものを降順で取得
        List<Follow> followers = em.createNamedQuery("getMyFollower", Follow.class)
                                    .setParameter("employee", login_employee)
                                    .setFirstResult(15 * (page - 1))
                                    .setMaxResults(15)
                                    .getResultList();


        // long型を使って上と同じ条件で取得した数を表示する処理
        long followers_count = (long)em.createNamedQuery("getMyFollowerCount", Long.class)
                                     .setParameter("employee", login_employee)
                                     .getSingleResult();

        em.close();

        // ("/WEB-INF/views/followers/index.jsp")に遷移した時値を渡すためにリクエストスコープにセットする
        request.setAttribute("followers", followers);
        request.setAttribute("followers_count", followers_count);
        request.setAttribute("page", page);

        // フォロワー一覧に遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/followers/index.jsp");
        rd.forward(request, response);
    }

}