import urllib, urllib2, json

# ---------------------------------- #
#url = 'http://localhost:8080/msg-dispatcher/msgtemplate?action=query&id=10'
#url = 'http://products.da.netease.com/msg-dispatcher/msgtemplate?action=query&id=1'
#url = 'http://10.120.104.6/msg-dispatcher/msgtemplate?action=query&id=10'

# ---------------------------------- #
#url = 'http://10.120.104.6/msg-dispatcher/sendmsg'
#url = 'http://products.da.netease.com/msg-dispatcher/sendmsg'
#url = 'http://localhost:8080/msg-dispatcher/sendmsg'
#url = 'http://localhost:8080'
url = 'http://10.120.104.184:9999'
# 15024405406
data = json.dumps({"imParams": {"year": "2013", "month": "12", "day": "03", "num":"0x7243a2d8d"},
                   "smsParams": {"target": "cat", "animal": "pig"},
                   "from": "1505001", "to": ["18605811280"],
                   "imTemplateId": "100", "smsTemplateId": "1",
                   "imType":0})

#data = json.dumps({"templateId": "104", "action": "change"})

req = urllib2.Request(url, data, {'Content-Type': 'application/json',
                                  'Connection':'keep-alive',
                                  #'Request-Type':'sendMsg',
                              })


response = urllib2.urlopen(req)

# ---------------------------------- #

# url = 'https://plus.yixin.im/cgi-bin/token?grant_type=client_credential&appid=d4c1b9675daa412299cb8852f5ba4111&secret=9c6b0a57211b407080c91de9ec9a4a46'
# print 'Http Get: ' + url
# response = urllib.urlopen(url)
# data = json.loads(response.read())

# url = 'https://plus.yixin.im/cgi-bin/sms/send?access_token=' + data['access_token']
# print 'Http Post: ' + url

# data = json.dumps(
#     {
#         "to":"18605811280",
#         "imTemplateId":"101",
#         "smsTemplateId": "102",
#         "imType":"1",
#         "smsParams":{"phone":"18605811280","number":"1000"},
#         "imParams": {"phone":"18605811280","number":"1000"}
#     }
# )
# print 'Input: ' + data
# req = urllib2.Request(url, data, {'Content-Type': 'application/json'})
# response = urllib2.urlopen(req)


# ---------------------------------- #
print response.read()
