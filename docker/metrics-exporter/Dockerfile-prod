FROM python:3
WORKDIR /app
COPY . .
ENV FLASK_APP=metrics_exporter.py
RUN pip install --no-cache-dir -r requirements.txt
CMD ["python", "-m", "flask", "run", "--host", "0.0.0.0", "--port", "3000"]
