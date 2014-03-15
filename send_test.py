#!/usr/bin/env python
#-*- coding:utf8 -*-
import urllib, urllib2, json
from Crypto.Cipher import AES

def main():
    url = 'http://localhost:9010'
    #data = '{"appChannel":"qq","appVersion":"0.1","content":"内容_test","costTime":300,"dataType":"m","deviceModel":"小米","deviceNetWork":"wifi","deviceOs":"android","deviceOsVersion":"4.4","deviceUdid":"2fasf3qw3er23","msgId":"msgid_9527","msgType":"email","occurTime":1394880161307,"subject":"邮件subject_test","to":["wdggat@163.com"],"userId":"userid_0"}'
    data = 'e4de352b365dd25732974830b74607f3174f0e24bf3fdb70c36d32cd578af5d0dbdc39833a561b14751188c3d63e4f0b27f82a347fe9245a4555409749c9013419ce9029608433d187b9f05ae54b2a01c7167b124d87aa85113182f19014050fb95bd606ae1deb210a8a0ce5e4ca349abd1a89193b857af73564c08f60a09e35969333b1bbeee64903592433073e89eda6f0ab9b58a9b2ae99be02260d1bc546f14964067fb27c21fe9642f75371d24db47cd9d7550981bc69d8e898fd211780be62a55e44a807935957e22d77b64bb09a670bac26ee24eab0705134ee495af9c71d021896884e8039a6833a47093e4c0053cf07b29777c4377494365be6d94efae8e1ad94b98d9dce606ed473fcd7eb709f026bdcf983db6d56b1fd02625cd2ff1542cf9e4d7c4fcb74b731cb6fb6e5fbf4db67ed7a89595bf1cb0351932be4bbb389e9d2fee3b06630b666ac9fafa1a8659a8b5cc3ed6b9d6ccfb3823758402dbccd06cbf83c4be6cf7088a863f7fb'
    req = urllib2.Request(url, data, {'Content-Type': 'application/json',
                                  'Connection':'keep-alive',
                              })
    response = urllib2.urlopen(req)
    print response.read()

def aes_enctypt(text):
    KEY = 'liu_helloboy9527'
    encryptor = AES.new(KEY, AES.MODE_ECB)
    return encryptor.encrypt(text)

def aes_enctypt_test():
    text = 'abcde12345'
    expected = '51421149c664e290cf03914a42b07e3c'
    actual = aes_enctypt(text)
    print 'Expected: %s, Actual: %s' % (expected, actual)

if __name__ == '__main__':
    main()
    #aes_enctypt_test()
