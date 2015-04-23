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
        public void sendEmail(String recipient, String subject, String emailMessage){

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
                // message.setContent(someHtmlMessage, "text/html; charset=utf-8");
                message.setText(emailMessage);

                System.out.println("Mail Check 3");

                Transport.send(message);
                System.out.println("Mail Sent");
            }catch(Exception ex){
                System.out.println("Mail fail");
                System.out.println(ex);
            }
        }
    }

