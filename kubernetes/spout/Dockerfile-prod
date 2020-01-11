FROM python:3
WORKDIR /app
COPY . .
ENV FLASK_APP=spout.py
RUN pip install --no-cache-dir -r requirements.txt
CMD ["python", "-m", "flask", "run", "--host", "0.0.0.0"]
