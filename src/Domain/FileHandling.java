package Domain;

/**
 * Created by Lasse on 19-04-2015.
 */
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import java.io.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@MultipartConfig
public class FileHandling  {

    String filename;
    String filetype;
    String path = "C:\\Users\\Lasse\\Desktop\\File Storage Dell";

    public void putFile(Part file) throws IOException {
        filename = getFileName(file);
        filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());



        OutputStream out = null;
        InputStream filecontent = null;

        try {
            out = new FileOutputStream(new File(path + File.separator
                    + filename));
            filecontent = file.getInputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];

            while ((read = filecontent.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

        } catch (FileNotFoundException fne) {

        } finally {
            if (out != null) {
                out.close();
            }
            if (filecontent != null) {
                filecontent.close();
            }

        }
    }


    public String getFileName(Part file) {

        for (String content : file.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;

    }

    public String getFileName() {
        return filename;
    }
    public String getFileType() {
        return filetype;
    }

}