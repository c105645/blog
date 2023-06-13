#!/bin/sh

echo "\n🏴️ Destroying Kubernetes cluster...\n"

minikube stop --profile blog-cluster

minikube delete --profile blog-cluster

echo "\n🏴️ Cluster destroyed\n"
