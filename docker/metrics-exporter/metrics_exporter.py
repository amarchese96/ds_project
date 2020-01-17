from flask import Flask
import threading
import subprocess
import select
import logging
import time

app = Flask(__name__)
logging.basicConfig(level=logging.DEBUG)

total_request_count = 0
last_request_length = 0
last_body_bytes_sent = 0
last_request_time = 0.0
total_errors = 0

endpoint_request_count = {}

def error_logs_job():
    global total_errors
    f = subprocess.Popen(['tail','-F','/app/logs/access.log'],stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    p = select.poll()
    p.register(f.stdout)
    while True:
        if p.poll(1):
            f.stdout.readline()
            total_errors += 1

def access_logs_job():
    global total_request_count
    global last_request_length
    global last_body_bytes_sent
    global last_request_time
    global endpoint_request_count
    f = subprocess.Popen(['tail','-F','/app/logs/access.log'],stdout=subprocess.PIPE,stderr=subprocess.PIPE)
    p = select.poll()
    p.register(f.stdout)
    while True:
        if p.poll(1):
            line = f.stdout.readline().decode()
            tokens = line.split(",")
            if tokens[1] not in endpoint_request_count:
                endpoint_request_count[tokens[1]]=1
            else:
                endpoint_request_count[tokens[1]]+=1
            last_request_length = int(tokens[2])
            last_body_bytes_sent = int(tokens[3])
            last_request_time = float(tokens[4])
            total_request_count += 1

@app.before_first_request
def start_jobs():
    task = threading.Thread(target=access_logs_job)
    task.start()
    app.logger.info(f'Access logs thread started')
    task = threading.Thread(target=error_logs_job)
    task.start()
    app.logger.info(f'Error logs thread started')
    
@app.route('/', methods=['GET'])
def ping():
    return "pong"

@app.route('/metrics', methods=['GET'])
def get_metrics():
    response = f"total_request_count {total_request_count}\n"
    response += f"last_request_length {last_request_length}\n"
    response += f"last_body_bytes_sent {last_body_bytes_sent}\n"
    response += f"last_request_time {last_request_time}\n"
    response += f"total_errors {total_errors}\n"
    for key,value in endpoint_request_count.items():
        response += f'endpoint_request_count{{endpoint="{key}"}} {value}\n'
    return response


