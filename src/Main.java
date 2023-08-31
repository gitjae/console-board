import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String fileName = "board.txt";
        FileWriter fw = null;
        FileReader fr = null;
        BufferedReader br = null;
        File file = new File(fileName);

        String[][] board = null;
        String[][] temp = null;
        int count = 0;			// 전체 게시글 수
        int pageSize = 5;		// 한 페이지에 보여줄 게시글 수
        int curPageNum = 1;		// 현재 페이지 번호
        int pageCount = 1;		// 전체 페이지 갯수
        int startRow = 0;		// 현재 페이지의 게시글 시작 번호
        int endRow = 0;			// 현재 페이지의 게시글 마지막 번호
        int nullCnt = 0;

        if(file.exists()) {
            try {
                fr = new FileReader(fileName);
                br = new BufferedReader(fr);

                String str = "";
                while (br.ready()) {
                    str += br.readLine() + "\n";
                }
                str = str.substring(0,str.length()-1);

                String[] split = str.split("/");
                count = split.length;
                pageCount = 1+(count/5);
                if(count%5==0) {
                    pageCount--;
                }
                board = new String[count][2];
                for(int i=0;i<count;i++) {
                    String[] post = split[i].split(",");
                    board[i][0] = post[0];
                    board[i][1] = post[1];
                }

                br.close();
                fr.close();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        while (true) {
            System.out.println("번호\t제목");
            startRow = (curPageNum-1)*pageSize;
            endRow = curPageNum*pageSize;
            for(int i=startRow; i<endRow; i++) {
                if(i>=count) {
                    System.out.println("게시물이 없습니다.");
                    break;
                }
                else {
                    System.out.println((i+1)+ "\t" + board[i][0]);
                }
            }
            System.out.println("["+curPageNum+"/"+pageCount+"page] 총 게시물 : " + (count));

            System.out.println("[1]이전");
            System.out.println("[2]이후");
            System.out.println("[3]추가하기");
            System.out.println("[4]삭제하기");
            System.out.println("[5]내용확인");

            int choice = scan.nextInt();
            scan.nextLine();

            if(choice==1) {
                if(curPageNum-1<1) {
                    System.out.println("이전 페이지가 없습니다.");
                }
                else {
                    curPageNum--;
                }
            }else if (choice==2) {
                if(curPageNum+1>pageCount) {
                    System.out.println("다음 페이지가 없습니다.");
                }
                else {
                    curPageNum++;
                }
            }
            else if (choice==3 || choice==4) {
                if (choice==3) {
                    System.out.print("제목 : ");
                    String title = scan.nextLine();
                    String content = "";
                    while (true) {
                        System.out.print("내용(종료하려면 enter) : ");
                        String input = scan.nextLine();
                        if(input.equals("")) {
                            break;
                        }
                        else {
                            content+=input+"\n";
                        }
                    }
                    content = content.substring(0,content.length()-1);

                    count++;

                    if(board!=null) {
                        temp = board;
                    }

                    board = new String[count][2];
                    for(int i=0;i<count-1;i++) {
                        board[i][0] = temp[i][0];
                        board[i][1] = temp[i][1];
                    }

                    board[count-1][0] = title;
                    board[count-1][1] = content;

                }else if (choice==4) {
                    System.out.print("삭제할 게시물 번호 : ");
                    int delIdx = scan.nextInt() - 1;
                    scan.nextLine();

                    if(delIdx>count) {
                        System.out.println("삭제할 게시물이 없습니다.");
                    }
                    else {
                        temp = board;
                        int cnt = 0;
                        for(int i=0;i<count;i++) {
                            if(i!=delIdx) {
                                board[cnt][0] = temp[i][0];
                                board[cnt][1] = temp[i][1];
                                cnt++;
                            }
                        }

                        count--;
                    }
                }

                try {		// ----- 파일 저장
                    fw = new FileWriter(fileName);

                    String data = "";
                    for(int i=0;i<count;i++) {
                        data += board[i][0] + "," + board[i][1];
                        if(i<count-1) {
                            data += "/";
                        }
                    }

                    fw.write(data);

                    fw.close();

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else if (choice==5) {
                System.out.print("열람할 게시물 번호 : ");
                int openIdx = scan.nextInt() - 1;
                scan.nextLine();

                if(openIdx>=startRow && openIdx<endRow) {
                    System.out.println("제목 : " + board[openIdx][0]);
                    System.out.println("내용 : " + board[openIdx][1]);
                }
                else {
                    System.out.println("페이지에 없는 게시물 입니다.");
                }

            }

            pageCount = 1+(count/5);
            if(count%5==0) {
                pageCount--;
            }
        }
    }
}