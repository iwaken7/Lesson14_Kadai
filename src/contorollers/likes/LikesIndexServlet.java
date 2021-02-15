package contorollers.likes;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class LikesIndexServlet
 */
@WebServlet("/likes/index")
public class LikesIndexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikesIndexServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        // 選択したレポートIDから値を受け取る
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        // getYourselfLikes("SELECT l FROM Like AS l WHERE l.report = :report ORDER BY l.id DESC")
        // いいねした日報IDと選択した日報IDが一致した時にいいねIDを降順で取得
        List<Like> likes = em.createNamedQuery("getYourselfLikes", Like.class)
                                  .setParameter("report", r)
                                  .setFirstResult(15 * (page - 1))
                                  .setMaxResults(15)
                                  .getResultList();

     // long型を使って上と同じ条件で取得した数を表示する処理
        long likes_count = (long)em.createNamedQuery("getYourselfLikesCount", Long.class)
                                     .setParameter("report", r)
                                     .getSingleResult();

        em.close();

        // ("/WEB-INF/views/likes/index.jsp")に遷移した時値を渡すためにリクエストスコープにセットする
        request.setAttribute("likes", likes);
        request.setAttribute("likes_count", likes_count);
        request.setAttribute("page", page);

        // レポート毎のいいね一覧に遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/likes/index.jsp");
        rd.forward(request, response);
    }

}
