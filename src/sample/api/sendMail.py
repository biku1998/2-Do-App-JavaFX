import smtplib
import sys
import config

def send_email(subject,msg):

    try:
        server = smtplib.SMTP('smtp.gmail.com:587')
        server.ehlo()
        server.starttls()
        emails = [sys.argv[3]]
        server.login(config.EMAIL,config.PASSWORD)
        message = 'Subject :{}\n\n{}'.format(subject,msg)

        server.sendmail(config.EMAIL,emails,message)
        
        server.quit()

        print("Mail sent")

    except:
        print("sending failed due to ")


send_email(sys.argv[1],sys.argv[2]) #(subject , message , emailToSend)
