package Domain;

/**
 * Created by Lasse on 21-04-2015.
 */

    import java.util.Properties;

    import javax.mail.Message;
    import javax.mail.MessagingException;
    import javax.mail.Session;
    import javax.mail.Transport;
    import javax.mail.internet.AddressException;
    import javax.mail.internet.InternetAddress;
    import javax.mail.internet.MimeMessage;
    import java.io.File;
    import java.io.FileWriter;
    import javax.mail.Authenticator;
    import javax.mail.PasswordAuthentication;


    public class Notifications {
        public void sendEmail(String recipient, String subject, String html){

            try{
                final String fromEmail = "automessagejava@gmail.com"; //requires valid gmail id
                final String password = "sappword123"; // correct password for gmail id
                final String toEmail = recipient; // can be any email id

                System.out.println("TLSEmail Start");
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
                props.put("mail.smtp.port", "587"); //TLS Port
                props.put("mail.smtp.auth", "true"); //enable authentication
                props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
                props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

                //create Authenticator object to pass in Session.getInstance argument
                Authenticator auth = new Authenticator() {
                    //override the getPasswordAuthentication method
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                };
                Session session = Session.getInstance(props, auth);

                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

                System.out.println("Mail Check 2");

                message.setSubject(subject);


                // IF YOU WANT TO SEND HTML, USE THIS LINE OF CODE INSTEAD:
                message.setContent(html, "text/html; charset=utf-8");
                //message.setText("");


                Transport.send(message);
            }catch(Exception ex){
                System.out.println("Mail fail");
                System.out.println(ex);
            }
        }


        public static String createNotificationHTML(String name, int project_id, String new_status) {
            return header + "<h3>Hi, " + name + "</h3>\n" +
                    "<p class=\"lead\">Project #" + project_id + " has advanced to a new step, " + new_status + "</p>\n" +
                    "<!-- Callout Panel -->\n" +
                    "<p class=\"callout\">\n" +
                    "Check out the <a href=\"http://localhost:8080/project?id=" + project_id + "\">changes here</a>\n" +
                    "</p><!-- /Callout Panel -->\n" + footer;
        }

        public static String createWelcomeHTML(String name, String link) {




            return header + "<h3>Welcome to Dell's Campaign Management System, " + name + "!</h3>\n" +
                    "<p class=\"lead\">To set get started, set your password and you will be logged in immediately</p>\n" +
                    "<p>If you need any help with the system, visit our <a href=\"localhost:8080/help\">help section</a></p>\n" +
                    "<p class=\"callout\">\n" +
                    "<a href=\"" + link + "\">Set your password</a>\n" +
                    "</p><!-- /Callout Panel -->\n" + footer;
        }

        private static String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" \"http://www.w3.org/TR/REC-html40/loose.dtd\">\n" +
                "<html style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<head></head>\n" +
                "<body style=\"-webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; height: 100%; margin: 0; padding: 0; width: 100% !important\">\n" +
                "<style type=\"text/css\">\n" +
                "img {\n" +
                "max-width: 100%;\n" +
                "}\n" +
                "body {\n" +
                "-webkit-font-smoothing: antialiased; -webkit-text-size-adjust: none; width: 100% !important; height: 100%;\n" +
                "}\n" +
                "@media only screen and (max-width: 600px) {\n" +
                "  a[class=\"btn\"] {\n" +
                "    display: block !important; margin-bottom: 10px !important; background-image: none !important; margin-right: 0 !important;\n" +
                "  }\n" +
                "  div[class=\"column\"] {\n" +
                "    width: auto !important; float: none !important;\n" +
                "  }\n" +
                "  table.social div[class=\"column\"] {\n" +
                "    width: auto !important;\n" +
                "  }\n" +
                "}\n" +
                "</style>\n" +
                "<div class=\"body\" bgcolor=\"#FFFFFF\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<!-- HEADER -->\n" +
                "<table class=\"head-wrap\" bgcolor=\"#0085C3\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0; width: 100%\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"></td>\n" +
                "<td class=\"header container\" style=\"clear: both !important; display: block !important; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0 auto; max-width: 600px !important; padding: 0\">\n" +
                "<div class=\"content\" style=\"display: block; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0 auto; max-width: 600px; padding: 15px\">\n" +
                "<table bgcolor=\"#0085C3\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0; width: 100%\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"><img src=\"http://f.cl.ly/items/2T3e1p3R2j21261Q3Q3V/dell-logo.png\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; max-width: 100%; padding: 0; width: 50px\"></td>\n" +
                "<td align=\"right\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"><h6 class=\"collapse\" style=\"color: white; font-family: 'HelveticaNeue-Light', 'Helvetica Neue Light', 'Helvetica Neue', Helvetica, Arial, 'Lucida Grande', sans-serif; font-size: 14px; font-weight: normal; line-height: 1.1; margin: 0; padding: 0; text-transform: uppercase\">Campaign management system</h6></td>\n" +
                "</tr></table>\n" +
                "</div>\n" +
                "</td>\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"></td>\n" +
                "</tr></table>\n" +
                "<!-- /HEADER --><!-- BODY --><table class=\"body-wrap\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0; width: 100%\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"></td>\n" +
                "<td class=\"container\" bgcolor=\"#FFFFFF\" style=\"clear: both !important; display: block !important; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0 auto; max-width: 600px !important; padding: 0\">\n" +
                "\n" +
                "<div class=\"content\" style=\"display: block; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0 auto; max-width: 600px; padding: 15px\">\n" +
                "<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0; width: 100%\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n";

        private static String footer =  "</tr></table>\n" +
                "</div>\n" +
                "<!-- /content -->\n" +
                "\n" +
                "</td>\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"></td>\n" +
                "</tr></table>\n" +
                "<!-- /BODY --><!-- FOOTER --><table class=\"footer-wrap\" style=\"clear: both !important; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0; width: 100%\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"></td>\n" +
                "<td class=\"container\" style=\"clear: both !important; display: block !important; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0 auto; max-width: 600px !important; padding: 0\">\n" +
                "\n" +
                "<!-- content -->\n" +
                "<div class=\"content\" style=\"display: block; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0 auto; max-width: 600px; padding: 15px\">\n" +
                "<table style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0; width: 100%\"><tr style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<td align=\"center\" style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">\n" +
                "<p style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; font-size: 14px; font-weight: normal; line-height: 1.6; margin: 0 0 10px; padding: 0\">\n" +
                "<a href=\"#\" style=\"color: #2BA6CB; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">Campaign System</a> |\n" +
                "<a href=\"#\" style=\"color: #2BA6CB; font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\">Dell homepage</a>\n" +
                "</p>\n" +
                "</td>\n" +
                "</tr></table>\n" +
                "</div>\n" +
                "<!-- /content -->\n" +
                "\n" +
                "</td>\n" +
                "<td style=\"font-family: 'Helvetica Neue', 'Helvetica', Helvetica, Arial, sans-serif; margin: 0; padding: 0\"></td>\n" +
                "</tr></table>\n" +
                "<!-- /FOOTER -->\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

