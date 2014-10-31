#!/usr/bin/python 
#coding: utf-8

import os
import sys
import MailSender

#content = "Hello, 猜猜我是谁?<br>下载半匿名社交工具whoami，可'戴上面具'与密友进行畅聊哦 http://a.app.qq.com/o/simple.jsp?pkgname=com.liu.activity"
#content = "hello, 猜猜我是谁?<br>半匿名社交工具whoami，可'戴上面具'与密友进行畅聊哦 http://a.app.qq.com/o/simple.jsp?pkgname=com.liu.activity"
content = r"""
<html>
  <head></head>
  <body>
  <p>hello, 猜猜我是谁?<p><p><p>
   半匿名社交工具<a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.liu.activity">whoami</a>，可'戴上面具'与密友进行畅聊哦 <a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.liu.activity">http://a.app.qq.com/o/simple.jsp?pkgname=com.liu.activity</a>
  </p>
  </body>
</html>
"""

N=1
POS_FILE = 'send_ads.pos'

def get_pos(posf=POS_FILE):
    if not os.path.exists(posf):
        return 0
    line = open(posf).readlines()[0]
    return int(line.strip())

def update_pos(lineno):
    out = open(POS_FILE, 'w')
    out.write('%s' %  lineno)
    out.close()

def get_qqnum_fromline(line):
    if line is None or line.strip() == '':
        return ''
    return line.strip()

def send_ad(qqnums):
    for qq in qqnums:
        mail = qq
	if '@' not in mail:
	    mail = mail + "@qq.com"
        MailSender.send(mail, "hehe", content, [])
        #print 'Ad sent: %s' % qq

def main(qqpath):
    c, qqnums = 0,set()
    pos_lineno = get_pos()
    for line in open(qqpath):
        c += 1
        if c <= pos_lineno:
            continue
        pos_lineno += 1
        qqnums.add(get_qqnum_fromline(line))
	if len(qqnums) >= N:
	    break
    update_pos(pos_lineno)
    send_ad(qqnums)
        
def try_run():
    MailSender.send("hzliuxiaolong@163.com", "hehe", content, [])

if __name__ == '__main__':
    if len(sys.argv) == 1:
        try_run()
    else:
        main(sys.argv[1])

