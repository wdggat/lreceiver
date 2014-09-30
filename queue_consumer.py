#!/usr/bin/python 

import pika

cre = pika.PlainCredentials('hello', 'world')
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', 5672, virtual_host='/', credentials=cre))
channel = connection.channel()
channel.queue_declare(queue='appmsg', durable=True)
print ' [*] Waiting for messages. To exit press CTRL+C'

def callback(ch, method, properties, body):
    print " [x] Received %r" % (body,)

channel.basic_consume(callback,queue='appmsg',no_ack=True)

channel.start_consuming()
