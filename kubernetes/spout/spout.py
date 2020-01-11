from flask import Flask
from kafka import KafkaProducer
from requests_queue import RequestsQueue
import subprocess
import os
import select
import logging
import threading
import time

app = Flask(__name__)
logging.basicConfig(level=logging.DEBUG)


def producer(requests_queue):
    kafka_producer = KafkaProducer(bootstrap_servers=os.getenv('KAFKA_ADDRESS'), client_id=os.getenv('KAFKA_GROUP_ID'), value_serializer=str.encode)
    production_interval = int(os.getenv('PRODUCTION_INTERVAL'))
    app.logger.info(os.getenv('KAFKA_ADDRESS'))
    app.logger.info(os.getenv('PRODUCTION_INTERVAL'))
    app.logger.info(os.getenv('KAFKA_GROUP_ID'))
    app.logger.info(os.getenv('KAFKA_MAIN_TOPIC'))
    while True:
        while requests_queue.is_empty() is False:
            app.logger.info('Attenzione coda non vuota quindi potrei prendere')
            try:
                request_time = requests_queue.pull()
                app.logger.info(f'Sto producendo {request_time}')
                kafka_producer.send(os.getenv('KAFKA_MAIN_TOPIC'), value=f'{request_time}')
                app.logger.info(f'Prodotto {request_time}')
            except Exception as e:
                app.logger.info(e)
                pass
        time.sleep(production_interval)

def job(requests_queue):
    f = subprocess.Popen(['tail','-F','/app/logs/access.log'],stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    p = select.poll()
    p.register(f.stdout)
    while True:
        if p.poll(1):
            line = f.stdout.readline().decode()
            tokens = line.split(",")
            try:
                request_time = float(tokens[4])
            except:
                request_time = 0.0
            
            app.logger.info(request_time)
            requests_queue.insert(request_time)

@app.before_first_request
def start_job():
    requests_queue = RequestsQueue()
    threading.Thread(target=producer, args=(requests_queue,)).start()
    threading.Thread(target=job, args=(requests_queue,)).start()
    app.logger.info('Thread started')
    
@app.route('/ping', methods=['GET'])
def ping():
    return "pong"



            





