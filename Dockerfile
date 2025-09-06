FROM ubuntu:latest
LABEL authors="aysee"

ENTRYPOINT ["top", "-b"]