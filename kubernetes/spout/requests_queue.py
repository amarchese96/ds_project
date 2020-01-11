import threading

class RequestsQueue():
    def __init__(self):
        self.requests_list = []
        self.lock = threading.Lock()
    
    def pull(self):
        with self.lock:
            if len(self.requests_list) == 0:
                raise Exception('Queue empty')
            return self.requests_list.pop(0)
    
    def insert(self,response_time):
        with self.lock:
            self.requests_list.append(response_time)
    
    def is_empty(self):
        return len(self.requests_list) == 0
    
