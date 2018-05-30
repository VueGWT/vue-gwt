#!/usr/bin/env sh

# abort on errors
set -e

echo "-> Build examples"
cd docs/examples
mvn clean package
cd .. # go back in docs

echo "-> Copy built examples to VuePress"
rm .vuepress/public/resources/scripts/*
cp examples/target/vue-gwt-examples-*/VueGwtExamples/* .vuepress/public/resources/scripts/

echo "-> Build VuePress"
vuepress build

# navigate into the build output directory
cd .vuepress/dist

git init
git add -A
git commit -m 'deploy'

git push -f git@github.com:axellience/vue-gwt.git master:gh-pages