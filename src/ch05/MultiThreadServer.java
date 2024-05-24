package ch05;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadServer {

	// 메인 함수
	public static void main(String[] args) {

		System.out.println("=== 서버 실행 ===");

		// 서버 측 소켓을 만들기 위한 준비물
		// 서버 소켓, 포트 번호

		try (ServerSocket serverSocket = new ServerSocket(5000)) {
			Socket socket = serverSocket.accept(); // 클라이언트 대기 --> 연결 요청
			// --> 소켓 객체를 생성 하는 메서드(컬라이언트와 연결된 상태)
			System.out.println("=== client connected ===");

			// 클라이언트와 통신을 위한 스트림을 설정(대상 소켓을 얻었다)
			BufferedReader readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			PrintWriter writerStream = new PrintWriter(socket.getOutputStream(), true);

			// 키보드 스트림 준비
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));

			// 스레드 시작
			startReadThread(readerStream);
			startWriteThread(writerStream, keyboardReader);

			System.out.println("메인 스레드 작업 완료...");

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end of main

	// 클라이언트로 부터 데이터를 읽는 스레드 분리
	private static void startReadThread(BufferedReader bufferedReader) {

		Thread readThread = new Thread(() -> {
			try {
				String clientMessage;
				while ((clientMessage = bufferedReader.readLine()) != null) {
					// 서버 측 콘솔에 클라이언트가 보낸 문자 데이터 출력
					System.out.println("클라이언트에서 온 메세지 : " + clientMessage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThread.start(); // 스레드 실행 -> run() 메서드 진행
	}

	// 서버 측에서 클라이언트로 데이터를 보내는 기능
	private static void startWriteThread(PrintWriter printWriter, BufferedReader keyboardReader) {

		Thread writeThread = new Thread(() -> {
			try {
				String serverMessage;
				while ((serverMessage = keyboardReader.readLine()) != null) {
					printWriter.println(serverMessage);
					printWriter.flush();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
	}

	// 워커 스레드가 종료될 때 까지 기다리는 메서드
	private static void waitForThreadToEnd(Thread thread) {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} // end of class
