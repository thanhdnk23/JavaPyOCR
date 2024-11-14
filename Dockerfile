# Stage 1: Build the Java application
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app

# Sao chép file cấu hình và mã nguồn vào container
COPY pom.xml .
COPY src ./src

# Build ứng dụng bằng Maven
RUN mvn clean install -B

# Stage 2: Run the application with JRE 21, Docker CLI, and Python dependencies
FROM python:3.10-slim AS base
WORKDIR /app

# Cài đặt Docker CLI và các phụ thuộc hệ thống cần thiết
RUN apt-get update && \
    apt-get install -y \
    libgl1 libglib2.0-0 libgomp1 wget && \
    rm -rf /var/lib/apt/lists/*

# Tạo và kích hoạt môi trường ảo cho Python
RUN python3 -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

# Cài đặt pip bằng get-pip.py
RUN wget https://bootstrap.pypa.io/get-pip.py -O /tmp/get-pip.py && \
    python3 /tmp/get-pip.py

# Kích hoạt môi trường ảo và cài đặt các gói cần thiết
RUN /opt/venv/bin/pip install --upgrade pip setuptools
COPY requirements.txt /app/
RUN /opt/venv/bin/pip install -r requirements.txt

# Sao chép các tệp cần thiết vào container tại thư mục /app
COPY . /app/
COPY --from=build /app/target/ocrweb-0.0.1-SNAPSHOT.jar /app/ocrweb.jar 

# Expose port cho ứng dụng
EXPOSE 8080

# Chạy ứng dụng Java
CMD ["java", "-jar", "ocrweb.jar"]
