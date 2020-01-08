FROM python:3
WORKDIR /app
COPY . .
ENV FLASK_APP=video_processing_service.py
RUN apt-get update -y && apt-get install -y ffmpeg -y && pip install --no-cache-dir -r requirements.txt
CMD ["python", "-m", "flask", "run", "--host", "0.0.0.0"]
