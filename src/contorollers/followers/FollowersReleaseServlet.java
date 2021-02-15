package contorollers.followers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowersReleaseServlet
 */
@WebServlet("/followers/release")
public class FollowersReleaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowersReleaseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // Followクラスから選択したフォローしたIDを取得
        Follow f = em.find(Follow.class, Integer.parseInt(request.getParameter("id")));

        // データベースから選択されたフォローIDを削除
        em.getTransaction().begin();
        em.remove(f);
        em.getTransaction().commit();
        em.close();

        // フォロワー一覧に遷移し「フォローを解除しました」をフラッシュメッセージにて表示
        request.getSession().setAttribute("flush", "フォローを解除しました");
        response.sendRedirect(request.getContextPath() + "/followers/index");
    }

}
