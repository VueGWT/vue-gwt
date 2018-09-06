#!/usr/bin/env sh

# abort on errors
set -e

cd docs

echo "-> Build VuePress"
vuepress build

# navigate into the build output directory
cd .vuepress/dist

echo "-> Deploy VuePress doc"

git init
git add -A
git commit -m 'deploy'

git push -f git@github.com:VueGWT/vue-gwt.git master:gh-pages