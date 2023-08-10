FROM python:latest

ENV PIP_ROOT_USER_ACTION=ignore

WORKDIR /app

COPY requirements.txt requirements.txt

RUN pip3 install -r requirements.txt

COPY app.py ./

CMD ["python", "-u", "app.py"] 