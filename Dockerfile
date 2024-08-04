# Use the latest Jenkins base image
FROM jenkins/jenkins:latest

# Switch to root user to install packages
USER root

# Install Docker CLI
RUN apt-get update && \
    apt-get install -y apt-transport-https ca-certificates curl software-properties-common gnupg2 && \
    curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add - && \
    add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable" && \
    apt-get update && \
    apt-get install -y docker-ce-cli

# Install Maven
RUN apt-get install -y maven

# Install Node.js (and npm) and Yarn
RUN curl -fsSL https://deb.nodesource.com/gpgkey/nodesource.gpg.key | apt-key add - && \
    echo "deb https://deb.nodesource.com/node_14.x $(lsb_release -cs) main" | tee /etc/apt/sources.list.d/nodesource.list && \
    apt-get update && \
    apt-get install -y nodejs && \
    apt-get install -y npm && \
    npm install --global yarn

# Install AWS CLI
RUN curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip" && \
    unzip awscliv2.zip && \
    ./aws/install

# Install Docker Compose
RUN curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose && \
    chmod +x /usr/local/bin/docker-compose

# Install Git
RUN apt-get install -y git

# Clean up
RUN apt-get clean && \
    rm -rf /var/lib/apt/lists/* awscliv2.zip

# Set default AWS region and output format (optional)
ENV AWS_DEFAULT_REGION=us-east-1
ENV AWS_DEFAULT_OUTPUT=json

# Switch back to the Jenkins user
USER jenkins

# Set Java memory options
ENV JAVA_OPTS="-Xmx2048m"

# Health check to ensure Jenkins is running
HEALTHCHECK --interval=1m --timeout=5s CMD curl -f http://localhost:8080/login || exit 1