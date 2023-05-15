package ch20.oracle.sec12;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import ch20.oracle.sec09.exam02.Board;

public class BoardExample5 {
	
	
	// Field
		private Scanner sc = new Scanner(System.in);
		private Connection conn;
		
		// Constructor
		public BoardExample5() {
			try {
				Class.forName("oracle.jdbc.OracleDriver");
				
				conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
	        			"spcsm5452",
	        			"12345"
	        			);
			}catch (Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		
		
		// Method
		public void list() {
			// 타이틀 및 컬럼명 출력
			System.out.println();
			System.out.println("[게시물 목록]");
			System.out.println("-------------------------------------------------------------");
			System.out.printf("%-6s%-12s%-16s%-40s\n", "no", "writer", "date", "title");
			System.out.println("-------------------------------------------------------------");
			System.out.printf("%-6s%-12s%-16s%-40s \n", "1", "winter", "2022.01.27", "게시판에 오신 것을 환영합니다.");
			System.out.printf("%-6s%-12s%-16s%-40s \n", "2", "winter", "2022.01.27", "올 겨울은 많이 춥습니다.");
			mainMenu();
			
			try {
				String sql ="" + "SELECT bno, btitle, bcontent, bwriter, bdate " +
						"FROM boards "+
						"ORDER BU bno DESC";
			
						PreparedStatement pstmt = conn.prepareStatement(sql);
						ResultSet rs = pstmt.executeQuery();
						while(rs.next()) {
						Board board = new Board();
						board.setBno(rs.getInt("bno"));
						board.setBtitle(rs.getString("btitle"));
						board.setBcontent(rs.getString("bcontent"));
						board.setBwriter(rs.getString("bwriter"));
						board.setBdate(rs.getDate("bdate"));
						System.out.printf("%-6s%-12s%-16s%-40s \n",
								board.getBno(),
								board.getBwriter(),
								board.getBdate(),
								board.getBtitle()
								);
					}
						rs.close();
						pstmt.close();
						
			}catch (SQLException e) {
				e.printStackTrace();
				exit();
			}
			mainMenu();

		}
		
		public void mainMenu() {
			System.out.println();
			System.out.println("-------------------------------------------------------------");
			System.out.println("메인 메뉴: 1.Create | 2.Read | 3.Clear | 4.Exit");
			System.out.print("메뉴 선택: ");
			String menuNo = sc.nextLine();
			System.out.println();
			
			switch(menuNo) {
			case "1" -> create();
			case "2" -> read();
			case "3" -> clear();
			case "4" -> exit();
			}
		}
		private void create() {
			
			Board board = new Board();
			System.out.println("[새 게시물 입력]");
			System.out.print("제목: ");
			board.setBtitle(sc.nextLine());
			System.out.print("내용: ");
			board.setBcontent(sc.nextLine());
			System.out.print("작성자: ");
			board.setBwriter(sc.nextLine());
			
			System.out.println("-----------------------------------------------------");
			System.out.println("보조 메뉴 : 1.Ok | 2.Cancel");
			System.out.print("메뉴 선택: ");
			String menuNo = sc.nextLine();
			if(menuNo.equals("1")) {
				try {
					String sql = ""+ "INSERT INTO boards (bno, btitle, bcontent, bwriter, bdate) " + 
				"VALUES (SEQ_BNO.NEXTVAL, ?, ?, ?, SYSDATE)";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, board.getBtitle());
					pstmt.setString(2, board.getBcontent());
					pstmt.setString(3, board.getBwriter());
					pstmt.close();
					} catch (Exception e) {
					e.printStackTrace();
					exit();
				}
			}
			
			
			
			list();
		}
		
		private void read() {
			
			System.out.println("[게시물 읽기]");
			System.out.print("bno: ");
			int bno = Integer.parseInt(sc.nextLine());
			
			try {
				String sql = ""+
						"SELECT bno, btitle, bcontent, bwriter, bdate " + 
						"FROM boards " +
						"WHERE bno=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, bno);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()) {
					Board board = new Board();
					board.setBno(rs.getInt("bno"));
					board.setBtitle(rs.getString("btitle"));
					board.setBcontent(rs.getString("bcontent"));
					board.setBwriter(rs.getString("bwriter"));
					board.setBdate(rs.getDate("bdate"));
					System.out.println("#####################");
					System.out.println("번호: " + board.getBno());
					System.out.println("제목: " + board.getBtitle());
					System.out.println("내용: " + board.getBcontent());
					System.out.println("작성자: " + board.getBwriter());
					System.out.println("날짜: " + board.getBdate());
					System.out.println("#####################");
				}
				rs.close();
				pstmt.close();
				} catch (Exception e) {
				e.printStackTrace();
				exit();
			}
			
			list();
		}
		
		private void clear() {
			System.out.println("*** clear()메소드 실행됨");
			list();
		}
		
		private void exit() {
			System.exit(0);
		}
		
		public static void main(String[] args) {
			BoardExample5 boardExample = new BoardExample5();
			boardExample.list();
			
		}
}
