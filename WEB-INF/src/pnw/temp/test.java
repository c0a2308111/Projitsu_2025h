package pnw.temp;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pnw.common.PnwDB; // PnwDBクラスをインポート

/**
 * PnwDBクラスを使ってデータベースへの接続テストを行う専用サーブレット
 */
@WebServlet("/test")
public class test extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // ★★★ あなたの環境に合わせて、接続先のデータベース名を書き換えてください ★★★
    private static final String DB_NAME = "2025h";
    // ★★★ 注意：ユーザー名とパスワードは PnwDB.java 内に直接書かれています ("id", "pass") ★★★

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title>データベース接続テスト (PnwDB使用)</title></head>");
        out.println("<body>");
        out.println("<h1>PnwDBクラスを使った接続テストだよ</h1>");

        try {
            // 1. PnwDBのインスタンス化を試みる
            // この中でDriverのロードとDB接続が行われる
            out.println("<h3>1. PnwDBのインスタンス化を試みます...</h3>");
            PnwDB db = new PnwDB(DB_NAME);
            out.println("<p style='color:blue;'>PnwDBオブジェクトの作成に成功しました。</p>");

            // 2. 実際に接続が有効か、ステートメントを取得してみる
            // PnwDBのコンストラクタは接続失敗時に例外を握りつぶすため、
            // 実際に使ってみてNullPointerExceptionが起きないか確認するのが確実
            out.println("<h3>2. 接続を使ってステートメントの取得を試みます...</h3>");
            PreparedStatement stmt = db.getStmt("SELECT 1"); // 最も簡単なSQLでテスト

            if (stmt != null) {
                out.println("<h2 style='color:green; font-weight:bold;'>データベース接続成功！</h2>");
                out.println("<p>PnwDB経由でMySQLに問題なく接続できています。</p>");
            } else {
                out.println("<h2 style='color:red; font-weight:bold;'>データベース接続失敗...</h2>");
                out.println("<p>PnwDBオブジェクトは作成できましたが、ステートメントの取得に失敗しました。<br>これは内部でエラーが発生していることを示します。</p>");
                out.println("<p>Tomcatのコンソールログに`NullPointerException`や`SQLException`が出力されていないか確認してください。</p>");
            }

        } catch (Throwable t) {
            // 3. エラーが発生した場合
            out.println("<h2 style='color:red; font-weight:bold;'>データベース接続失敗...</h2>");
            out.println("<p>PnwDBのインスタンス化、またはその利用中に予期せぬエラーが発生しました。</p>");

            // エラーの詳細情報をブラウザに表示
            out.println("<hr>");
            out.println("<h4>▼ エラー詳細 ▼</h4>");
            out.println("<pre>");
            t.printStackTrace(out);
            out.println("</pre>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}