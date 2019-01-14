package com.lsz.depot.local.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FastByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.function.Function;

@Slf4j
public class CachedResponse extends HttpServletResponseWrapper {

    private final ResponseServletOutputStream outputStream = new ResponseServletOutputStream();

    private PrintWriter writer;

    private CachedResponse(HttpServletResponse response) {
        super(response);
        writer = new PrintWriter(outputStream);
    }

    public static CachedResponse wrap(HttpServletResponse response) {
        if (response instanceof CachedResponse) {
            return (CachedResponse) response;
        } else {
            return new CachedResponse(response);
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.writer;
    }

    @Override
    public void reset() {
        super.reset();
        outputStream.content.reset();
    }

    public void finalize() throws Throwable {
        super.finalize();
        outputStream.close();
    }

    private ServletOutputStream getRealOutputStream() throws IOException {
        return super.getOutputStream();
    }

    void process(Function<String, String> processor) {
        try {
            if (processor!= null && StringUtils.containsIgnoreCase(this.getContentType(), "application/json")) {
                String current = new String(outputStream.content.toByteArray(), getCharacterEncoding());
                String result = processor.apply(current);
                outputStream.content.reset();
                outputStream.content.write(result.getBytes(getCharacterEncoding()));
            }
            outputStream.trans();
        } catch (UnsupportedEncodingException e) {
            log.error("Exception when process mapping code 1", e);
        } catch (IOException e) {
            log.error("Exception when process mapping code 2", e);
        }
    }

    private class ResponseServletOutputStream extends ServletOutputStream {
        private final FastByteArrayOutputStream content = new FastByteArrayOutputStream(1024);

        @Override
        public void write(int b) throws IOException {
            content.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            content.write(b, off, len);
        }

        void trans() throws IOException {
            content.writeTo(getRealOutputStream());
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}
