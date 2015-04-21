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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@MultipartConfig
public class FileHandling {

    String filename;
    String filetype;

    public void putFile(Part file, int project_id) throws IOException {

       // String path = System.getProperty("user.dir");
       // String projectRoot = path.substring(0, path.lastIndexOf("\\")) + "\\Poe\\";
       // String PoeRootByProjectId = projectRoot + project_id;

        String path = System.getenv("POE_FOLDER");

        String newPath = path + "\\" + project_id;

        File dir = new File(path + "\\" + project_id);

        dir.mkdir();

        filename = getFileName(file);
        filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            out = new FileOutputStream(new File(newPath + File.separator
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

    public void putLogo(Part file, int company_id) throws IOException {
        String path = System.getenv("POE_FOLDER");

        String newPath = path + File.separator + "companies" + File.separator + company_id;

        File dir = new File(newPath);

        dir.mkdir();

        filename = getFileName(file);
        filetype = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

        OutputStream out = null;
        InputStream filecontent = null;

        try {
            out = new FileOutputStream(new File(newPath + File.separator
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

    public boolean deleteFile(String filename, int project_id) throws IOException {
       try {
           String stringpath = System.getenv("POE_FOLDER") + "\\" + project_id + "\\" + filename;
           Path path = Paths.get(stringpath);
           Files.delete(path);
       } catch (Exception e) {
           System.out.println("didnt delete file");
           return false;
       }
        return true;
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