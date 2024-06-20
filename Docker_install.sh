#!/bin/bash

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 UBUNTU_VERSION ARCH"
    exit 1
fi

UBUNTU_VERSION=$1
ARCH=$2

files=$(wget -q https://download.docker.com/linux/ubuntu/dists/$UBUNTU_VERSION/pool/stable/$ARCH/ -O - | grep -o 'href="[^"]*.deb"' | grep -o '[^"]*.deb')

latest_containerd=$(echo "$files" | grep 'containerd.io' | sort -V | tail -n 1)
latest_docker_ce=$(echo "$files" | grep 'docker-ce_' | sort -V | tail -n 1)
latest_docker_ce_cli=$(echo "$files" | grep 'docker-ce-cli_' | sort -V | tail -n 1)
latest_docker_buildx=$(echo "$files" | grep 'docker-buildx-plugin_' | sort -V | tail -n 1)
latest_docker_compose=$(echo "$files" | grep 'docker-compose-plugin_' | sort -V | tail -n 1)


wget https://download.docker.com/linux/ubuntu/dists/$UBUNTU_VERSION/pool/stable/$ARCH/$latest_containerd
wget https://download.docker.com/linux/ubuntu/dists/$UBUNTU_VERSION/pool/stable/$ARCH/$latest_docker_ce
wget https://download.docker.com/linux/ubuntu/dists/$UBUNTU_VERSION/pool/stable/$ARCH/$latest_docker_ce_cli
wget https://download.docker.com/linux/ubuntu/dists/$UBUNTU_VERSION/pool/stable/$ARCH/$latest_docker_buildx
wget https://download.docker.com/linux/ubuntu/dists/$UBUNTU_VERSION/pool/stable/$ARCH/$latest_docker_compose

sudo dpkg -i $latest_containerd \
  $latest_docker_ce \
  $latest_docker_ce_cli \
  $latest_docker_buildx \
  $latest_docker_compose

sudo service docker start
sudo docker run hello-world
