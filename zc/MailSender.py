#!/usr/bin/python 
#! coding: utf-8

from email.MIMEText import MIMEText
from email.MIMEMultipart import MIMEMultipart
import smtplib
import sys,os

mail_host = 'smtp.163.com'
mail_user = 'forweibotest@163.com'
mail_pwd = 'testpassword'

def send(mail_to, subject, content, attachments):
    message = content
    body = MIMEText(message)
    msg = MIMEMultipart(message)
    msg.attach(body)

    for atta in attachments:
    	if not os.path.exists(atta): continue
        att = MIMEText(open(atta, 'rb').read(),'base64','utf-8')
        att["Content-Type"] = 'application/octet-stream'
        att["Content-Disposition"] = 'attachment;filename=%s' % atta.split('/')[-1]
        msg.attach(att)

    msg['To'] = mail_to
    msg['from'] = mail_user
    msg['subject'] = subject

    try:
        s = smtplib.SMTP()
        s.connect(mail_host)
        s.login(mail_user,mail_pwd)
        s.sendmail(mail_user, mail_to.split(','), msg.as_string())
        s.close()

        print 'Sent (%s)' % subject + str(attachments) + ' to ' + str(mail_to)
    except Exception,e:
        print e

def main():
    if len(sys.argv) < 4:
        print 'Usage : MalSender.py mail_to subject content attachments'
        return -1;

    mail_to = sys.argv[1]
    subject = sys.argv[2]
    content = sys.argv[3]
    attachments = sys.argv[4:]
    attaches = []
    for atta in attachments:
    	if os.path.isdir(atta):
            for root, dirs, files in os.walk(atta):
    	        for f in files:
    		    attaches.append(os.path.join(root, f))
    	elif os.path.isfile(atta):
    	    attaches.append(os.path.join(os.getcwd(), atta))
    send(mail_to, subject, content, attaches)


if __name__ == '__main__':
    main() 
