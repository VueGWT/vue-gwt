#!/usr/bin/env sh

# abort on errors
set -e

echo "-> Build examples"
mvn clean package
cd .. # go back in docs

echo "-> Copy built examples to VuePress"
rm .vuepress/public/resources/scripts/*
cp examples/target/vue-gwt-examples-*/VueGwtExamples/* .vuepress/public/resources/scripts/