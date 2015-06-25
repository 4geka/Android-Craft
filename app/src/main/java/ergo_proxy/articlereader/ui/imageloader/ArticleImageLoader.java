package ergo_proxy.articlereader.ui.imageloader;

/**
 * Created by Ergo-Proxy on 23.06.2015.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.CharsetUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

public class ArticleImageLoader extends Request<String>
{

    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    private String FILE_PART_NAME = "files";

    private final Response.Listener<String> mListener;
    private final File mFilePart;
    private final Map<String, String> mStringPart;
    private Map<String, String> headerParams;
    private final MultipartProgressListener multipartProgressListener;
    private long fileLength = 0L;

    private final static String apiKey="7e56b73c3cbb6f63ae70041a4e592e87";


    public ArticleImageLoader(String url, Response.ErrorListener errorListener,
                              Response.Listener<String> listener, File file, long fileLength,
                              Map<String, String> mStringPart,
                              final Map<String, String> headerParams, String partName,
                              MultipartProgressListener progressListener)
    {
        super(Method.POST, url, errorListener);

        this.mListener = listener;
        this.mFilePart = file;
        this.fileLength = fileLength;
        this.mStringPart = mStringPart;
        this.headerParams = headerParams;
        this.FILE_PART_NAME = partName;
        this.multipartProgressListener = progressListener;

        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try
        {
            entity.setCharset(CharsetUtils.get("UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        buildMultipartEntity();
        httpentity = entity.build();
    }

    private void buildMultipartEntity()
    {
        entity.addPart(FILE_PART_NAME, new FileBody(mFilePart, ContentType.create("image/jpeg"), mFilePart.getName()));
        if (mStringPart != null) {
            for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
                entity.addTextBody(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String getBodyContentType()
    {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try
        {
            httpentity.writeTo(new CountingOutputStream(bos, fileLength,
                    multipartProgressListener));
        }
        catch (IOException e)
        {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response)
    {

        try
        {
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();

            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response)
    {
        mListener.onResponse(response);
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {

        Map<String, String> headers = headers = new HashMap<>();

        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Token token=" + apiKey);
        return headers;
    }

    public static interface MultipartProgressListener
    {
        void transferred(long transferred, int progress);
    }

    public static class CountingOutputStream extends FilterOutputStream
    {
        private final MultipartProgressListener progressListener;
        private long transferred;
        private long fileLength;

        public CountingOutputStream(final OutputStream out, long fileLength,
                                    final MultipartProgressListener listener)
        {
            super(out);
            this.fileLength = fileLength;
            this.progressListener = listener;
            this.transferred = 0;
        }

        public void write(byte[] b, int off, int len) throws IOException
        {
            out.write(b, off, len);
            openFileToString(b);
            if (progressListener != null)
            {
                this.transferred += len;
                int prog = (int) (transferred * 100 / fileLength);
                this.progressListener.transferred(this.transferred, prog);
            }
        }

        public void write(int b) throws IOException
        {
            out.write(b);
            if (progressListener != null)
            {
                this.transferred++;
                int prog = (int) (transferred * 100 / fileLength);
                this.progressListener.transferred(this.transferred, prog);
            }
        }
        public String openFileToString(byte[] bytes)
        {
            String s = null;
            try
            {
                s = new String(bytes, "UTF-8");

            }
            catch (Exception e ) {}
            return s;
        }

    }
}
