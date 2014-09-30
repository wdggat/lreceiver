#!/usr/bin/python 

import pika

cre = pika.PlainCredentials('hello', 'world')
connection = pika.BlockingConnection(pika.ConnectionParameters('localhost', 5672, virtual_host='/', credentials=cre))
channel = connection.channel()
channel.queue_declare(queue="appmsg", durable=True)
channel.basic_publish(exchange='', routing_key='appmsg', body='hello rabbitmq', properties = pika.BasicProperties(delivery_mode=2))
print '[x] Sent "hello rabbitmq"'
connection.close()

