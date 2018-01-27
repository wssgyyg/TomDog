import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class FileServer {
    private static String wwwhome = "/Users/bruce/Documents/GitHub/JavaConcurrency";
    private static int port = 8080;
    private static ThreadPool<HttpRequestHandler> threadPool = new DefaultThreadPool<>(1);

    public static void main(String[] args) {
        // open server socket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e);
            System.exit(-1);
        }
        System.out.println("FileServer accepting connections on port " + port);

        // request handler loop
        while (true) {
            Socket connection = null;
            try {
                connection = socket.accept();
                threadPool.execute(new HttpRequestHandler(connection));

            } catch (Exception e){
                System.out.println("Connection error");
            }
        }
    }

    static class HttpRequestHandler implements Runnable {
        private Socket socket;

        public HttpRequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = new BufferedOutputStream(socket.getOutputStream());
                PrintStream pout = new PrintStream(out);

                // read first line of request (ignore the rest)
                String request = in.readLine();
                if (request == null)
                    return;
                while (true) {
                    String misc = in.readLine();
                    if (misc == null || misc.length() == 0)
                        break;
                }

                String req = request.substring(4, request.length() - 9).trim();

                String path = wwwhome + "/" + req;
                File f = new File(path);

                if (f.isDirectory()) {
                    // if directory, implicitly add 'index.html'
                    path = path + "index.html";
                    f = new File(path);
                }
                try {
                    // send file
                    InputStream file = new FileInputStream(f);
                    pout.print("HTTP/1.0 200 OK\r\n" +
                            "Content-Type: " + guessContentType(path) + "\r\n" +
                            "Date: " + new Date() + "\r\n" +
                            "Server: FileServer 1.0\r\n\r\n");
                    sendFile(file, out); // send raw file
                } catch (FileNotFoundException e) {
                }


                out.flush();
            } catch (IOException e) {
                System.err.println(e);
            }
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }

        private String guessContentType(String path) {
            if (path.endsWith(".html") || path.endsWith(".htm"))
                return "text/html";
            else if (path.endsWith(".txt") || path.endsWith(".java"))
                return "text/plain";
            else if (path.endsWith(".gif"))
                return "image/gif";
            else if (path.endsWith(".class"))
                return "application/octet-stream";
            else if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
                return "image/jpeg";
            else
                return "text/plain";
        }

        private void sendFile(InputStream file, OutputStream out) {
            try {
                byte[] buffer = new byte[1000];
                while (file.available() > 0)
                    out.write(buffer, 0, file.read(buffer));
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}



