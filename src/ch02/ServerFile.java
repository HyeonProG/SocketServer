package ch02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerFile {

	public static void main(String[] args) {

		// 준비물
		// 1. 서버 소켓이 필요하다.
		// 2. 포트 번호가 필요하다. (0 ~ 65535 까지 존재)
		// 2-1. 잘 알려진 포트 번호 : 주로 시스템 레벨에서 먼저 선점 - 0 ~ 1023까지 사용
		// 2-2. 등록 가능한 포트 : 1024 ~ 49151 까지 등록 가능
		// 2-3. 동적/사설 포트 번호 : 그 외 임시 사용을 위해 할당 된다.

		// 지역 변수
		ServerSocket serverSocket = null;
		// 일반 try-catch 문
		try {
			serverSocket = new ServerSocket(5001);
			System.out.println("서버를 시작 합니다 - 포트 번호 : 5001");

			Socket socket = serverSocket.accept(); // while --> 소켓 리턴
			System.out.println(">>> 클라이언트가 연결 하였습니다 <<<");

			// 데이터를 전달 받기 위해서는 스트림이 필요하다.
			InputStream input = socket.getInputStream();
			// 문자 기반 스트림
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			// 실제 데이터를 읽는 행위가 필요하다.
			String message = reader.readLine();
			System.out.println("클라이언트 측 메세지 전달 받음 : " + message);

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	} // end of main

} // end of class
