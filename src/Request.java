import com.sun.xml.internal.bind.v2.model.core.ID;
import com.sun.xml.internal.ws.api.model.MEP;

import java.io.IOException;
import java.io.InputStream;

public class Request {
    private String method;

    private String url;

    private static final String CRLF = "\r\n";
    private InputStream inputStream;
    private String requestInfo;

    public Request() {
        method = "";
        url = "";
    }

    public Request(InputStream inputStream) {
        this();
        this.inputStream = inputStream;

        try {
            byte[] data = new byte[20480];
            int len = 0;
            len = inputStream.read(data);
            requestInfo = new String(data, 0, len);

            parseRequestInfo();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRequestInfo() {
        if (null == requestInfo || (requestInfo = requestInfo.trim()).equals("")) {
            return;
        }

        String firstLine = requestInfo.substring(0, requestInfo.indexOf(CRLF));
        int idx = firstLine.indexOf("/");
        this.method = requestInfo.substring(0, idx).trim();
        this.url = requestInfo.substring(idx, requestInfo.indexOf("HTTP/")).trim();

    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
